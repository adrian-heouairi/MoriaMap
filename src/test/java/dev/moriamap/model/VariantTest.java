package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariantTest {

    @Test void testConstruction() {
        Variant v = Variant.empty(1, Line.of("14"));
        assertEquals(1, v.getId());
    }

    @Test void testEmptyVariantWithNegativeIdThrowsException() {
        Line l = Line.of("3");
        assertThrows(IllegalArgumentException.class, () -> Variant.empty(-1,l)
                );
    }

    @Test void testEmptyVariantWithNullLineThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Variant.empty(1, null)
        );
    }

    @Test void variantsHavingSameIdAndLinesWithSameReferencesAreEqual() {
        Line l = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(1,l);
        assertEquals(v,v1);
    }

    @Test void variantsHavingSameIdAndSemanticallySameLinesAreEqual() {
        Line l = Line.of("14");
        Line l1 = Line.of("14");
        Variant v = Variant.empty(1,l);
        Variant v1 = Variant.empty(1, l1);
        assertEquals(v, v1);
    }

    @Test void testVariantIsEqualToItself() {
        Variant v = Variant.empty(1, Line.of("13"));
        assertEquals(v, v);
    }

    @Test void testVariantInstanceIsNotEqualToNull() {
        Variant v = Variant.empty(1, Line.of("14"));
        assertNotEquals(null, v);
    }

    @Test void testObjectIsNotEqualToVariant() {
        Object o = new Object();
        Variant v = Variant.empty(3,Line.of("4"));
        assertNotEquals(v,o);
    }

    @Test void testVariantsWithDifferentIdAndSameLineAreNotEqual(){
        Variant v = Variant.empty(2,Line.of("4"));
        Variant v1 = Variant.empty(4, Line.of("4"));
        assertNotEquals(v,v1);
    }

    @Test void testVariantOfSameIdInDifferentLinesAreNotEqual() {
        Variant v = Variant.empty(2, Line.of("2"));
        Variant v1 = Variant.empty(2, Line.of("3"));
        assertNotEquals(v,v1);
    }

    @Test void testGetLine() {
        Line l = Line.of("13");
        Variant v = Variant.empty(3, l);
        assertEquals(l, v.getLine());
    }
}
