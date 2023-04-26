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
	 * Return a String of the given Duration
	 * @param dur the duration
	 * @return duration formatted as a string
	 * format: H hour(s) M minute(s) S second(s)
	 */
	public static String formatDuration(Duration dur) {
		String res = "";
		int hours = dur.toHoursPart();
		int minutes = dur.toMinutesPart();
		int seconds = dur.toSecondsPart();

		if(hours > 0)
			res += hours + " hour" + ( hours > 1 ? "s" : "" );
		if(minutes > 0)
			res += ( res.equals( "" ) ? "" : " " ) + minutes + " minute" + ( minutes > 1 ? "s" : "" );
		if(res.equals( "" ) || seconds > 0)
			res += ( res.equals( "" ) ? "" : " " ) + seconds + " second" + ( seconds > 1 ? "s" : "" );
		return res;
	}

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
		for( int i = 0; i < path.size(); i++ ) {
			Edge edge = path.get( i );
			segment = (TransportSegment) edge;
			if( !segment.getLineName().equals( currentLine ) ) {
				stringBuilder.append( "\n" )
						.append( lineChangeToString( tn, segment.getLineName(), segment ) );
			}
			stringBuilder.append( " --(" )
						 .append( formatDuration(segment.getTravelDuration()) )
						 .append( ")--> " );
			currentLine = segment.getLineName();
			if( i == path.size()-1 )
				stringBuilder.append( "\033[42m" ).append( segment.getTo() ).append( FORMAT_RESET );
			else
				stringBuilder.append( segment.getTo() );
		}
		return stringBuilder.toString();
	}

	private static Duration getEdgeDuration( Edge edge ) {
		if(edge instanceof TransportSegment segment)
			return segment.getTravelDuration();
		else if(edge instanceof WalkSegment segment)
			return segment.travelTime();
		throw new UnsupportedOperationException("Segment type not yet supported");
	}


	/**
	 * This method build a string that contain all the information to travel
	 * on the transport network, and with times at every line change
	 * @param tn the transport network this route is on
	 * @param route the route of edges to take in order
	 * @param lts list of times for every edge
	 * @return "" if route is empty or the built String with all the information
	 * format:
	 * 	'Switching line at [time]' (or for the first:'Taking line at: [starting time]')
	 * 	[when changing line] ## [Line and variant names] ## [terminus of the variant]
	 * 	[list of all the stops to go throught on the same line: s1 -> s2 -> s3]
	 */
	public static String printTransportSegmentPathWithLineChangeTimes(
			TransportNetwork tn,
			List<Edge> route,
			List<LocalTime> lts) {
		if(route.isEmpty()) return "";
		if(route.size() != lts.size())
			throw new IllegalArgumentException("route and lts have different sizes");
		StringBuilder builder = new StringBuilder();
		String currentLine = null;
		if(route.get( 0 ) instanceof TransportSegment firstSegment) {
			currentLine = firstSegment.getLineName();
			builder.append( "Taking line at " )
					  .append( lts.get( 0 ))
					  .append( lineChangeToString( tn, currentLine, firstSegment ) );
		}
		LocalTime arrivalTime = null;
		for( int i = 0; i < route.size(); i++ ) {
			Edge edge = route.get( i );
			if(edge instanceof TransportSegment segment) {
				if( !segment.getLineName().equals( currentLine ) ) {
					builder
							  .append("\n\nArrives at: ").append(lts.get( i-1 ).plus( getEdgeDuration(route.get(i-1)) ))
							  .append( ", leaving at: " ).append(lts.get(i))
							  .append( lineChangeToString( tn, segment.getLineName(), segment ) );
				}
				builder.append( " --> " );
				currentLine = segment.getLineName();
				if( i == route.size() - 1 )
					builder.append( "\033[42m" ).append( segment.getTo() ).append( FORMAT_RESET );
				else
					builder.append( segment.getTo() );
				if(i == route.size() - 1)
					arrivalTime = lts.get( i ).plus( segment.getTravelDuration());
			} else if(edge instanceof WalkSegment segment) {
				builder.append( "\n\nWalk for ")
					   .append( formatDuration(segment.travelTime()) )
					   .append( " from '" )
					   .append( segment.getFrom() )
					   .append( "' to '" )
					   .append( segment.getTo() )
					   .append( "'");
			}
		}
		builder.append("\n\nArrival time : ")
				  .append(arrivalTime);
		return builder.toString();
	}



}
