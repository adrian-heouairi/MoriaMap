package dev.moriamap.model;

/**
 * A LECTTIMEQuery represents a request for schedule information fetching.
 */
public record LECTTIMEQuery(
        String stopName)  implements Query {
    
    /**
     * Constructor of PLAN0Query
     * @param stopName the name of the stop
     */
    public LECTTIMEQuery {
        if (stopName == null)
            throw new IllegalArgumentException("Stop name can't be null");
    }

    /**
     * Auxiliary method that run the LECTTIMEQuery
     * @param network The TransportNetwork the query is run on
     * @return the result of the query to print
     * @throws QueryFailureException if stop was not found
     */
    String run(TransportNetwork network) throws QueryFailureException {
        Stop stop = network.getStopByName(stopName);
        if( stop == null ) {
            throw new QueryFailureException("Stop was not found");
        }
        Passages passages = network.getPassages(stop);
        return passages.getFullDescription();
    }
    @Override
    public void execute(TransportNetwork network) {
        String result;
        try {
            result = run(network);
        } catch (Exception e) {
            result = "Error : " + e.getMessage();
        }
        System.out.println(result);
    }
}
