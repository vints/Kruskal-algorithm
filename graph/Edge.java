
package graph;
import list.*;

/**
 * The Edge class is used to represent edges via method iii
 * (under design element 4 in the readme). An edge has a
 * reference to the two DLists of the vertices the edge connects to
 * that represents which edges each vertex is connected to.
 */

class Edge {
    protected DListNode thisDListNode;
    protected DListNode otherDListNode;
    protected Object thisVertex;
    protected Object otherVertex;
    protected int weight;


    /**
     * Creates an Edge object with start vertex "start", end vertex "end", and references
     * to the DList of edges for both "start" and "end".
     */
    protected Edge(Object thisVertex, Object otherVertex, DListNode thisDList, DListNode otherDList, int weight) {
        this.thisVertex = thisVertex;
        this.otherVertex = otherVertex;
        this.thisDListNode = thisDList;
        this.otherDListNode = otherDList;
        this.weight = weight;
    }



}
