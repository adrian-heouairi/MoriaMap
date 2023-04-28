package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class EdgeTupleTest {

    EdgeRecord e = new EdgeRecord(
            "Olympiades",
            40.5,
            40.5,
            "Saint-Ouen",
            50.6,
            50.6,
            "14",
            "1",
            Duration.ofSeconds(23),
            23.4
    );

    @Test void returnedListHasExpectedSize() {
        List<List<String>> lines = new ArrayList<>();
        try {

            InputStream resouce = CSVParserTest.class.getResourceAsStream("/test_map_data.csv");
            lines = CSVParser.extractLines(resouce);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EdgeRecord> liste  = EdgeRecord.fromTuples(lines);
        assertEquals(27,liste.size());
    }

    @Test void constructorParsingNullFromName() {
        assertThrows( IllegalArgumentException.class,
                () ->{
                    new EdgeRecord(
                            null,
                            40.5,
                            40.5,
                            "Olympiades",
                            50.6,
                            50.6,
                            "12",
                            "1",
                            Duration.ZERO,
                            23.4);
        });
    }

    @Test void constructorParsingNullToName() {
        assertThrows( IllegalArgumentException.class,
                () ->{
                    new EdgeRecord(
                            "Olympiades",
                            40.5,
                            40.5,
                            null,
                            50.6,
                            50.6,
                            "12",
                            "1",
                            Duration.ZERO,
                            23.4);
                });
    }

    @Test void constructorParsingNullLineName() {
        assertThrows( IllegalArgumentException.class,
                () ->{
                    new EdgeRecord(
                            "Olympiades",
                            40.5,
                            40.5,
                            "BNF",
                            50.6,
                            50.6,
                            null,
                            "1",
                            Duration.ZERO,
                            23.4);
                });
    }

    @Test void constructorParsingNullVariantName() {
        assertThrows( IllegalArgumentException.class,
                () ->{
                    new EdgeRecord(
                            "Olympiades",
                            40.5,
                            40.5,
                            "BNF",
                            50.6,
                            50.6,
                            "14",
                            null,
                            Duration.ZERO,
                            23.4);
                });
    }

    @Test void constructorParsingNullDuration() {
        assertThrows( IllegalArgumentException.class,
                () ->
                    new EdgeRecord(
                            "Olympiades",
                            40.5,
                            40.5,
                            "BNF",
                            50.6,
                            50.6,
                            "14",
                            "1",
                            null,
                            23.4));
    }

    @Test void fromNameGetter() {
        assertEquals("Olympiades",e.fromName());
    }

    @Test void toNameGetter() {
        assertEquals("Saint-Ouen", e.toName());
    }

    @Test void distanceGetter() {
        assertEquals(23.4, e.distance());
    }

    @Test void durationGetter() {
        assertEquals(Duration.ofSeconds(23), e.duration());
    }

    @Test void fromLatitudeGetter() {
        assertEquals(40.5,e.fromLatitude());
    }

    @Test void fromLongitudeGetter() {
        assertEquals(40.5,e.fromLongitude());
    }

    @Test void toLatitudeGetter() {
        assertEquals(50.6,e.toLatitude());
    }

    @Test void toLongitudeGetter() {
        assertEquals(50.6,e.toLongitude());
    }

    @Test void lineNameGetter() {
        assertEquals("14",e.lineName());
    }
    @Test void variantNameGetter() {
        assertEquals("1",e.variantName());
    }
}
