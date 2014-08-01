/* Edge.java */

package graphalg;

/**
 * The Edge class represents an edge in a weighted, undirected graph.
 * An edge is represented by 2 vertices that it connects: v1 and v2.
 */

public class Edge implements Comparable {

  private Object v1;
  private Object v2;
  private int weight;

  /**
   * Edge() creates an Edge object with v1 and v2 being the 2 vertices
   * connected by "this" edge. The numbering of the vertices is arbitrary
   * since this is an edge in an undirected graph.
   */
  public Edge(Object v1, Object v2, int weight) {
    this.v1 = v1;
    this.v2 = v2;
    this.weight = weight;
  }

  /**
   * getV1() returns the first vertex object represented by this edge.
   */
  public Object getV1() {
    return v1;
  }

  /**
   * getV2() returns the second vertex object represented by this edge.
   */
  public Object getV2() {
    return v2;
  }

  /**
   * getWeight() returns the weight of this edge.
   */
  public int getWeight() {
    return weight;
  }

  /**
   * overwrite compareTo() method for Comparable to take into account
   * Edge objects to compare weights.
   * @param o: the object (cast to edge) being compared against
   * @return -1 if this wedge's weight is less than o's weight, 0 if
   * this edge's weight is equal to o's weight, and 1 if greater.
   */
  public int compareTo(Object o) {
    Edge e = (Edge) o;
    if (this.getWeight() < e.getWeight()) {
      return -1;
    } else if (this.getWeight() == e.getWeight()) {
      return 0;
    } else {
      return 1;
    }
  }

  /**
   * toString() returns a string representation of "this" edge. Used for 
   * debugging purposes.
   */
  public String toString() {
    return "[v1]---" + weight + "---[v2]";
  }

}