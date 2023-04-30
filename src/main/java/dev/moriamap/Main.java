package dev.moriamap;

import dev.moriamap.model.*;

import java.io.*;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;

class Main {

    private static Scanner cachedScanner = null;
    private static InputStream in;
    private static OutputStream out;

    private static void print(String str) {
        try {
            out.write( str.getBytes() );
        } catch( IOException ignored ) {
            Logging.getLogger().severe("Failed to write to OutputStream: \"" + str + "\"");
        }
    }

    private static String getInput() {
        if (cachedScanner == null) cachedScanner = new Scanner(in);
        String ret = "";
        try {
            ret = cachedScanner.nextLine();
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
        return ret;
    }

    private static LocalTime getTime() {
        print( "When do you want to start your travel? (just press ENTER for right now)\n");
        LocalTime lt = null;
        while (lt == null) {
            print("   hour: ");
            String hourStr = getInput();
            if (hourStr.isBlank())
                lt = LocalTime.now();
            else {
                print("   minute: ");
                String minuteStr = getInput();
                if (minuteStr.isBlank())
                    lt = LocalTime.now();
                else {
                    try {
                        int hour = Integer.parseInt(hourStr);
                        int minute = Integer.parseInt(minuteStr);
                        lt = LocalTime.of(hour, minute);
                    } catch (Exception e) {
                        print("Invalid input, retry\n");
                    }
                }
            }
        }
        return lt;
    }

    private static String getOptimizationChoicesDescription() {
        StringBuilder res = new StringBuilder("How do you want to optimize the route?\n    ");
        int len = RouteOptimization.values().length;
        for( int i = 0; i < len; i++ ) {
            res.append( i+1 )
                      .append( " for " )
                      .append( RouteOptimization.values()[i] );
            if(i < len-1)
                res.append( ", " );
        }
        res.append("\n    Choice: ");
        return res.toString();
    }

    private static String getStopName(String message, TransportNetwork tn){
        print(message);

        String input = getInput();
        if(input.isBlank()) return "";

        List<Stop> nearestStops = tn.getNearestStopsByInexactName(input, 5);
        if(nearestStops.get(0).getName().equals(input)) return input;

        StringBuilder askChoice = new StringBuilder("Did you mean...\n    ");
        int len = nearestStops.size();
        for( int i = 0; i < len; i++ ) {
            askChoice.append( i+1 )
                      .append(") ")
                      .append( nearestStops.get(i).getName() );
            if(i < len-1)
                askChoice.append( ", " );
        }
        askChoice.append("\n    Choice: ");

        print(askChoice.toString());
        String choice = getInput();
        int choiceInt;
        try {
            choiceInt = Integer.parseInt(choice) - 1;
        } catch (Exception e) {
            return choice;
        }
        if (0 <= choiceInt && choiceInt < nearestStops.size())
            return nearestStops.get(choiceInt).getName();
        else return "";
    }

    private static String getInputWithPrompt(String message){
        print(message);
        return getInput();
    }

    private static RouteOptimization getRouteOptimization(){
        print( getOptimizationChoicesDescription() );
        RouteOptimization[] values = RouteOptimization.values();
        RouteOptimization optimizationChoice = null;
        while (optimizationChoice == null) {
            try {
                optimizationChoice = values[Integer.parseInt(getInput()) - 1];
            } catch (Exception e) {
                print("Invalid input, retry\n");
            }
        }

        return optimizationChoice;
    }

    private static TransportNetwork createTransportNetwork(
            InputStream transportNetworkInputStream, InputStream timetablesInputStream) {
        TransportNetwork res = null;

        try {
            res = TransportNetworkParser.generateFrom( transportNetworkInputStream );
        } catch( InconsistentCSVException e ) {
            print("An issue occurred while parsing the transport network\n");
            System.exit( 1 );
        }
        try {
            DepartureParser.addDeparturesTo(res, timetablesInputStream );
        } catch( InconsistentCSVException e ) {
            print("An issue occurred while parsing the transport schedules\n");
            System.exit( 1 );
        }

        return res;
    }

    private static TransportSchedulesQuery makeTransportSchedulesQuery( TransportNetwork tn ) {
        String stopName = getStopName("Name of the stop: ",tn);
        return new TransportSchedulesQuery( out, stopName );
    }

    private static RouteBetweenStopsQuery makeRouteBetweenStopsQuery( TransportNetwork tn ) {
        String startStopName = getStopName("Name of the starting stop: ",tn);
        String targetStopName = getStopName("Name of the destination stop: ",tn);
        return new RouteBetweenStopsQuery( out, startStopName, targetStopName );
    }

    private static OptimizedRouteBetweenPositionsQuery makeOptimizedRouteBetweenStopsQuery( TransportNetwork tn ) {
        String startStopName = getStopName("Name of the starting stop: ",tn);
        Stop startStop = tn.getStopByName( startStopName );
        if(startStop == null) return null;

        String targetStopName = getStopName("Name of the destination stop: ",tn);
        Stop targetStop = tn.getStopByName( targetStopName );
        if(targetStop == null) return null;

        RouteOptimization optimizationChoice = getRouteOptimization();
        LocalTime startTime = getTime();
        return new OptimizedRouteBetweenPositionsQuery( out, startStop, targetStop, optimizationChoice, startTime );
    }

    private static GeographicVertex getGeographicVertex(TransportNetwork tn, String message) {
        String choice = getInputWithPrompt("What is your " + message +
                                           " point?\n   1 - A Stop, 2 - A position\n   Choice: ");
        if(choice.equals( "1" )) {
            String stopName = getStopName( "Name of the " + message + " stop: ", tn);
            return tn.getStopByName( stopName );
        } else {
            String latitude = getInputWithPrompt( "Latitude of the " + message +
                                                  " position \n(for example: -4, 20.5, 24 12 35 N or 27 12 45 S): ");
            String longitude = getInputWithPrompt( "Longitude of the " + message +
                                                   " position \n(for example: 98, -102.36745, 35 59 11 W, 0 56 32 E): ");
            try {
                return GeographicVertex.at( GeographicPosition.from( latitude, longitude ) );
            } catch( Exception e ) {
                print("Invalid " + message + " latitude or longitude");
                return null;
            }
        }
    }

    private static OptimizedRouteBetweenPositionsQuery makeOptimizedRouteBetweenPositionsQuery( TransportNetwork tn ){
        GeographicVertex startGeoVertex = getGeographicVertex(tn, "starting");
        if(startGeoVertex == null) return null;
        GeographicVertex targetGeoVertex = getGeographicVertex(tn, "destination");
        if(targetGeoVertex == null) return null;
        RouteOptimization optimizationChoice = getRouteOptimization();
        LocalTime startTime = getTime();
        
        return new OptimizedRouteBetweenPositionsQuery( out, startGeoVertex, targetGeoVertex, optimizationChoice, startTime);
    }

    private static OptimizedRouteBetweenPositionsWithWalkQuery makeOptimizedRouteBetweenPositionsWithWalkQuery( TransportNetwork tn ){
        GeographicVertex startGeoVertex = getGeographicVertex(tn, "starting");
        if(startGeoVertex == null) return null;
        GeographicVertex targetGeoVertex = getGeographicVertex(tn, "destination");
        if(targetGeoVertex == null) return null;
        RouteOptimization optimizationChoice = getRouteOptimization();
        LocalTime startTime = getTime();

        return new OptimizedRouteBetweenPositionsWithWalkQuery( out, startGeoVertex, targetGeoVertex, optimizationChoice, startTime);
    }

    public static void main(String[] args) {
        in = System.in;
        out = new PrintStream( new FileOutputStream( FileDescriptor.out));

        TransportNetwork tn = createTransportNetwork(
                Main.class.getResourceAsStream( "/map_data.csv" ),
                Main.class.getResourceAsStream( "/timetables.csv" ) );

        print("Press Ctrl+C at any moment to exit the program\n");

        while(true) {
           print("""
                    What do you want to do?
                      1 - Get the transport schedules of a stop
                      2 - Get a path from a stop to another
                      3 - Get an optimized path from a stop to another
                      4 - Get an optimized path from a position to another
                          without walking sections between stops
                      5 - Get an optimized path from a position to another
                          with walking sections between stops
                      6 - Exit
                    """);
            print("Choice: ");
            String option = getInput();

            if (option.equals("6")) break;

            Query query = switch (option) {
                case "1" -> makeTransportSchedulesQuery(tn);
                case "2" -> makeRouteBetweenStopsQuery(tn);
                case "3" -> makeOptimizedRouteBetweenStopsQuery(tn);
                case "4" -> makeOptimizedRouteBetweenPositionsQuery(tn);
                case "5" -> makeOptimizedRouteBetweenPositionsWithWalkQuery(tn);
                default -> null;
            };

            print("\n");

            if (query != null) {
                query.execute(tn);
                print("\n");
            } else {
                print("Failed to create a query, check your inputs and repeat\n\n");
            }
        }
    }
}
