package dev.moriamap.model.parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

class DepartureRecordTest {

    @Test void departureRecordWithNullLineNameThrowsException() {
        var dep = LocalTime.now();
        assertThrows(
          NullPointerException.class,
          () -> new DepartureRecord(null, "", dep, "")
        );
    }

    @Test void departureRecordWithNullTerminusNameThrowsException() {
        var dep = LocalTime.now();
        assertThrows(
          NullPointerException.class,
          () -> new DepartureRecord("", null, dep, "")
        );
    }

    @Test void departureRecordWithNullDepartureTimeThrowsException() {
        assertThrows(
          NullPointerException.class,
          () -> new DepartureRecord("", "", null, "")
        );
    }

    @Test void departureRecordWithNullVariantNameThrowsException() {
        var dep = LocalTime.now();
        assertThrows(
          NullPointerException.class,
          () -> new DepartureRecord("", "", dep, null)
        );
    }

    @Test void validTupleSizeEquals4() {
        assertEquals(4, DepartureRecord.VALID_TUPLE_SIZE);
    }

    @Test void lineNameValueTupleIndexEquals0() {
        assertEquals(0, DepartureRecord.LINE_NAME_TUPLE_INDEX);
    }

    @Test void terminusNameValueTupleIndexEquals1() {
        assertEquals(1, DepartureRecord.TERMINUS_NAME_TUPLE_INDEX);
    }

    @Test void departureTimeValueTupleIndexEquals2() {
        assertEquals(2, DepartureRecord.DEPARTURE_TIME_TUPLE_INDEX);
    }

    @Test void variantNameValueTupleIndexEquals3() {
        assertEquals(3, DepartureRecord.VARIANT_NAME_TUPLE_INDEX);
    }

    @Test void departureRecordFromNullTupleThrowsException() {
        assertThrows(
          NullPointerException.class,
          () -> DepartureRecord.fromTuple(null)
        );
    }

    @Test void departureRecordFromIllegallySizedTupleThrowsException() {
        List<String> empty = Collections.emptyList();
        assertThrows(
          IllegalArgumentException.class,
          () -> DepartureRecord.fromTuple(empty)
        );
    }

    @Test void departureRecordFromUnparsableDepartureTimeThrowsException() {
        List<String> tuple = Arrays.asList("", "", "114:25", "");
        assertThrows(
          DateTimeParseException.class,
          () -> DepartureRecord.fromTuple(tuple)
        );
    }

    @Test void departureRecordFromStringTupleReturnsCorrespondingRecord() {
        var sut = DepartureRecord.fromTuple(
          Arrays.asList("l", "t", LocalTime.NOON.toString(), "v")
        );
        assertEquals("l", sut.lineName());
        assertEquals("t", sut.terminusName());
        assertEquals(LocalTime.NOON, sut.departureTime());
        assertEquals("v", sut.variantName());
    }

    @Test void departureRecordsFromNullTuplesThrowsException() {
        assertThrows(
          NullPointerException.class,
          () -> DepartureRecord.fromTuples(null)
        );
    }

    @Test void departureRecordsFromTuplesWithNullThrowsException() {
        List<List<String>> tuples = Arrays.asList(null, new ArrayList<>());
        assertThrows(
          NullPointerException.class,
          () -> DepartureRecord.fromTuples(tuples)
        );
    }

    @Test void departureRecordsFromTuplesWithInvalidSizeThrowsException() {
        List<List<String>> tuples = Arrays.asList(new ArrayList<>());
        assertThrows(
          IllegalArgumentException.class,
          () -> DepartureRecord.fromTuples(tuples)
        );
    }

    @Test void depRecordsFromTuplesWithInvalidDepartureTimeThrowsException() {
        List<List<String>> tuples = Arrays.asList(
          Arrays.asList("", "", "66:42", "")
        );
        assertThrows(
          DateTimeParseException.class,
          () -> DepartureRecord.fromTuples(tuples)
        );
    }
}
