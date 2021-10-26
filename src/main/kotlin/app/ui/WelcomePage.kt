package app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.git.GitManager
import openRepositoryDialog
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import app.extensions.dirName
import app.extensions.dirPath
import app.theme.primaryTextColor
import app.theme.secondaryTextColor
import java.awt.Desktop
import java.net.URI


@Composable
fun WelcomePage(gitManager: GitManager) {
    val appStateManager = gitManager.appStateManager

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = BiasAlignment.Vertical(-0.5f),
    ) {
        Column(
            modifier = Modifier
                .padding(end = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Gitnuro",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )

            ButtonTile(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                title = "Open a repository",
                painter = painterResource("open.svg"),
                onClick = { openRepositoryDialog(gitManager) }
            )

            ButtonTile(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                title = "Clone a repository",
                painter = painterResource("open.svg"),
                onClick = { }
            )

            ButtonTile(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                title = "Start a local repository",
                painter = painterResource("open.svg"),
                onClick = { }
            )

            Text(
                text = "About Gitnuro",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
            )

            IconTextButton(
                title = "Source code",
                painter = painterResource("code.svg"),
                onClick = {
                    Desktop.getDesktop().browse(URI("https://github.com/aeab13/Gitnuro"))
                }
            )

            IconTextButton(
                title = "Report a bug",
                painter = painterResource("bug.svg"),
                onClick = {
                    Desktop.getDesktop().browse(URI("https://github.com/aeab13/Gitnuro/issues"))
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 32.dp),
        ) {

            Text(
                text = "Recent",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
            )
            LazyColumn {
                items(items = appStateManager.latestOpenedRepositoriesPaths) { repo ->
                    val repoDirName = repo.dirName
                    val repoDirPath = repo.dirPath

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(
                            onClick = {
                                gitManager.openRepository(repo)
                            }
                        ) {
                            Text(
                                text = repoDirName,
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.primary,
                            )
                        }

                        Text(
                            text = repoDirPath,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 4.dp),
                            color = MaterialTheme.colors.secondaryTextColor
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun ButtonTile(
    modifier: Modifier = Modifier,
    title: String,
    painter: Painter,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(width = 280.dp, height = 56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp),
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            )

            Text(
                text = title,
            )
        }
    }
}

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    title: String,
    painter: Painter,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(width = 280.dp, height = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp),
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            )

            Text(
                text = title,
            )
        }
    }
}
