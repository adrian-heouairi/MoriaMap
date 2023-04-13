package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
		assertDoesNotThrow(
				  () -> PrettyPrinter.printEdgePath(tn, new ArrayList<>() )
						  );
	}

	@Test
	void nonEmptyListTest() {
		Stop start = tn.getStopByName( "Lourmel" );
		Stop target = tn.getStopByName( "Hoche" );
		try {
			Map<Vertex, Edge> dfs = tn.depthFirstSearch( start );
			List<Edge> path = Graph.getRouteFromTraversal( dfs, start, target );
			assertDoesNotThrow(
					() -> PrettyPrinter.printEdgePath(tn, path ));
		} catch(Exception e) {
			System.out.println( "An issue occurred during the path finding, please check your inputs and repeat" );
			e.printStackTrace();
		}

	}

}
