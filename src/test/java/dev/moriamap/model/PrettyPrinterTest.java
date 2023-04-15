package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class PrettyPrinterTest {

	TransportNetwork tn;
	{
		try {
			tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void emptyListTest() {
		assertEquals("",PrettyPrinter.printTransportSegmentPath(tn, new ArrayList<>() ));
	}

	@Test
	void nonEmptyListGiveNonEmptyStringTest() {
		Stop start = tn.getStopByName( "Lourmel" );
		Stop target = tn.getStopByName( "Hoche" );
		Map<Vertex, Edge> dfs = tn.depthFirstSearch( start );
		List<Edge> path = Graph.getRouteFromTraversal( dfs, start, target );
		assertNotEquals("", PrettyPrinter.printTransportSegmentPath(tn, path ));
	}

}
