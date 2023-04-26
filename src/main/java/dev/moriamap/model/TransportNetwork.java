package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an arbitrary transport network. A TransportNetwork contains
 * Lines. It also contains Stops and TransportSegments which are used for
 * path calculations on the underlying Graph.
 */
public final class TransportNetwork extends Graph {
    private final List<Line> lines;

    private static final String ERR_NULL_ARG_MESSAGE = "Argument cannot be null";

    private TransportNetwork() {
        super();
        this.lines = new ArrayList<>();
    }

    /**
     * {@return a new empty TransportNetwork with no lines, no stops and no transport segments}
     */
    public static TransportNetwork empty() {
        return new TransportNetwork();
    }

    /**
     * {@return the Stop at position gp in the transport network,
     * or null if not found}
     * @param gp the geographic position of the wanted stop
     * @throws IllegalArgumentException if gp is null
     */
    public Stop getStopFromPosition(GeographicPosition gp) {
        if (gp == null) throw new IllegalArgumentException(ERR_NULL_ARG_MESSAGE);
        for (Stop s : this.getStops()) {
            if (s.getGeographicPosition().equals(gp))
                return s;
        }
        return null;
    }

    /**
     * {@return a list containing all the Passages for a given Stop }
     * @param s the Stop for which we want the Passages
     */
    public Passages getPassages(Stop s){
        List<Variant> variantsOfStop = new ArrayList<>();
        for(Variant v : this.getVariants()){
            if(v.getStops().contains(s) && !s.equals(v.getEnd()))
                variantsOfStop.add(v);
        }
        List<TransportSchedule> transportSchedules = new ArrayList<>();
        for(Variant v : variantsOfStop){
            Duration travelTime = v.getTravelTimeTo(s);
            for(LocalTime t : v.getDepartures()){
                TransportSchedule ts = new TransportSchedule(t.plus(travelTime), s, v);
                transportSchedules.add(ts);
            }
        }
        return Passages.of(transportSchedules);
    }

    /**
     * {@return the Stop which has the wanted name in the network,
     * or null if it is not found}
     * @param name the name of the wanted stop
     * @throws IllegalArgumentException if name is null
     */
    public Stop getStopByName(String name) {
        if (name == null) throw new IllegalArgumentException(ERR_NULL_ARG_MESSAGE);
        for (Stop s : this.getStops()) {
            if (s.getName().equals(name))
                return s;
        }
        return null;
    }

    /**
    * Finds the stop in the transport network stop list that has the closest name to the given stop name,
    * as measured by the Levenshtein distance.
    * @param name the name of the stop to find the closest match for
    * @return the stop in the list with the closest name to the given stop name, or null if no match was found
    */
    public Stop getStopByInexactName(String name){
        var stops = this.getStops();
        var min = StopNameFinder.levenshteinDistance(name, stops.get(0).getName());
        var res = stops.get(0);
        for(int i=1;i<stops.size();i++){
            var distance = StopNameFinder.levenshteinDistance(name, stops.get(i).getName());
            if(distance< min){
                min = distance;
                res = stops.get(i);
            }
        }
        // If the closest match is more than three edit operations away, return null
        if(min >= 3) return null;
        return res;
    }

    /**
    * Returns a list of the X nearest stops to the given name, based on their
    * Levenshtein distance.
    * @param name the name to compare to.
    * @param x the number of nearest stops to return.
    * @return a list of the X nearest stop to the given name.
    */
    public List<Stop> getNearestStopsByInexactName(String name, int x){
        Map<Stop, Integer> distances = new HashMap<>();
        var stops = this.getStops();
        for (Stop stop: stops) {
            int distance = StopNameFinder.levenshteinDistance(name, stop.getName());
            distances.put(stop, distance);
        }
        List<Stop> sortedNames = new ArrayList<>(distances.keySet());
        Collections.sort(sortedNames, (name1, name2) -> distances.get(name1) - distances.get(name2));
        List<Stop> nearestNames = new ArrayList<>();
        for (int i = 0; i < Math.min(x, sortedNames.size()); i++) {
            nearestNames.add(sortedNames.get(i));
        }
        return nearestNames;

    }

    /**
     * {@return the lines (e.g. bus, tram) of this network}
     */
    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    /**
     * {@return all the variants of all the lines of this TransportNetwork}
     */
    public List<Variant> getVariants() {
        List<Variant> variants = new ArrayList<>();
        for (Line l : lines) variants.addAll(l.getVariants());
        return variants;
    }

