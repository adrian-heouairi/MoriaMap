package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class PLAN0QueryTest {

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
				  () -> new PLAN0Query( null, null, "Hoche" )
					);
	}

	@Test void targetStopNullTest() {
		assertThrows(
				  IllegalArgumentException.class,
				  () -> new PLAN0Query( null, "Lourmet", null )
					);
	}

	@Test void startingStopNotFoundTest() {
		PLAN0Query query = new PLAN0Query( null, "Java", "Hoche" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void targetStopNotFoundTest() {
		PLAN0Query query = new PLAN0Query( null, "Lourmel", "Ocaml" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void sameStartAndTargetStopsTest() {
		Query query = new PLAN0Query( null, "Lourmel", "Lourmel" );
		assertThrows( QueryFailureException.class,
					  () -> query.run(tn));
	}

	@Test void noProblemsFoundTest() {
		PLAN0Query query = new PLAN0Query( null, "Lourmel", "Hoche" );
		assertDoesNotThrow(
				  () -> query.execute( tn )
						  );
		assertDoesNotThrow( () -> {
			query.run( tn );
		});
	}

}
