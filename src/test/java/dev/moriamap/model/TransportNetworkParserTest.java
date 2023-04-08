package dev.moriamap.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class TransportNetworkParserTest {    
    @Test void testNumberOfTransportLine() throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_data_transportNetwork.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        assertEquals(2,tn.getLines().size());
    }

    @Test void testNumberOfVariant() throws InconsistentCSVException{
        List<Integer> transportNetworkVariantSizeList = new ArrayList<>();
        List<Integer> variantSizeList = List.of(3,3);

        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_data_transportNetwork.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);

        for(int i =0; i < tn.getLines().size();i++){
            Line l = tn.getLines().get(i);
            transportNetworkVariantSizeList.add(l.getVariants().size());
        }

        assertEquals(variantSizeList,transportNetworkVariantSizeList);

    }

    @Test void findLineNameEqualTrue() throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_data_transportNetwork.csv");

        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);

        assertEquals("8",tn.findLine("8").getName() );
    }

    @Test void findLineObjectEqualFalse()throws InconsistentCSVException{
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_data_transportNetwork.csv");

        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        Line l = Line.of("8");
        assertNotEquals(l,tn.findLine("8"));
    }

    @Test void findStopObjectEqualtrue() throws InconsistentCSVException {
        InputStream resource = CSVParserTest.class.getResourceAsStream("/test_data_transportNetwork.csv");
        TransportNetwork tn = TransportNetworkParser.generateFrom(resource);
        Stop stop = Stop.from("Faidherbe - Chaligny", GeographicPosition.at(48.85011054413369, 2.384028566383108));
       
       assertEquals(tn.getStopByName("Faidherbe - Chaligny"),stop) ;
    }

}
