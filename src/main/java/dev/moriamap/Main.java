package dev.moriamap;

import dev.moriamap.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;

class Main {
    private static Scanner cachedScanner = null;

    private static void print(OutputStream out, String str) {
        try {
            out.write( str.getBytes() );
        } catch( IOException ignored ) {
            Logging.getLogger().severe("Failed to write to OutputStream: \"" + str + "\"");
        }
    }

    private static String getInput(InputStream in) {
        if (cachedScanner == null) cachedScanner = new Scanner(in);
        String ret = "";
        try {
            ret = cachedScanner.nextLine();
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
        return ret;
    }

    private static LocalTime getTime(InputStream in, OutputStream out) {
        print( out, "When do you want to start your travel? (just press ENTER for right now)\n");
        LocalTime lt = null;
        while (lt == null) {
            print(out, "   hour: ");
            String hourStr = getInput(in);
            if (hourStr.isBlank())
                lt = LocalTime.now();
            else {
                print(out, "   minute: ");
                String minuteStr = getInput(in);
                if (minuteStr.isBlank())
                    lt = LocalTime.now();
                else {
                    try {
                        int hour = Integer.parseInt(hourStr);
                        int minute = Integer.parseInt(minuteStr);
                        lt = LocalTime.of(hour, minute);
                    } catch (Exception e) {
                        print(out, "Invalid input, retry\n");
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
        res.append("\n    Option: ");
        return res.toString();
    }

    private static String getStopName(String message, TransportNetwork tn, InputStream in, OutputStream out){
        print(out, message);

        String input = getInput(in);
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

        print(out, askChoice.toString());
        String choice = getInput(in);
        int choiceInt = -1;
        try {
            choiceInt = Integer.parseInt(choice) - 1;
        } catch (Exception e) {
            return choice;
        }
        if (0 <= choiceInt && choiceInt < nearestStops.size())
            return nearestStops.get(choiceInt).getName();
        else return "";
    }

    private static TransportNetwork createTransportNetwork(
            InputStream transportNetworkInputStream, InputStream timetablesInputStream,
            OutputStream out) {
        TransportNetwork res = null;

        try {
            res = TransportNetworkParser.generateFrom( transportNetworkInputStream );
        } catch( InconsistentCSVException e ) {
            print(out, "An issue occurred while parsing the transport network\n");
            System.exit( 1 );
        }
        try {
            DepartureParser.addDeparturesTo(res, timetablesInputStream );
        } catch( InconsistentCSVException e ) {
            print(out, "An issue occurred while parsing the transport schedules\n");
            System.exit( 1 );
        }

        return res;
    }

    private static LECTTIMEQuery makeLECTTIMEQuery(TransportNetwork tn, InputStream in, OutputStream out) {
        String stopName = getStopName("Name of the stop: ",tn, in, out);
        return new LECTTIMEQuery( out, stopName );
    }

    private static PLAN0Query makePLAN0Query(TransportNetwork tn, InputStream in, OutputStream out) {
        String startStopName = getStopName("Name of the starting stop: ",tn, in, out);
        String targetStopName = getStopName("Name of the destination stop: ",tn, in, out);
        return new PLAN0Query( out, startStopName, targetStopName );
    }

    private static PLAN1Query makePLAN1Query(TransportNetwork tn, InputStream in, OutputStream out) {
        String startStopName = getStopName("Name of the starting stop: ",tn, in, out);
        String targetStopName = getStopName("Name of the destination stop: ",tn, in, out);

        print( out, getOptimizationChoicesDescription() );
        RouteOptimization[] values = RouteOptimization.values();
        RouteOptimization optimizationChoice = null;
        while (optimizationChoice == null) {
            try {
                optimizationChoice = values[Integer.parseInt(getInput(in)) - 1];
            } catch (Exception e) {
                print(out, "Invalid input, retry\n");
            }
        }

        LocalTime startTime = getTime(in, out);

        return new PLAN1Query( out, startStopName, targetStopName, optimizationChoice, startTime );
    }

    public static void main(String[] args) {
        InputStream in = System.in;
        OutputStream out = System.out;

        TransportNetwork tn = createTransportNetwork(
                Main.class.getResourceAsStream( "/map_data.csv" ),
                Main.class.getResourceAsStream( "/timetables.csv" ),
                out
        );

        print(out, "Press Ctrl+C at any moment to exit the program\n");

        while(true) {
           print(out, """
                    What do you want to do?
                      1 - Get the transport schedules of a stop
                      2 - Get a path from a stop to another
                      3 - Get an optimized path from a stop to another
                      4 - Exit
                    """);
            print(out, "Choice: ");
            String option = getInput(in);

            if (option.equals("4")) break;

            Query query = switch (option) {
                case "1" -> makeLECTTIMEQuery(tn, in, out);
                case "2" -> makePLAN0Query(tn, in, out);
                case "3" -> makePLAN1Query(tn, in, out);
                default -> null;
            };

            print(out, "\n");

            if (query != null) {
                query.execute(tn);
                print(out, "\n");
            }
        }
    }
}
