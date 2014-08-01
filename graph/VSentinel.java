
package graph;
import list.*;

/**
 * The VSentinel Class is used to link a vertex to a DList of edges.
 */

class VSentinel {
    protected Object vertex;
    protected DList edges;


    /**
     * Creates a VSentinel that links a vertex to its edges.
     * A reference to this VSentinel is kept by the DList
     * "vertices" in WUGraph. Only for use by WUGraph.
     */
    protected VSentinel(Object vertex, DList edges) {
        this.vertex = vertex;
        this.edges = edges;
    }




}
