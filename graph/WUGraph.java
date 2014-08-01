/* WUGraph.java */

package graph;
import dict.*;
import list.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

    private int numVertices;
    private int numEdges;
    private DList vertices;
    private HashTableChained hashVertices;
    private HashTableChained hashEdges;
    //"edges" is a hashtable with Entries: key = vertex, value = DList of adjacent edges
    //value = DList is to ensure that even with collision, our code works

    /**
    * WUGraph() constructs a graph having no vertices or edges.
    *
    * Running time:  O(1).
    */
    public WUGraph() {
        numEdges = 0;
        numVertices = 0;
        vertices = new DList();
        hashEdges = new HashTableChained();
        hashVertices = new HashTableChained();
    }

    /**
    * vertexCount() returns the number of vertices in the graph.
    *
    * Running time:  O(1).
    */
    public int vertexCount() {
        return numVertices;
    }

    /**
    * edgeCount() returns the total number of edges in the graph.
    *
    * Running time:  O(1).
    */
    public int edgeCount() {
        return numEdges;
    }

    /**
    * getVertices() returns an array containing all the objects that serve
    * as vertices of the graph.  The array's length is exactly equal to the
    * number of vertices.  If the graph has no vertices, the array has length
    * zero.
    *
    * (NOTE:  Do not return any internal data structure you use to represent
    * vertices!  Return only the same objects that were provided by the
    * calling application in calls to addVertex().)
    *
    * Running time:  O(|V|).
    */
    public Object[] getVertices() {
        Object[] verticesArr = new Object[numVertices];
        //verticesArr is what we will be returning at end of method
        DListNode d = vertices.front();
        int index = 0;
        while (d != null) {
            verticesArr[index] = ((VSentinel)d.item).vertex;
            index++;
            d = vertices.next(d);
        }
        return verticesArr;
    }

    /**
    * addVertex() adds a vertex (with no incident edges) to the graph.
    * The vertex's "name" is the object provided as the parameter "vertex".
    * If this object is already a vertex of the graph, the graph is unchanged.
    *
    * Running time:  O(1).
    */
    public void addVertex(Object vertex) {
        if (isVertex(vertex)) {
            return;
        }
        vertices.insertBack(new VSentinel(vertex, new DList()));
        //Creates a new DListNode in vertices pointing to a VSentinel
        //with an empty DList for the VSentinel.edges
        DListNode vertexNode = vertices.back();
        hashVertices.insert(vertex, vertexNode);
        numVertices++;
    }

    /**
    * removeVertex() removes a vertex from the graph.  All edges incident on the
    * deleted vertex are removed as well.  If the parameter "vertex" does not
    * represent a vertex of the graph, the graph is unchanged.
    *
    * Running time:  O(d), where d is the degree of "vertex".
    */
    public void removeVertex(Object vertex) {
        if (!isVertex(vertex)) {
            return;
        }
        DListNode vertexNode = (DListNode)hashVertices.find(vertex).value();
        DList vertexVSentinelEdges = ((VSentinel)vertexNode.item).edges;
        DListNode currEdgeNode = vertexVSentinelEdges.front();
        while (currEdgeNode != null) {
            Edge currEdge = (Edge)currEdgeNode.item;
            Object otherVertex = currEdge.otherVertex;
            DListNode otherNode = currEdge.otherDListNode;
            ((VSentinel)((DListNode)hashVertices.find(otherVertex).value()).item).edges.remove(otherNode);
            VertexPair vp = new VertexPair(vertex, otherVertex);
            hashEdges.remove(vp);
            numEdges--;
            currEdgeNode = vertexVSentinelEdges.next(currEdgeNode);
        }
        vertices.remove(vertexNode);
        hashVertices.remove(vertex);
        numVertices--;
    }

    /**
    * isVertex() returns true if the parameter "vertex" represents a vertex of
    * the graph.
    *
    * Running time:  O(1).
    */
    public boolean isVertex(Object vertex) {
        if (hashVertices.find(vertex) != null) {
            return true;
        }
        return false;
    }

    /**
    * degree() returns the degree of a vertex.  Self-edges add only one to the
    * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
    * of the graph, zero is returned.
    *
    * Running time:  O(1).
    */
    public int degree(Object vertex) {
        if (isVertex(vertex)) {
            return ((VSentinel)((DListNode)hashVertices.find(vertex).value()).item).edges.length();
        }
        return 0;
    }

    /**
    * getNeighbors() returns a new Neighbors object referencing two arrays.  The
    * Neighbors.neighborList array contains each object that is connected to the
    * input object by an edge.  The Neighbors.weightList array contains the
    * weights of the corresponding edges.  The length of both arrays is equal to
    * the number of edges incident on the input vertex.  If the vertex has
    * degree zero, or if the parameter "vertex" does not represent a vertex of
    * the graph, null is returned (instead of a Neighbors object).
    *
    * The returned Neighbors object, and the two arrays, are both newly created.
    * No previously existing Neighbors object or array is changed.
    *
    * (NOTE:  In the neighborList array, do not return any internal data
    * structure you use to represent vertices!  Return only the same objects
    * that were provided by the calling application in calls to addVertex().)
    *
    * Running time:  O(d), where d is the degree of "vertex".
    */
    public Neighbors getNeighbors(Object vertex) {
        int deg = degree(vertex);
        if (deg == 0 || !isVertex(vertex)) {
            return null;
        }
        Object[] neighborList = new Object[deg];
        int[] weightList = new int[deg];
        DList neighbors = ((VSentinel)((DListNode)hashVertices.find(vertex).value()).item).edges;
        DListNode currNeighbor = neighbors.front();
        int index = 0;
        while (currNeighbor != null) {
            Edge edge = (Edge)currNeighbor.item;
            Object otherV = edge.otherVertex;
            neighborList[index] = otherV;
            VertexPair vp = new VertexPair(vertex, otherV);
            weightList[index] = ((Edge)hashEdges.find(vp).value()).weight;
            index++;
            currNeighbor = neighbors.next(currNeighbor);
        }
        Neighbors n = new Neighbors();
        n.neighborList = neighborList;
        n.weightList = weightList;
        return n;
    }

    /**
    * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
    * u and v does not represent a vertex of the graph, the graph is unchanged.
    * The edge is assigned a weight of "weight".  If the graph already contains
    * edge (u, v), the weight is updated to reflect the new value in only the
    * hashEdges field. Weights are only trusted from hashEdges.  Self-edges
    * (where u == v) are allowed.
    *
    * Running time:  O(1).
    */
    public void addEdge(Object u, Object v, int weight) {
        VertexPair vp = new VertexPair(u, v);
        if (isEdge(u, v)) {
            //if updating the weight of an edge, only update hashEdges
            //thus, hashEdges is the most up-to-date, do not trust
            //weights from Edge objects not referenced by hashEdges
            ((Edge) hashEdges.find(vp).value()).weight = weight;
            return;
        }
        DList uEdges = ((VSentinel) ((DListNode) hashVertices.find(u).value()).item).edges;
        Edge uEdge = new Edge(u, v, null, null, weight);
        uEdges.insertBack(uEdge);
        DListNode uNode = uEdges.back();
        if (!u.equals(v)) {
            DList vEdges = ((VSentinel) ((DListNode) hashVertices.find(v).value()).item).edges;
            Edge vEdge = new Edge(v, u, null, null, weight);
            vEdges.insertBack(vEdge);
            DListNode vNode = vEdges.back();
            vEdge.thisDListNode = vNode;
            vEdge.otherDListNode = uNode;
            uEdge.thisDListNode = uNode;
            uEdge.otherDListNode = vNode;
        } else {
            uEdge.thisDListNode = uNode;
            uEdge.otherDListNode = uNode;
        }

        hashEdges.insert(vp, uEdge);
        //hashEdges only has one copy of this edge, NOT two
        numEdges++;
    }

    /**
    * removeEdge() removes an edge (u, v) from the graph.  If either of the
    * parameters u and v does not represent a vertex of the graph, the graph
    * is unchanged.  If (u, v) is not an edge of the graph, the graph is
    * unchanged.
    *
    * Running time:  O(1).
    */
    public void removeEdge(Object u, Object v) {
        if (!isVertex(u) || !isVertex(v) || !isEdge(u, v)) {
            return;
        }
        VertexPair vp = new VertexPair(u, v);
        Edge edge = (Edge)hashEdges.find(vp).value();
        if (!u.equals(v)) {
            DList otherEdges = ((VSentinel)((DListNode)hashVertices.find(edge.otherVertex).value()).item).edges;
            otherEdges.remove(edge.otherDListNode);
        }
        DList thisEdges = ((VSentinel)((DListNode)hashVertices.find(edge.thisVertex).value()).item).edges;
        thisEdges.remove(edge.thisDListNode);
        hashEdges.remove(vp);
        numEdges--;
    }

    /**
    * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
    * if (u, v) is not an edge (including the case where either of the
    * parameters u and v does not represent a vertex of the graph).
    *
    * Running time:  O(1).
    */
    public boolean isEdge(Object u, Object v) {
        if (hashEdges.find(new VertexPair(u,v)) != null) {
            return true;
        }
        return false;
    }

    /**
    * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
    * an edge (including the case where either of the parameters u and v does
    * not represent a vertex of the graph).
    *
    * (NOTE:  A well-behaved application should try to avoid calling this
    * method for an edge that is not in the graph, and should certainly not
    * treat the result as if it actually represents an edge with weight zero.
    * However, some sort of default response is necessary for missing edges,
    * so we return zero.  An exception would be more appropriate, but also more
    * annoying.)
    *
    * Running time:  O(1).
    */
    public int weight(Object u, Object v) {
        if (!isEdge(u, v)) {
            return 0;
        }
        return ((Edge)hashEdges.find(new VertexPair(u, v)).value()).weight;
    }

}
