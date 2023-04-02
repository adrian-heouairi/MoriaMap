package dev.moriamap.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * This record holds datas for an Edge
 * @param fromName name of first stop of the edge
 * @param fromLongitude longitude of startStop of the edge
 * @param fromLatitude latitude of startStop of the edge
 * @param toName name of destinationStop of the edge
 * @param toLongitude longitude of destinationStop of the edge
 * @param toLatitude latitude of destinationStop of the edge
 * @param lineName the name of the line containing this transport edge
 * @param variantName the variant name
 * @param duration the duration form startStop to destinationStop
 * @param distance distance between the two stops of this edge tuple
 */
public record EdgeTuple(String fromName,
                        double fromLongitude,
                        double fromLatitude,
                        String toName,
                        double toLongitude,
                        double toLatitude,
                        String lineName,
                        String variantName,
                        Duration duration,
                        double distance) {

    /**
     * Transforms a list of strings representing a CSV file line to an EdgeTuple.
     * <p>
     *     The format of fields is assumed to be correct.
     * </p>
     * @param fields the field of the given line
     * @return an EdgeTuple represented by fields parameter
     */
    private static EdgeTuple fromCSVLine(List<String> fields) {
        String [] longitudeAndLatitude = fields.get(1).split(", ");
        double lon1 = Double.parseDouble(longitudeAndLatitude[0]);
        double lat1 = Double.parseDouble(longitudeAndLatitude[1]);
        String [] longitudeAndLatitude1 = fields.get(3).split(", ");
        double lon2 = Double.parseDouble(longitudeAndLatitude1[0]);
        double lat2 = Double.parseDouble(longitudeAndLatitude1[1]);
        String lineName = fields.get(4).split(" variant ")[0];
        String variantName = fields.get(4).split(" variant ")[1];
        String [] time = fields.get(5).split(":");
        Duration d = Duration.ofSeconds(Integer.parseInt(time[0].trim()) * 60L + Integer.parseInt(time[1].trim()));

        return new EdgeTuple(fields.get(0),
                lon1,
                lat1,
                fields.get(2),
                lon2,
                lat2,
                lineName,
                variantName,
                d,
                Double.parseDouble(fields.get(6)));

    }

    /**
     * Transforms lines of a CSV file into a list of EdgeTuples.
     * @param lines list representing the lines of a CSV file
     * @return a list of EdgeTuples
     */
    public static List<EdgeTuple> fromTuples(List<List<String>> lines) {
        ArrayList<EdgeTuple> res = new ArrayList<>();
        for ( List<String> line: lines) {
            res.add(fromCSVLine(line));
        }
        return res;
    }
}
