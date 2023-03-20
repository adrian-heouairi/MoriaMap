package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariantTest {

    @Test public void testConstruction() {
        Variant v = Variant.empty(1, Line.of("14"));
        assertEquals(1, v.getId());
    }

    @Test public void testEmptyVariantWithNegativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Variant.empty(-1, Line.of("3"))
                );
    }

    @Test public void testEmptyVariantWithNullLineThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Variant.empty(1, null)
        );
    }

    @Test public void variantsHavingSameIdAndLinesWithSameReferencesAreEqual() {
        Line l = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(1,l);
        assertEquals(v,v1);
    }

    @Test public void variantsHavingSameIdAndSemanticallySameLinesAreEqual() {
        Line l = Line.of("14");
        Line l1 = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(1, l1);
        assertEquals(v, v1);
    }

    @Test public void testVariantIsEqualToItself() {
        Variant v = Variant.empty(1, Line.of("13"));
        assertEquals(v, v);
    }

    @Test public void testVariantInstanceIsNotEqualToNull() {
        Variant v = Variant.empty(1, Line.of("14"));
        assertNotEquals(v, null);
    }

    @Test public void testObjectIsNotEqualToVariant() {
        Object o = new Object();
        Variant v = Variant.empty(3,Line.of("4"));
        assertNotEquals(v,o);
    }

    @Test public void testVariantsWithDifferentIdAndSameLineAreNotEqual(){
        Variant v = Variant.empty(2,Line.of("4"));
        Variant v1 = Variant.empty(4, Line.of("4"));
        assertNotEquals(v,v1);
    }

    @Test public void testVariantOfSameIdInDifferentLinesAreNotEqual() {
        Variant v = Variant.empty(2, Line.of("2"));
        Variant v1 = Variant.empty(2, Line.of("3"));
        assertNotEquals(v,v1);
    }

    @Test public void testGetLine() {
        Line l = Line.of("13");
        Variant v = Variant.empty(3, l);
        assertEquals(l, v.getLine());
    }
}
