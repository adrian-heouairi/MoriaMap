package dev.moriamap.model;

import java.io.OutputStream;

/**
 * A LECTTIMEQuery represents a request for schedule information fetching.
 */
public class LECTTIMEQuery extends Query {

    /**
     * The name of the stop this query use for getting schedules
     */
    private final String stopName;


    /**
     * Constructor of LECTTIMEQuery
     * @param out the outputStream where the result will be written
     * @param stopName the name of the stop used in the query
     */
    public LECTTIMEQuery(OutputStream out, String stopName){
        super(out);
        if (stopName == null)
            throw new IllegalArgumentException("Stop name can't be null");
        this.stopName = stopName;
    }
    
    @Override
    protected String run(TransportNetwork network) throws QueryFailureException {
        Stop stop = network.getStopByInexactName(stopName);
        if( stop == null ) {
            throw new QueryFailureException("Stop was not found");
        }
        Passages passages = network.getPassages(stop);
        return passages.getFullDescription();
    }

}
