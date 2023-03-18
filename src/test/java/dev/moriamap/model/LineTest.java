package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LineTest {

    @Test void testLineStaticFactory() {
        Line l = Line.of("13");
        assertEquals("13", l.getName());
    }

    @Test void testStaticFactoryThrrowsExceptionIfNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Line.of(null)
                );
    }
}
