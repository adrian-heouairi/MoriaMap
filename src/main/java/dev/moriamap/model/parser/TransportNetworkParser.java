package dev.moriamap.model.parser;

import java.io.InputStream;
import java.util.List;

import dev.moriamap.model.network.GeographicPosition;
import dev.moriamap.model.network.Line;
import dev.moriamap.model.network.Stop;
import dev.moriamap.model.network.TransportNetwork;
import dev.moriamap.model.network.TransportSegment;
import dev.moriamap.model.network.Variant;

/**
 * A TransportNetworkParser is a parser that read a InputStream of a csv file to create a TransportNetwork
 */
public class TransportNetworkParser {

    private TransportNetworkParser(){}

    /**
     * apply the algorithm described in diagrams/transport-network-generation-algorithm to produce a TransportNetwork
     * @param tuples a List of TransportSegmentRecord
     * @return a TransportNetwork corrsponding to the network given in argument
     */
    public static TransportNetwork generateFromTransportSegmentRecord(List<TransportSegmentRecord> tuples ){
        TransportNetwork tn = TransportNetwork.empty();

        for(TransportSegmentRecord t : tuples){
            
            Stop s1 = generateStop(tn,t.fromName(), t.fromLatitude(), t.fromLongitude());
            Stop s2 = generateStop(tn,t.toName(), t.toLatitude(), t.toLongitude());

            Line l = generateLine(t,tn);

            Variant v = generateVariant(l, t);


            TransportSegment segment = TransportSegment.from(s1, s2, t.lineName(), t.variantName(), t.duration(),t.distance());
            tn.addTransportSegment(segment);
            v.addTransportSegment(segment);

        }

        return tn;
    }

    /**
     * apply the algorithm described in diagrams/transport-network-generation-algorithm to produce a TransportNetwork
     * @param transportNetworkFileContent the stream to the data csv file 
     * @throws InconsistentCSVException if the line of the given CSV does not contain the expected number of fields
     * @return a TransportNetwork corrsponding to the network given in argument
     */
    public static TransportNetwork generateFrom(InputStream transportNetworkFileContent) throws InconsistentCSVException{
        List<TransportSegmentRecord> tuples = TransportSegmentRecord.fromTuples(CSVParser.extractLines(transportNetworkFileContent));
        return generateFromTransportSegmentRecord(tuples);
    }
    /**
     * @param l the Line that contains the Variant
     * @param t the actual TransportSegmentRecord that we are browsing
     * @return the Variant needed for the transportSegment contained in the Line or a new Variant
     */
    private static Variant generateVariant(Line l,TransportSegmentRecord t){
        Variant v = null;
        for(Variant lv : l.getVariants()){
            if (lv.getName().equals(t.variantName()) && lv.getLineName().equals(t.lineName()))
                v = lv;
        }
        if(v == null){
            v = Variant.empty(t.variantName(),t.lineName());
            l.addVariant(v);
        }

        return v;
    }


    /**
     * @param t the actual TransportSegmentRecord that we are browsing
     * @param tn the TransportNetwork
     * @return the Line contained in the TransportNetwork or if null a new Line 
     */
    private static Line generateLine(TransportSegmentRecord t,TransportNetwork tn){
        Line l = tn.findLine(t.lineName());
        if(l == null){
            l = Line.of(t.lineName());
            tn.addLine(l);
        }

        return l;
    }

    /**
     * Finds a stop or adds a new one with the given specification to the given TransportNetwork.
     * @param tn transport network to which the stop will be added
     * @param name name of the stop
     * @param latitude of the stop
     * @param longitude of the stop
     * @return the stop that had been added or found.
     */
    private static Stop generateStop(TransportNetwork tn,String name,double latitude,double longitude){
        Stop stop = Stop.from(name, GeographicPosition.at(latitude, longitude));
        Stop realStop = tn.findStop(stop);
        if(realStop == null){
            tn.addStop(stop);
            return stop;
        }else{
            return realStop;
        }
    }



}
