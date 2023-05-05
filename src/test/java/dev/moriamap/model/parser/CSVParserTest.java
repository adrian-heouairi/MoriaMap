package dev.moriamap.model.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

class CSVParserTest {
  @Test
  void extractLinesThrowsExceptionWhenArgumentIsNull() {
    assertThrows(IllegalArgumentException.class, () -> CSVParser.extractLines(null));
  }

  @Test
  void extractedInconsistentDataThrowsException() {
    assertThrows(
        InconsistentCSVException.class,
        () -> {
          InputStream resource = CSVParserTest.class.getResourceAsStream("/InconsistentCSV.csv");
          CSVParser.extractLines(resource);
        });
  }

  @Test
  void parserExtractsExceptedNumberOfLines() throws InconsistentCSVException {
    InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data.csv");
    List<List<String>> lines = CSVParser.extractLines(resource);
    assertEquals(27, lines.size());
  }

  @Test
  void parseLineThrowsExceptionIfDelimiterIsNull() {
    assertThrows(IllegalArgumentException.class, () -> CSVParser.parseCSVLine("1;1", null));
  }

  @Test
  void parseLineThrowsExceptionIfDelimiterIsEmptyString() {
    assertThrows(IllegalArgumentException.class, () -> CSVParser.parseCSVLine("1;1", ""));
  }

  @Test
  void parseLineThrowsExceptionIfLineIsNull() {
    assertThrows(IllegalArgumentException.class, () -> CSVParser.parseCSVLine(null, ""));
  }

  @Test
  void parseLineThrowsExceptionIfLineIsEmpty() {
    assertThrows(InconsistentCSVException.class, () -> CSVParser.parseCSVLine("", ";"));
  }
}
