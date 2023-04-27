package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.InputStream;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class PLAN1QueryTest {


	TransportNetwork tn;
	{
		try {
			tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
			InputStream timetablesInputStream = PLAN1QueryTest.class.getResourceAsStream( "/test_timetables.csv" );
			DepartureParser.addDeparturesTo(tn, timetablesInputStream );
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	TransportNetwork broken_tn;
	{
		try {
			broken_tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	@Test void startingStopNotFoundTest() {
		PLAN1Query query = new PLAN1Query( null, "Java", "Hoche", RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow( () -> query.execute( tn ) );
		assertThrows( QueryFailureException.class, () -> query.run(tn));
	}

	@Test void targetStopNotFoundTest() {
		PLAN1Query query = new PLAN1Query( null, "Lourmel", "Ocaml",RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void sameStartAndTargetStopsTest() {
		Query query = new PLAN1Query( null,
									  "Lourmel",
									  "Lourmel",
									  RouteOptimization.DISTANCE,
									  LocalTime.MIN);
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test
	void DistanceOptimizedTest() {
		PLAN1Query query = new PLAN1Query( null, "Lourmel", "Invalides",RouteOptimization.DISTANCE, LocalTime.MIN);
		assertDoesNotThrow(
				  () -> query.run(tn));
	}

	@Test
	void TimeOptimizedTest() {
		PLAN1Query query = new PLAN1Query( null, "Lourmel", "Invalides",RouteOptimization.TIME, LocalTime.MIN);
		assertDoesNotThrow(
				  () -> query.run(tn));
	}
	@Test
	void DistanceOptimizedThrowExceptionTest() {
		PLAN1Query query = new PLAN1Query( null, "Lourmel", "Invalides",RouteOptimization.DISTANCE, LocalTime.MIN);
		assertThrows( QueryFailureException.class,
				  () -> query.run(broken_tn));
	}

	@Test
	void TimeOptimizedThrowExceptionTest() {
		PLAN1Query query = new PLAN1Query( null, "Lourmel", "Invalides",RouteOptimization.TIME, LocalTime.MIN);
		assertThrows(QueryFailureException.class,
				  () -> query.run(broken_tn));
	}

}
