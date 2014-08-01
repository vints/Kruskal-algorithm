/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;
import queue.*;
import dict.*;
import java.util.Random;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  private int vertexRank = 0;

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {

    // t will be the MST of g
    WUGraph t = new WUGraph();
    Object[] allVertices = g.getVertices();

    // Make t have same vertices as g
    for (int i = 0; i < allVertices.length; i++) {
      t.addVertex(allVertices[i]);
    }

    // Make a list of all edges in g
    LinkedQueue allEdges = new LinkedQueue();
    for (int i = 0; i < allVertices.length; i++) {
      Object currVertex = allVertices[i];
      Neighbors neighbors = g.getNeighbors(currVertex);
      Object[] neighborList = neighbors.neighborList; // Neighbors of currVertex
      int[] weightList = neighbors.weightList;
      for (int j = 0; j < neighborList.length; j++) {
        Object currNeighbor = neighborList[j];
        int currWeight = weightList[j];
        Edge edge = new Edge(currVertex, currNeighbor, currWeight);
        allEdges.enqueue(edge);
      }
    }

    // Sort all the edges of g
    quickSort(allEdges);

    // Hash the vertices
    HashTableChained vertices = new HashTableChained(allVertices.length);
    int vertexRank = 0; // Needed for mapping vertices to unique integers
    for (int i = 0; i < allVertices.length; i++) {
      Object key = allVertices[i];
      Vertex value = new Vertex(key, vertexRank);
      vertexRank ++;
      vertices.insert(key, value);
    }

    DisjointSets connections = new DisjointSets(allVertices.length);
    while (!allEdges.isEmpty()) {
      try {
        Edge currEdge = (Edge) allEdges.dequeue(); // Grab smallest edge
        Object v1 = currEdge.getV1();
        Object v2 = currEdge.getV2();
        int weight = currEdge.getWeight();
        Vertex hashedv1 = (Vertex) vertices.find(v1).value();
        Vertex hashedv2 = (Vertex) vertices.find(v2).value();
        Entry entry1 = vertices.find(v1);
        Entry entry2 = vertices.find(v2);
        int unique1 = hashedv1.getRank(); // Grab the unique ints of vertices
        int unique2 = hashedv2.getRank();
        int root1 = connections.find(unique1);
        int root2 = connections.find(unique2);
        if (root1 != root2) {
          t.addEdge(v1, v2, weight);
          connections.union(root1, root2);
        } else {
          continue;
        }
      }
      catch (QueueEmptyException e) {
        System.err.println("Queue is empty");
      } 
    }
    return t;

  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  A helper function used in the quick sort algorithm.
   *  @param qIn is a LinkedQueue of Edge objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  private static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
    while (!qIn.isEmpty()) {
      try {
        Edge current = (Edge) qIn.dequeue();
        if (current.compareTo(pivot) < 0) {
          qSmall.enqueue(current);
        } else if (current.compareTo(pivot) > 0) {
          qLarge.enqueue(current);
        } else {
          qEquals.enqueue(current);
        }
      } catch (QueueEmptyException e) {
        System.err.println(e);
      }
    }
  }

  /**
  *  A helper function that returns a random number between min and max,
  *  inclusive.
  *  @param min Minimum value
  *  @param max Maximum value
  *  @return Integer between min and max, inclusive.
  **/
  private static int randInt(int min, int max) {
    Random rand = new Random();
    int randNum = rand.nextInt((max - min) + 1) + min;
    return randNum;
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort. In the 
   *  context of Kruskal's algorithm, it is used to sort the edges of a
   *  weighted, undirected graph by weight.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  private static void quickSort(LinkedQueue q) {
    if (q.size() <= 1) {
      return;
    } else {
      int random = randInt(1, q.size());
      Comparable pivot = (Comparable) q.nth(random);
      LinkedQueue qSmall = new LinkedQueue();
      LinkedQueue qEquals = new LinkedQueue();
      LinkedQueue qLarge = new LinkedQueue();
      partition(q, pivot, qSmall, qEquals, qLarge);
      quickSort(qSmall);
      quickSort(qLarge);
      q.append(qSmall);
      q.append(qEquals);
      q.append(qLarge);
    }
  }

}
