package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * Class for printing different elements of the project
 */
public class PrettyPrinter {

	private static final String FORMAT_RESET = "\033[0m";

	private PrettyPrinter(){}

	/**
	 * This method return a String for when a change in line is detected
	 * @param tn the transport network the new line belong to
	 * @param lineName the name of the new line
	 * @param segment the first transport segment of the route of this new line
	 * @return "null line" if the line was not found
	 * or a string build in the format:
	 * ## [Line and variant names] ## [Terminus of the variant]
	 *   [the 'from' stop of the transport segment]
	 * @throws IllegalArgumentException if line is null
	 */
	public static String lineChangeToString( TransportNetwork tn, String lineName, TransportSegment segment ) {
		Line line = tn.findLine( lineName );
		if(line == null) throw new IllegalArgumentException("null line");
		Variant variant = line.getVariantNamed( segment.getVariantName() );
		return "\n## \033[1;37mLine " + lineName + " variant " + variant.getName()
			   + FORMAT_RESET + " ## \033[4mTerminus: " + variant.getEnd()
			   + FORMAT_RESET + "\n    "
			   + segment.getFrom();
	}

	/**
	 * Method that print the path
	 * @param tn the transport network used for the path creation
	 * @param path list of edges to print
	 * @return The constructed String to print
	 */
	public static String printTransportSegmentPath(TransportNetwork tn, List<Edge> path ) {
		if(path.isEmpty())
			return "";
		TransportSegment segment = (TransportSegment) path.get( 0 );
		String currentLine = segment.getLineName();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( lineChangeToString(tn, currentLine, segment) );
		Duration totalDuration = Duration.ZERO;
		for( int i = 0; i < path.size(); i++ ) {
			Edge edge = path.get( i );
			segment = (TransportSegment) edge;
			totalDuration = totalDuration.plus( segment.getTravelDuration() );
			String time = segment.getTravelDuration().toString().substring( 2 );
			if( !segment.getLineName().equals( currentLine ) ) {
				stringBuilder.append( "\n" )
						.append( lineChangeToString( tn, segment.getLineName(), segment ) );
			}
			stringBuilder.append( " --(" ).append( time ).append( ")--> " );
			currentLine = segment.getLineName();
			if( i == path.size()-1 )
				stringBuilder.append( "\033[42m" ).append( segment.getTo() ).append( FORMAT_RESET );
			else
				stringBuilder.append( segment.getTo() );
		}
		stringBuilder.append("\n\nTotal traject duration: \033[5m\033[1m")
				.append(totalDuration.toString().substring( 2 ))
				.append( FORMAT_RESET );
		return stringBuilder.toString();
	}

	/**
	 * This method build a string that contain all the information to travel
	 * on the transport network, and with times at every line change
	 * @param tn the transport network this route is on
	 * @param route the route of edges to take in order
	 * @param lts list of times for every edge
	 * @return the built String with all the information
	 * format:
	 * 	'Switching line at [time]' (or for the first:'Taking line at: [starting time]')
	 * 	[when changing line] ## [Line and variant names] ## [terminus of the variant]
	 * 	[list of all the stops to go throught on the same line: s1 -> s2 -> s3]
	 * @throws IllegalArgumentException if route and lts are different sizes
	 */
	public static String printTransportSegmentPathWithLineChangeTimes(
			TransportNetwork tn,
			List<Edge> route,
			List<LocalTime> lts) {
		if(route.isEmpty()) return "";
		if(route.size() != lts.size())
			throw new IllegalArgumentException("route and lts have different sizes");
		StringBuilder stringBuilder = new StringBuilder();
		String currentLine = null;
		if(route.get( 0 ) instanceof TransportSegment firstSegment) {
			currentLine = firstSegment.getLineName();
			stringBuilder.append( "Taking line at " )
					  .append( lts.get( 0 ))
					  .append( lineChangeToString( tn, currentLine, firstSegment ) );
		}
		for( int i = 0; i < route.size(); i++ ) {
			Edge edge = route.get( i );
			if(edge instanceof TransportSegment segment) {
				if( !segment.getLineName().equals( currentLine ) ) {
					stringBuilder
							  .append( "\nSwitching line at " )
							  .append( lts.get( i ).plus( segment.getTravelDuration() ))
							  .append( lineChangeToString( tn, segment.getLineName(), segment ) );
				}
				stringBuilder.append( " --> " );
				currentLine = segment.getLineName();
				if( i == route.size() - 1 )
					stringBuilder.append( "\033[42m" ).append( segment.getTo() ).append( FORMAT_RESET );
				else
					stringBuilder.append( segment.getTo() );
			}
		}
		return stringBuilder.toString();
	}



}
