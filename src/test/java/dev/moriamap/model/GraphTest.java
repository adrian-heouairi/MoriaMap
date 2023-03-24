package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

class GraphTest {
    class DummyGraph extends Graph {}
    class DummyVertex extends Vertex {}
    class DummyEdge extends Edge {
        public DummyEdge() { super(new DummyVertex(), new DummyVertex()); }
        public DummyEdge(Vertex from, Vertex to) { super(from, to); }
        public double getWeight() { return 0; }
    }

    @Test void verticesOfNewGraphIsEmptyList() {
        Graph sut = new DummyGraph();
        assertTrue(sut.getVertices().isEmpty());
    }

    @Test void edgesOfNewGraphIsEmptyList() {
        Graph sut = new DummyGraph();
        assertTrue(sut.getEdges().isEmpty());
    }

    @Test void addNullVertexThrowsException() {
        Graph sut = new DummyGraph();
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.addVertex(null)
        );
    }

    @Test void verticesOfGraphWithOneVertexReturnsVertex() {
        Graph sut = new DummyGraph();
        Vertex vertex = new DummyVertex();
        sut.addVertex(vertex);
        assertTrue(sut.getVertices().contains(vertex));
    }

    @Test void addNullEdgeThrowsException() {
        Graph sut = new DummyGraph();
        assertThrows(IllegalArgumentException.class, () -> sut.addEdge(null));
    }

    @Test void addEdgeWithTwoNewVerticesAddsTwoNewVertices() {
        Graph sut = new DummyGraph();
        Edge edge = new DummyEdge();
        sut.addEdge(edge);
        assertEquals(2, sut.getVertices().size());
    }

    @Test void getOutgoindEdgesOfNullVertexThrowsIllegalArgumentException() {
        Graph sut = new DummyGraph();
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.getOutgoingEdgesOf(null)
        );
    }

    @Test void getOutgoingEdgesOfAbsentVertexThrowsIllegalArgumentException() {
        Graph sut = new DummyGraph();
        Vertex dummy = new DummyVertex();
        assertThrows(
            NoSuchElementException.class,
            () -> sut.getOutgoingEdgesOf(dummy)
        );
    }

    @Test void getOutgoingEdgesOfVertexWithoutOutgoingEdgesIsEmptyList() {
        Graph sut = new DummyGraph();
        Vertex from = new DummyVertex();
        sut.addVertex(from);
        assertTrue(sut.getOutgoingEdgesOf(from).isEmpty());
    }

    @Test void getOutgoingEdgesOfVertexWithOneOutgoingEdgesReturnsSuchList() {
        Graph sut = new DummyGraph();
        Vertex from = new DummyVertex();
        Edge edge = new DummyEdge(from, new DummyVertex());
        sut.addEdge(edge);
        List<Edge> outgoingEdges = new ArrayList<>();
        outgoingEdges.add(edge);
        assertEquals(outgoingEdges, sut.getOutgoingEdgesOf(from));
    }
}
