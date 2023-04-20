package dev.moriamap.model;

import java.time.Duration;
import java.util.List;


/**
 * Class for printing different elements of the project
 */
public class PrettyPrinter {

	private PrettyPrinter(){}

	private static String lineChangeToString(TransportNetwork tn, String lineName, TransportSegment segment) {
		Line line = tn.findLine( lineName );
		Variant variant = line.getVariantNamed( segment.getVariantName() );
		return "\n## \033[1;37mLine " + lineName + " variant " + variant.getName()
				+ "\033[0m ## \033[4mTerminus: " + variant.getEnd()
				+ "\033[0m\n    ";
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
		stringBuilder.append( lineChangeToString(tn, currentLine, segment) )
					 .append( segment.getFrom() );
		Duration totalDuration = Duration.ZERO;
		for( int i = 0; i < path.size(); i++ ) {
			Edge edge = path.get( i );
			segment = (TransportSegment) edge;
			totalDuration = totalDuration.plus( segment.getTravelDuration() );
			String time = segment.getTravelDuration().toString().substring( 2 );
			if( !segment.getLineName().equals( currentLine ) ) {
				stringBuilder.append( "\n" )
							 .append( lineChangeToString( tn, segment.getLineName(), segment ) )
							 .append( segment.getFrom() );
			}
			stringBuilder.append( " --(" ).append( time ).append( ")--> " );
			currentLine = segment.getLineName();
			if( i == path.size()-1 )
				stringBuilder.append( "\033[42m" ).append( segment.getTo() ).append( "\033[0m" );
			else
				stringBuilder.append( segment.getTo() );
		}
		stringBuilder.append("\n\nTotal traject duration: \033[5m\033[1m")
				  .append(totalDuration.toString().substring( 2 ))
				  .append("\033[0m");
		return stringBuilder.toString();
	}

}
