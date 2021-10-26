@file:Suppress("unused")

package app.git.graph

import org.eclipse.jgit.lib.AnyObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit

val NO_CHILDREN = arrayOf<GraphNode>()
val NO_LANES = arrayOf<GraphLane>()
val NO_REFS = listOf<Ref>()
val NO_LANE = GraphLane(INVALID_LANE_POSITION)

open class GraphNode(id: AnyObjectId?) : RevCommit(id), IGraphNode {
    var forkingOffLanes: Array<GraphLane> = NO_LANES
    var passingLanes: Array<GraphLane> = NO_LANES
    var mergingLanes: Array<GraphLane> = NO_LANES
    var lane: GraphLane = NO_LANE
    var children: Array<GraphNode> = NO_CHILDREN
    var refs: List<Ref> = NO_REFS

    fun addForkingOffLane(graphLane: GraphLane) {
        forkingOffLanes = addLane(graphLane, forkingOffLanes)
    }

    fun addPassingLane(graphLane: GraphLane) {
        passingLanes = addLane(graphLane, passingLanes)
    }

    fun addMergingLane(graphLane: GraphLane) {
        mergingLanes = addLane(graphLane, mergingLanes)
    }

    fun addChild(c: GraphNode) {
        when (val childrenCount = children.count()) {
            0 -> children = arrayOf(c)
            1 -> if (!c.id.equals(children[0].id)) children = arrayOf(children[0], c)
            else -> {
                for (pc in children)
                    if (c.id.equals(pc.id))
                        return

                val n: Array<GraphNode> = children.copyOf(childrenCount + 1).run {
                    this[childrenCount] = c
                    requireNoNulls()
                }

                n[childrenCount] = c
                children = n
            }
        }
    }

    val childCount: Int
    get() {
        return children.size
    }

    /**
     * Get the nth child from this commit's child list.
     *
     * @param nth
     * child index to obtain. Must be in the range 0 through
     * [.getChildCount]-1.
     * @return the specified child.
     * @throws ArrayIndexOutOfBoundsException
     * an invalid child index was specified.
     */
    fun getChild(nth: Int): GraphNode {
        return children[nth]
    }

    /**
     * Determine if the given commit is a child (descendant) of this commit.
     *
     * @param c
     * the commit to test.
     * @return true if the given commit built on top of this commit.
     */
    fun isChild(c: GraphNode): Boolean {
        for (a in children)
            if (a === c)
                return true

        return false
    }

    /**
     * Get the number of refs for this commit.
     *
     * @return number of refs; always a positive value but can be 0.
     */
    fun getRefCount(): Int {
        return refs.size
    }

    /**
     * Get the nth Ref from this commit's ref list.
     *
     * @param nth
     * ref index to obtain. Must be in the range 0 through
     * [.getRefCount]-1.
     * @return the specified ref.
     * @throws ArrayIndexOutOfBoundsException
     * an invalid ref index was specified.
     */
    fun getRef(nth: Int): Ref {
        return refs[nth]
    }


    /** {@inheritDoc}  */
    override fun reset() {
        forkingOffLanes = NO_LANES
        passingLanes = NO_LANES
        mergingLanes = NO_LANES
        children = NO_CHILDREN
        lane = NO_LANE
        super.reset()
    }

    private fun addLane(graphLane: GraphLane, lanes: Array<GraphLane>): Array<GraphLane> {
        var newLines = lanes

        when (val linesCount = newLines.count()) {
            0 -> newLines = arrayOf(graphLane)
            1 -> newLines = arrayOf(newLines[0], graphLane)
            else -> {
                val n = newLines.copyOf(linesCount + 1).run {
                    this[linesCount] = graphLane
                    requireNoNulls()
                }

                newLines = n
            }
        }

        return newLines
    }

    override val graphParentCount: Int
        get() = parentCount

    override fun getGraphParent(nth: Int): GraphNode {
        return getParent(nth) as GraphNode
    }
}