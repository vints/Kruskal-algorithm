/* Vertex.java */

package graphalg;

/**
 * The Vertex class represents a vertex in a weighted, undirected graph.
 * A Vertex knows the Object that is being used to represent it as well as
 * its rank - a unique integer assigned to a vertex when it is created
 * (used for the implementation of Kruskal's algorithm utilizing 
 * the disjoint set data structure).
 */

public class Vertex {

  private Object obj;
  private int rank;

  /**
   * Vertex() creates a Vertex object with anyObject serving as the
   * vertex in a WUGraph.
   */
  public Vertex(Object anyObject, int rank) {
    obj = anyObject;
    this.rank = rank;
  }

  /**
   * getObject() returns the object serving as the vertex in a
   * WUGraph
   */
  public Object getObject() {
    return obj;
  }

  /**
   * getRank() returns the unique integer associated with "this" Vertex.
   */
  public int getRank() {
    return rank;
  }

  /**
   * toString() returns a string representation of "this" Vertex. Used for
   * debugging purposes.
   */
  public String toString() {
    return "[" + rank + "]";
  }

}