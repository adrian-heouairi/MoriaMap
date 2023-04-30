package dev.moriamap.model;

import java.io.OutputStream;

/**
 * A TransportSchedulesQuery represents a request for schedule information fetching.
 */
public class TransportSchedulesQuery extends Query {

    /**
     * The name of the stop this query use for getting schedules
     */
    private final String stopName;


    /**
     * Constructor of TransportSchedulesQuery
     * @param out the outputStream where the result will be written
     * @param stopName the name of the stop used in the query
     */
    public TransportSchedulesQuery( OutputStream out, String stopName ){
        super(out);
        if (stopName == null)
            throw new IllegalArgumentException("Stop name can't be null");
        this.stopName = stopName;
    }
    
    @Override
    protected String run(TransportNetwork network) throws QueryFailureException {
        Stop stop = network.getStopByName(stopName);
        if( stop == null ) {
            throw new QueryFailureException("Stop was not found");
        }
        Passages passages = network.getPassages(stop);
        return passages.getFullDescription();
    }

}
