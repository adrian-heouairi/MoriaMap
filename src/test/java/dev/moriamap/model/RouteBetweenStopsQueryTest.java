package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class RouteBetweenStopsQueryTest {

	TransportNetwork tn;
	{
		try {
			tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
		} catch (InconsistentCSVException e) {
			throw new RuntimeException(e);
		}
	}

	@Test void startingStopNullTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new RouteBetweenStopsQuery( null, null, "Hoche" )
					);
	}

	@Test void targetStopNullTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new RouteBetweenStopsQuery( null, "Lourmet", null )
					);
	}

	@Test void startingStopNotFoundTest() {
		RouteBetweenStopsQuery query = new RouteBetweenStopsQuery( null, "Java", "Hoche" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void targetStopNotFoundTest() {
		RouteBetweenStopsQuery query = new RouteBetweenStopsQuery( null, "Lourmel", "Ocaml" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void sameStartAndTargetStopsTest() {
		Query query = new RouteBetweenStopsQuery( null, "Lourmel", "Lourmel" );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void noProblemsFoundTest() {
		RouteBetweenStopsQuery query = new RouteBetweenStopsQuery( null, "Lourmel", "Reuilly - Diderot" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertDoesNotThrow( () -> {
			query.run( tn );
		});
	}

}
