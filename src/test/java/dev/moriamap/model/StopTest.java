package dev.moriamap.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.ArrayList;

class StopTest {
    @Test void stopConstructorTest(){
        Stop s = Stop.from("test",GeographicPosition.NULL_ISLAND);
        assertEquals("test",s.getName());
    }
}
