package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class DepartureParserTest {
        
    @Test void createDepartureRecordArgumentIsNull(){
        assertThrows(IllegalArgumentException.class,() ->{
            DepartureParser.addDeparturesTo(null, null);
        });
    }

    @Test void createDepartureRecordBadFile(){ 
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_timetables.csv");
        assertThrows(IllegalArgumentException.class,() ->{
            DepartureParser.addDeparturesTo(null, resource);
        });
    }

    @Test void createDepartureRecorGoodArgument() throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_timetables.csv");
        InputStream resourceMap = CSVParserTest.class.getResourceAsStream("/test_map_data.csv");

        TransportNetwork tn = TransportNetworkParser.generateFrom(resourceMap);
        
        assertDoesNotThrow(() -> { DepartureParser.addDeparturesTo(tn, resource);}, "DepartureParser has trow an error it shouldn't have");

       

    }

}
