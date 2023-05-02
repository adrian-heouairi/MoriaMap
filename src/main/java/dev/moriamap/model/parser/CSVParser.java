package dev.moriamap.model.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class parses data from CSV file and provides it as raw string.
 */
public final class CSVParser {

    private CSVParser(){}

    /**
     * Parses a CSV file line.
     * @param line to be parsed
     * @param delimiter fields separators
     * @return a list of strings containing each field
     * @throws InconsistentCSVException if the line is empty
     */
    public static List<String> parseCSVLine(String line, String delimiter) throws InconsistentCSVException {
        if (line == null) {
            throw new IllegalArgumentException("Line can not be null.");
        }
        if (line.equals("")) {
            throw new InconsistentCSVException();
        }
        if (delimiter == null) {
            throw new IllegalArgumentException("Delimiter can not be null.");
        }
        if (delimiter.equals("")) {
            throw new IllegalArgumentException("Delimiter can not be an empty string.");
        }
        return new ArrayList<>(Arrays.asList(line.split(delimiter)));
    }

    /**
     * Parses a CSV file.
     * @param resource to CSV file
     * @return a list of lists of strings containing raw data.
     * @throws InconsistentCSVException if two lines do not have same number of fields
     */
    public static List<List<String>> extractLines(InputStream resource) throws InconsistentCSVException {
        if (resource == null) {
            throw new IllegalArgumentException("Path can not be null");
        }
        List<List<String>> content = new ArrayList<>();
        Scanner sc = new Scanner(resource);
        while (sc.hasNextLine()){
            content.add(CSVParser.parseCSVLine(sc.nextLine(),";"));

        }
        int len = content.get(0).size();
        boolean same = true;
        for (List<String> fields:content) {
            if (fields.size() != len) {
                same  = false;
                break;
            }
        }
        if (!same) {
            sc.close();
            throw new InconsistentCSVException();
        }
        sc.close();
        return content;
    }
}
