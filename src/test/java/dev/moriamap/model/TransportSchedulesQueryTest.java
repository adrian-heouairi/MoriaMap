package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class TransportSchedulesQueryTest {

    TransportNetwork tn;
    {
        try {
            tn = TransportNetworkParser.generateFrom(CSVParserTest.class.getResourceAsStream("/test_map_data.csv"));
        } catch (InconsistentCSVException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void stopNullTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TransportSchedulesQuery( null, null)
        );
    }

    @Test void stopNotFoundTest() {
        TransportSchedulesQuery query = new TransportSchedulesQuery( null, "Java");
        assertDoesNotThrow(
                () -> query.execute( tn )
        );
        assertThrows( QueryFailureException.class,
                () -> query.run(tn));
    }

    @Test void noProblemsFoundTest() {
        TransportSchedulesQuery query = new TransportSchedulesQuery( null, "Lourmel");
        assertDoesNotThrow(
                () -> query.execute( tn )
        );
        assertDoesNotThrow( () -> {
            query.run( tn );
        });
    }

}
