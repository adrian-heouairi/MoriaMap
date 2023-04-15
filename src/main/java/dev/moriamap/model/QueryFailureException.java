package dev.moriamap.model;


/**
 * This exception is thrown during a query's execution to signal an issue in the inputs
 */
public class QueryFailureException extends Exception{

	/**
	 * The constructor for this exception
	 * @param message the message the exception should show
	 */
	public QueryFailureException(String message) {
		super(message);
	}

}
