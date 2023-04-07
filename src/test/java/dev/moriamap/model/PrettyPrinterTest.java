package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PrettyPrinterTest {

	static class DummyEdge extends Edge {
		public DummyEdge(Vertex from, Vertex to) { super(from, to); }
		public double getWeight() { return 0; }
	}

	@Test
	void emptyListTest() {
		assertDoesNotThrow(
				  () -> PrettyPrinter.printEdgePath( new ArrayList<>() )
						  );
	}

	@Test
	void nonEmptyListTest() {
		List<Edge> edges = new ArrayList<>();
		Stop stop1 = Stop.from( "Stop1", GeographicPosition.NORTH_POLE );
		Stop stop2 = Stop.from( "Stop2", GeographicPosition.SOUTH_POLE );
		edges.add(new DummyEdge(stop1, stop2));
		assertDoesNotThrow(
				  () -> PrettyPrinter.printEdgePath( edges )
						  );
	}

}
