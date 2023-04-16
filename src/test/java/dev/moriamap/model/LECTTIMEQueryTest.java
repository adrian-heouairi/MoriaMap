package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


    @TestInstance( TestInstance.Lifecycle.PER_CLASS ) 
    class LECTTIMEQueryTest {

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
                    () -> new LECTTIMEQuery( null)
            );
        }

        @Test void stopNotFoundTest() {
            LECTTIMEQuery query = new LECTTIMEQuery( "Java");
            assertDoesNotThrow(
                    () -> query.execute( tn )
            );
            assertThrows( QueryFailureException.class,
                    () -> query.run(tn));
        }
        
        @Test void noProblemsFoundTest() {
            LECTTIMEQuery query = new LECTTIMEQuery( "Lourmel");
            assertDoesNotThrow(
                    () -> query.execute( tn )
            );
            assertDoesNotThrow( () -> {
                query.run( tn );
            });
        }

        @Test void stopNameGetterTest() {
            LECTTIMEQuery query = new LECTTIMEQuery( "Lourmel" );
            assertEquals( "Lourmel", query.stopName() );
        }
    }


