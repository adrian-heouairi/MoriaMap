package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test void testLineStaticFactory() {
        Line l = Line.of("13");
        assertEquals("13", l.getName());
    }

    @Test void testStaticFactoryThrowsExceptionIfNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Line.of(null)
                );
    }

    @Test void testAddingTwiceTheSameVariantsDoesNothing() {
        Line l = Line.of("14");
        Variant v = Variant.empty(1,l);
        l.addVariant(v);
        assertEquals(1, l.getVariants().size());
    }

    @Test void testAddNullVariantThrowsException() {
        Line l = Line.of("14");
        assertThrows(IllegalArgumentException.class,
                () -> l.addVariant(null)
                );
    }

    @Test void testAddingAVariantWhichWasAddedBeforeDoesNothing() {
        Line l = Line.of("14");

        Variant v = Variant.empty(1,l);
        l.addVariant(v);
        assertFalse(l.addVariant(v));
    }

    @Test void testLinesThatDoNotHaveSameNumberOfVariantsAreNotEqual() {
        Line l = Line.of("14");
        Line l1 = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(2,l);
        l.addVariant(v);
        l.addVariant(v1);
        l1.addVariant(v);
        assertNotEquals(l,l1);
    }

    @Test void testLinesSameLinesWithSameVariantsAreEqual() {
        Line l = Line.of("14");
        Line l1 = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(2,l);
        l.addVariant(v);
        l.addVariant(v1);
        l1.addVariant(v);
        l1.addVariant(v1);
        assertEquals(l,l1);
    }

    @Test void testLinesThatHaveAtLeastOneDifferentVariantAreNotEqual() {
            Line l = Line.of("14");
            Line l1 = Line.of("14");
            Variant v = Variant.empty(1,l);
            Variant v1 = Variant.empty(2,l);
            l.addVariant(v);
            l.addVariant(v1);
            l1.addVariant(v1);
            l1.addVariant(v);
            assertNotEquals(l,l1);
    }

    @Test void testObjectIsNotEqualToLine() {
        Object o = new Object();
        Line l = Line.of("14");
        assertNotEquals(l,o);
    }

    @Test void testLineIsNotEqualToNull() {
        Line l = Line.of("14");
        assertNotEquals(null,l);
    }
}
