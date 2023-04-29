package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.InputStream;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class PLAN2QueryTest {
	private final TransportNetwork tn;
	{
		try {
			tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
			InputStream timetablesInputStream = PLAN2QueryTest.class.getResourceAsStream( "/test_timetables.csv" );
			DepartureParser.addDeparturesTo(tn, timetablesInputStream );
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	private final TransportNetwork brokenTN;
	{
		try {
			brokenTN = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	@Test void startingPointIsAStop() {
		// Coordinates of Lourmel

		PLAN2Query query = new PLAN2Query( null, tn.getStopByName( "Lourmel" ),
										   GeographicVertex.at( 0,0 ),
										   RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}

	@Test void destinationPointIsAStop() {
		// Coordinates of Lourmel
		PLAN2Query query = new PLAN2Query( null, GeographicVertex.at( 0,0 ),
										   tn.getStopByName( "Porte de Charenton" ),
										   RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}

	@Test void brokenTNDoesNotThrow() {
		PLAN2Query query = new PLAN2Query( null, GeographicVertex.at( 0,0 ),
										   GeographicVertex.at( 1,1 ),
										   RouteOptimization.TIME, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(brokenTN));
	}

	@Test void betweenTwoGVsSucceeds() {
		PLAN2Query query = new PLAN2Query( null, GeographicVertex.at( 0,0 ),
										   GeographicVertex.at( 1,1 ),
										   RouteOptimization.TIME, LocalTime.MIN);
		assertDoesNotThrow(() -> query.run(tn));
	}
}
