package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
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
     * Loop through every edges of the TransportNetwork and associate
     * each edge to its distance inside a Map.
     * @return the map which associates every edge to its distance
     */
    public Map<Edge, Double> getDistanceWeights(){
        var res = new HashMap<Edge, Double>();
        for(Edge e : this.getEdges()){
            if (e.getClass() == TransportSegment.class){
                res.put(e,((TransportSegment)e).getDistance());
            }
        }
        return res;
    }
}
