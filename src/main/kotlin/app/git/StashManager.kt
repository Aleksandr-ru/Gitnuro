package app.git

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import javax.inject.Inject

class StashManager @Inject constructor() {
    private val _stashStatus = MutableStateFlow<StashStatus>(StashStatus.Loaded(listOf()))
    val stashStatus: StateFlow<StashStatus>
        get() = _stashStatus

    suspend fun stash(git: Git) = withContext(Dispatchers.IO) {
        git
            .stashCreate()
            .setIncludeUntracked(true)
            .call()

        loadStashList(git)
    }

    suspend fun popStash(git: Git) = withContext(Dispatchers.IO) {

        git
            .stashApply()
            .call()

        git.stashDrop()
            .call()

        loadStashList(git)
    }

    suspend fun loadStashList(git: Git) = withContext(Dispatchers.IO) {
        _stashStatus.value = StashStatus.Loading

        val stashList = git
            .stashList()
            .call()

        _stashStatus.value = StashStatus.Loaded(stashList.toList())
    }
}


sealed class StashStatus {
    object Loading : StashStatus()
    data class Loaded(val stashes: List<RevCommit>) : StashStatus()
}