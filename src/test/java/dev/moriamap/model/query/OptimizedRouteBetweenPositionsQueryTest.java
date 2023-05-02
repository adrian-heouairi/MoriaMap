package dev.moriamap.model.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dev.moriamap.model.network.GeographicVertex;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.traversal.RouteOptimization;
import dev.moriamap.model.parser.DepartureParser;
import dev.moriamap.model.parser.InconsistentCSVException;
import dev.moriamap.model.parser.TransportNetworkParser;

import java.io.InputStream;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class OptimizedRouteBetweenPositionsQueryTest {
	private final TransportNetwork tn;
	{
		try {
			tn = TransportNetworkParser.generateFrom(OptimizedRouteBetweenPositionsQueryTest.class.getResourceAsStream("/test_map_data.csv"));
			InputStream timetablesInputStream = OptimizedRouteBetweenPositionsQueryTest.class.getResourceAsStream( "/test_timetables.csv" );
			DepartureParser.addDeparturesTo(tn, timetablesInputStream );
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	private final TransportNetwork brokenTN;
	{
		try {
			brokenTN = TransportNetworkParser.generateFrom(OptimizedRouteBetweenPositionsQueryTest.class.getResourceAsStream("/test_map_data.csv"));
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	@Test void startingPointIsAStop() {
		// Coordinates of Lourmel

		OptimizedRouteBetweenPositionsQuery query = new OptimizedRouteBetweenPositionsQuery( null, tn.getStopByName( "Lourmel" ),
																							 GeographicVertex.at( 0,0 ),
																							 RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}

	@Test void destinationPointIsAStop() {
		// Coordinates of Lourmel
		OptimizedRouteBetweenPositionsQuery query = new OptimizedRouteBetweenPositionsQuery( null, GeographicVertex.at( 0, 0 ),
																							 tn.getStopByName( "Porte de Charenton" ),
																							 RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}

	@Test void brokenTNDoesNotThrow() {
		OptimizedRouteBetweenPositionsQuery query = new OptimizedRouteBetweenPositionsQuery( null, GeographicVertex.at( 0, 0 ),
																							 GeographicVertex.at( 1,1 ),
																							 RouteOptimization.TIME, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(brokenTN));
	}

	@Test void betweenTwoGVsSucceeds() {
		OptimizedRouteBetweenPositionsQuery query = new OptimizedRouteBetweenPositionsQuery( null, GeographicVertex.at( 0, 0 ),
																							 GeographicVertex.at( 1,1 ),
																							 RouteOptimization.TIME, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}
}
