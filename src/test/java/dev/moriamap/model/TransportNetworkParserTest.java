package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

class TransportNetworkParserTest {    
    @Test void testNumberOfTransportLine() throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data_incomplete.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        assertEquals(1,tn.getLines().size());
    }

    @Test void testNumberOfVariant() throws InconsistentCSVException{

        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data_incomplete.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);

        assertEquals(1,tn.getVariants().size());

    }

    @Test void findLineNameEqualTrue() throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data_incomplete.csv");

        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);

        assertEquals("8",tn.findLine("8").getName() );
    }

    @Test void findLineObjectEqualFalse()throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data_incomplete.csv");

        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        Line l = Line.of("8");
        assertNotEquals(l,tn.findLine("8"));
    }

    @Test void findStopObjectEqualtrue() throws InconsistentCSVException {
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_map_data_incomplete.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        Stop stop = Stop.from("Lourmel", GeographicPosition.at(48.83866086365992, 2.2822419598550767));
       
       assertEquals(tn.getStopByName("Lourmel"),stop) ;
    }

}
