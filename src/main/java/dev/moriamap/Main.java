package dev.moriamap;

import dev.moriamap.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

class Main {

    private static void print(OutputStream out, String str) {
        try {
            out.write( str.getBytes() );
        } catch( IOException ignored ) {
            Logging.getLogger().severe("Failed to write to OutputStream: \"" + str + "\"");
        }
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
                      2 - Get the trains schedules of a stop
                      3 - Exit
                    """);
            print(out, "Option: ");
            String option = inputScanner.nextLine();
            if (option.isBlank()|| option.equals("3"))
                break;

            Query query = null;
            if(option.equals("1")) {
                print(out, "Name of the starting stop: " );
                String startStopName = inputScanner.nextLine();
                if(startStopName.isBlank()) break;

                print(out, "Name of the target stop: " );
                String targetStopName = inputScanner.nextLine();
                if(targetStopName.isBlank()) break;
                query = new PLAN0Query(out, startStopName, targetStopName);
            } else if(option.equals( "2" )) {
                print(out, "Name of the stop: " );
                String stopName =  inputScanner.nextLine();
                if (stopName.isBlank()) break;
                query = new LECTTIMEQuery(out, stopName);
                query.execute(tn);
            }
            if(query == null) continue;
            query.execute( tn );
            print(out, "\n" );
        }
        inputScanner.close();
    }

}
