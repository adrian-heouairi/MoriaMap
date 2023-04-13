package dev.moriamap;

import dev.moriamap.model.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Main {

    private static boolean routeFromTwoStops(TransportNetwork tn, Scanner inputScanner) {
        System.out.print( "Name of the starting stop: " );
        String startStopName = inputScanner.nextLine();
        if(startStopName.isBlank()) return true;

        System.out.print( "Name of the target stop: " );
        String targetStopName = inputScanner.nextLine();
        if(targetStopName.isBlank()) return true;

        Stop start = tn.getStopByName( startStopName );
        Stop target = tn.getStopByName( targetStopName );
        if(start == null || target == null) {
            System.out.println( "One of the stops was not found, please check your inputs and repeat" );
        } else {
            try {
                Map<Vertex, Edge> dfs = tn.depthFirstSearch( start );
                List<Edge> path = Graph.getRouteFromTraversal( dfs, start, target );
                PrettyPrinter.printEdgePath(tn, path );
            } catch(Exception e) {
                System.out.println( "An issue occurred during the path finding, please check your inputs and repeat" );
            }
        }
        return false;
    }

    private static boolean getStopSchedules(TransportNetwork tn, Scanner inputScanner) {
        System.out.print( "Name of the stop: " );
        String stopName = inputScanner.nextLine();
        if(stopName.isBlank()) return true;
        Stop stop = tn.getStopByName(stopName);
        if( stop == null ) {
            System.out.println( "Stop was not found" );
        }
        Passages passages = tn.getPassages(stop);
        System.out.println( passages.getFullDescription() );
        return false;
    }

    public static void main(String[] args) {
        TransportNetwork tn = null;
        try {
            InputStream mapDataInputStream = Main.class.getResourceAsStream( "/map_data.csv" );
            tn = TransportNetworkParser.generateFrom( mapDataInputStream );
        } catch( InconsistentCSVException e ) {
            System.out.println( "An issue occurred while parsing the transport network" );
            System.exit( 1 );
        }
        try {
            InputStream timetablesInputStream = Main.class.getResourceAsStream( "/timetables.csv" );
            DepartureParser.addDeparturesTo(tn, timetablesInputStream );
        } catch( InconsistentCSVException e ) {
            System.out.println( "An issue occurred while parsing the transports schedules" );
            System.exit( 1 );
        }
        Scanner inputScanner = new Scanner(System.in);

        System.out.println( "At any moment waiting for an input, " +
                            "pressing ENTER without typing anything exit the program\n" );
        while(true) {
            System.out.println("""
                    What do you want to do?
                      1 - Get a path from one stop to an another
                      2 - Get the trains schedules of a stop
                    """);
            System.out.print("Option: ");
            String option = inputScanner.nextLine();
            if (
                    option.isBlank()
                || (option.equals("1") && routeFromTwoStops(tn, inputScanner))
                || (option.equals( "2" ) && getStopSchedules(tn, inputScanner))
            )
                break;
            System.out.println();
        }
        inputScanner.close();
    }

}
