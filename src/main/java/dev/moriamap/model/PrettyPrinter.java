package dev.moriamap.model;

import java.util.List;


/**
 * Class for printing different elements of the project
 */
public class PrettyPrinter {

	private PrettyPrinter(){}

	/**
	 * Method that print the path
	 * @param path list of edges to print
	 */
	public static void printEdgePath( List<Edge> path ) {
		if(path.isEmpty())
			return;
		System.out.print( path.get(0).getFrom() );
		for(Edge edge : path) {
			System.out.print( " --> " + edge.getTo() );
		}
		System.out.println();
	}

}
