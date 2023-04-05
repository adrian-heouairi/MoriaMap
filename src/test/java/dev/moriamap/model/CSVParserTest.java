package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest {
    @Test void extractLinesThrowsExceptionWhenArgumentIsNull() {
        assertThrows(IllegalArgumentException.class,() ->{
            CSVParser.extractLines(null);
        });
    }
    @Test void extractedInconsistentDataThrowsException() {
        assertThrows(InconsistentCSVLinesException.class,() ->{
            InputStream resouce = CSVParserTest.class.getResourceAsStream("/InconsistentCSV.csv");
            CSVParser.extractLines(resouce);
        });
    }

    @Test void parserExtractsExceptedNumberOfLines() throws InconsistentCSVLinesException, IOException {
        InputStream resouce = CSVParserTest.class.getResourceAsStream("/map_data.csv");
        List<List<String>> lines = CSVParser.extractLines(resouce);
        assertEquals(1770,lines.size());
    }

    @Test void parseLineThrowsExceptionIfDelimiterIsNull() {
        assertThrows(IllegalArgumentException.class,()->CSVParser.parseCSVLine("1;1",null));
    }

    @Test void parseLineThrowsExceptionIfDelimiterIsEmptyString() {
        assertThrows(IllegalArgumentException.class,()->CSVParser.parseCSVLine("1;1",""));
    }

    @Test void parseLineThrowsExceptionIfLineIsNull() {
        assertThrows(IllegalArgumentException.class,()->CSVParser.parseCSVLine(null,""));
    }

    @Test void parseLineThrowsExceptionIfLineIsEmpty() {
        assertThrows(IllegalArgumentException.class,()->CSVParser.parseCSVLine("",";"));
    }
}
