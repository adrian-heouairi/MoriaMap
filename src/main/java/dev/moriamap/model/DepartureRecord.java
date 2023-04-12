package dev.moriamap.model;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;

/**
 * Holds the data that represents the departure time of a transport from the
 * starting terminus of a certain variant in a line.
 */
public record DepartureRecord(
  String lineName,
  String terminusName,
  LocalTime departureTime,
  String variantName
) {
    
    /**
     * The size of a String tuple corresponding to a DepartureRecord.
     */
    public static final int VALID_TUPLE_SIZE = 4;

    /**
     * The index of the Line name value in a String tuple corresponding to a
     * DepartureRecord.
     */
    public static final int LINE_NAME_TUPLE_INDEX = 0;

    /**
     * The index of the terminus name value in a String tuple corresponding to a
     * DepartureRecord.
     */
    public static final int TERMINUS_NAME_TUPLE_INDEX = 1;

    /**
     * The index of the departure time value in a String tuple corresponding to
     * a DepartureRecord.
     */
    public static final int DEPARTURE_TIME_TUPLE_INDEX = 2;

    /**
     * The index of the Variant name value in a String tuple corresponding to a
     * DepartureRecord.
     */
    public static final int VARIANT_NAME_TUPLE_INDEX = 3;

    /**
     * Record constructor specifying Line name, terminus name, time and Variant
     * name.
     * @param lineName the name of the Line to which belongs the given Variant
     * @param terminusName the name of the starting terminus
     * @param departureTime the departure time of the transport
     * @param variantName the name of the Variant
     * @throws NullPointerException if one or more of the arguments are null
     */
    public DepartureRecord {
        Objects.requireNonNull(lineName);
        Objects.requireNonNull(terminusName);
        Objects.requireNonNull(departureTime);
        Objects.requireNonNull(variantName);
    }

    /**
     * {@return a DepartureRecord corresponding to the data in the given tuple}
     * Throws DateTimeParseException if the departure time value cannot be
     * parsed.
     * @param tuple data as a String tuple
     * @throws NullPointerException if tuple is null
     * @throws IllegalArgumentException if the size of tuple is different than
     *                                  VALID_TUPLE_SIZE
     */
    public static DepartureRecord fromTuple(List<String> tuple) {
        Objects.requireNonNull(tuple);
        if (tuple.size() != VALID_TUPLE_SIZE) {
            throw new IllegalArgumentException("Invalid tuple size");
        }

        String time = tuple.get(DEPARTURE_TIME_TUPLE_INDEX);
        String[] timeArray = time.split(":");
        String timeCorrectFormat = String.format("%02d",Integer.parseInt(timeArray[0])) + ":" + timeArray[1];

        return new DepartureRecord(
          tuple.get(LINE_NAME_TUPLE_INDEX),
          tuple.get(TERMINUS_NAME_TUPLE_INDEX),
          LocalTime.parse(timeCorrectFormat) ,
          tuple.get(VARIANT_NAME_TUPLE_INDEX)
        );
    }

    /**
     * Returns a list of DepartureRecords corresponding to the data given as
     * a list of String tuples. Throws DateTimeParseException if the departure
     * time value cannot be parsed.
     * @param tuples a list of tuples corresponding to DepartureRecords as
     *        String tuples
     * @return the corresponding list of DepartureRecords
     * @throws NullPointerException if tuples or one of the tuples is null
     * @throws IllegalArgumentException if the size of one or more of the tuples
     *                                  in tuples is not VALID_TUPLE_SIZE
     */
    public static List<DepartureRecord> fromTuples(List<List<String>> tuples) {
        Objects.requireNonNull(tuples);
        var records = new ArrayList<DepartureRecord>();
        for (List<String> tuple: tuples) {
            records.add(DepartureRecord.fromTuple(tuple));
        }
        return records;
    }

}