    /**
     * {@return all the stops of this TransportNetwork}
     */
    public List<Stop> getStops() {
        List<Vertex> vertices = this.getVertices();
        List<Stop> stops = new ArrayList<>();
        for (Vertex v : vertices)
            if (v.getClass() == Stop.class)
                stops.add((Stop)v);
        return stops;
    }

    /**
     * {@return all the transport segments of this TransportNetwork}
     */
    public List<TransportSegment> getTransportSegments() {
        List<Edge> edges = this.getEdges();
        List<TransportSegment> transportSegments = new ArrayList<>();
        for (Edge e : edges)
            if (e.getClass() == TransportSegment.class)
                transportSegments.add((TransportSegment)e);
        return transportSegments;
    }

    /**
     * Add a line to the TransportNetwork.
     * @return true if added, false if already present
     * @param line the Line to add
     * @throws IllegalArgumentException if line is null
     */
    public boolean addLine(Line line) {
        if (line == null) throw new IllegalArgumentException(ERR_NULL_ARG_MESSAGE);
        if (lines.contains(line)) return false;
        return lines.add(line);
    }

    /**
     * {@return the Stop in the TransportNetwork equal to stop or null if not found}
     * @param stop the stop to find by equals(Object)
     * @throws IllegalArgumentException if stop is null
     */
    public Stop findStop(Stop stop) {
        if (stop == null) throw new IllegalArgumentException(ERR_NULL_ARG_MESSAGE);
        for (Stop s : this.getStops())
            if (s.equals(stop))
                return s;
        return null;
    }

    /**
     * {@return the Line whose name is name or null if not found}
     * @param name the name of the Line
     * @throws IllegalArgumentException if name is null
     */
    public Line findLine(String name) {
        if (name == null) throw new IllegalArgumentException(ERR_NULL_ARG_MESSAGE);
        for (Line l : this.getLines())
            if (l.getName().equals(name))
                return l;
        return null;
    }

    /**
     * Add a Stop to this TransportNetwork. This will actually add a Vertex
     * to the Graph.
     * @param stop the Stop to add
     */
    public void addStop(Stop stop) {
        this.addVertex(stop);
    }

    /**
     * Add a TransportSegment to this TransportNetwork. This will actually
     * add an Edge to the Graph.
     * @param transportSegment the TransportSegment to add
     */
    public void addTransportSegment(TransportSegment transportSegment) {
        this.addEdge(transportSegment);
    }


    /**
     * Returns the description of the given route as a String containing
     * the arrival times at each Stop
     * @param route the route containing all the stops and travel durations of each edge
     * @param startTime the time when the first route edge is taken
     * @return a string with the whole route to take with times at each line change
     * String format: see PrettyPrinter.printTransportSegmentPathWithLineChangeTimes()
     */
    public String getRouteDescription(List<Edge> route, LocalTime startTime) {
        LocalTime cur = startTime;
        List<LocalTime> lts = new ArrayList<>();
        for( int i = 0; i < route.size(); i++ ) {
            Edge e = route.get( i );
            if( e instanceof TransportSegment transportSegment ) {
                Passages p = this.getPassages( (Stop) e.getFrom() );
                LocalTime next = p.getNextTimeWithWrap( cur,
                                                        transportSegment.getVariantName(),
                                                        transportSegment.getLineName() );
                if( next == null )
                    throw new IllegalStateException(
                              "There are no transports on the line "
                              + transportSegment.getLineName()
                              + " variant "
                              + transportSegment.getVariantName() );
                lts.add( next );
                cur = next.plus( transportSegment.getTravelDuration() );
            } else if( e instanceof WalkSegment segment ) {
                lts.add(cur);
                cur = cur.plus( segment.travelTime() );
            }
        }
        return PrettyPrinter.printTransportSegmentPathWithLineChangeTimes(this,route,lts);
    }

    /**
     * Add a departure time for a variant
     * @param lineName the variant's line name
     * @param variantName the variant's name
     * @param departureTime the departure time to add to the variant
     * @return true if the departure was added, false if not or if the line was not found
     * @throws IllegalArgumentException if lineName, variantName or variantName are null
     */
    public boolean addDepartureToVariant(String lineName, String variantName, LocalTime departureTime) {
        if( lineName == null || variantName == null || departureTime == null )
            throw new IllegalArgumentException( ERR_NULL_ARG_MESSAGE );
        for( Line l : this.getLines() ) {
            if( l.getName().equals( lineName ) ) {
                return l.getVariantNamed( variantName ).addDeparture( departureTime );
            }
        }
        return false;
    }


}
