package dev.moriamap;

import dev.moriamap.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.Scanner;

class Main {

    private static void print(OutputStream out, String str) {
        try {
            out.write( str.getBytes() );
        } catch( IOException ignored ) {
            Logging.getLogger().severe("Failed to write to OutputStream: \"" + str + "\"");
        }
    }

    private static LocalTime parseTime( String hoursStr, String minutesStr ) {
        try {
            int hours = Integer.parseInt( hoursStr );
            int minutes = Integer.parseInt( minutesStr );
            return LocalTime.of( hours, minutes );
        } catch( Exception e ) {
            return null;
        }
    }

    private static String getOptimizationsChoiceDescriptions() {
        StringBuilder res = new StringBuilder("How do you want to optimize the route?\n    ");
        int len = RouteOptimization.values().length;
        for( int i = 0; i < len; i++ ) {
            res.append( i+1 )
                      .append( " for " )
                      .append( RouteOptimization.values()[i] );
            if(i < len-1)
                res.append( ", " );
        }
        res.append("\n    Option: ");
        return res.toString();
    }

    public static void main(String[] args) {
        InputStream in = System.in;
        OutputStream out = System.out;

        TransportNetwork tn = null;
        try {
            InputStream mapDataInputStream = Main.class.getResourceAsStream( "/map_data.csv" );
            tn = TransportNetworkParser.generateFrom( mapDataInputStream );
        } catch( InconsistentCSVException e ) {
            print(out, "An issue occurred while parsing the transport network\n");
            System.exit( 1 );
        }
        try {
            InputStream timetablesInputStream = Main.class.getResourceAsStream( "/timetables.csv" );
            DepartureParser.addDeparturesTo(tn, timetablesInputStream );
        } catch( InconsistentCSVException e ) {
            print(out, "An issue occurred while parsing the transports schedules\n");
            System.exit( 1 );
        }
        Scanner inputScanner = new Scanner(in);

        print(out, "At any moment waiting for an input, " +
                            "pressing ENTER without typing anything exit the program\n" );

        while(true) {
           print(out, """
                    What do you want to do?
                      1 - Get a path from one stop to an another
                      2 - Get the transport schedules of a stop
                      3 - Get an optimized path from one stop to an another
                      4 - Exit
                    """);
            print(out, "Option: ");
            String option = inputScanner.nextLine();
            if (option.isBlank()|| option.equals("4"))
                break;

            Query query = null;
            if(option.equals("1")) {
                print( out, "Name of the starting stop: " );
                String startStopName = inputScanner.nextLine();
                if( startStopName.isBlank() ) break;
                print( out, "Name of the target stop: " );
                String targetStopName = inputScanner.nextLine();
                if( targetStopName.isBlank() ) break;
                query = new PLAN0Query( out, startStopName, targetStopName );
            } else if(option.equals("2")) {
                print( out, "Name of the stop: " );
                String stopName = inputScanner.nextLine();
                if( stopName.isBlank() ) break;
                query = new LECTTIMEQuery( out, stopName );
            } else if(option.equals("3")) {
                print( out, "Name of the starting stop: " );
                String startStopName = inputScanner.nextLine();
                if( startStopName.isBlank() ) break;
                print( out, "Name of the target stop: " );
                String targetStopName = inputScanner.nextLine();
                if( targetStopName.isBlank() ) break;

                print( out, getOptimizationsChoiceDescriptions() );
                RouteOptimization optimizationChoice;
                try {
                    int input = Integer.parseInt( inputScanner.nextLine() );
                    if( input < 1 || input > RouteOptimization.values().length ) {
                        print( out, "Entered input is not in bounds" );
                        continue;
                    }
                    optimizationChoice = RouteOptimization.values()[input - 1];
                } catch( NumberFormatException e ) {
                    print( out, "Entered input is not a valid number" );
                    continue;
                }

                print( out, "When do you want to start your travel? (just press ENTER for right now)" +
                            "\n   hours: " );
                String hoursStr = inputScanner.nextLine();
                print( out, "   minutes: " );
                String minutesStr = inputScanner.nextLine();
                LocalTime startTime;
                if( hoursStr.isBlank() || minutesStr.isBlank() )
                    startTime = LocalTime.now();
                else
                    startTime = parseTime( hoursStr, minutesStr );
                if( startTime != null )
                    query = new PLAN1Query( out, startStopName, targetStopName, optimizationChoice, startTime );
            }
            if(query == null) continue;
            print(out, "\n" );
            query.execute( tn );
            print(out, "\n" );
        }
        inputScanner.close();
    }

}
