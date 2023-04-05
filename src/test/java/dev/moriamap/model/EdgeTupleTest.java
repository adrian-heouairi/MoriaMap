package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EdgeTupleTest {

    @Test void returnedListHasExpectedSize() {
        List<List<String>> lines = new ArrayList<>();
        try {

            InputStream resouce = CSVParserTest.class.getResourceAsStream("/map_data.csv");
            lines = CSVParser.extractLines(resouce);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EdgeTuple> liste  = EdgeTuple.fromTuples(lines);
        assertEquals(1770,liste.size());
    }
}
