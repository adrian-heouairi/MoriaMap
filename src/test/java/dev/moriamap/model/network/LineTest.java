package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class LineTest {

  @Test
  void testLineStaticFactory() {
    Line l = Line.of("13");
    assertEquals("13", l.getName());
  }

  @Test
  void testStaticFactoryThrowsExceptionIfNullArgument() {
    assertThrows(IllegalArgumentException.class, () -> Line.of(null));
  }

  @Test
  void testAddingTwiceTheSameVariantsDoesNothing() {
    Line l = Line.of("14");
    Variant v = Variant.empty("1", "14");
    l.addVariant(v);
    assertEquals(1, l.getVariants().size());
  }

  @Test
  void testAddNullVariantThrowsException() {
    Line l = Line.of("14");
    assertThrows(IllegalArgumentException.class, () -> l.addVariant(null));
  }

  @Test
  void testAddingAVariantWhichWasAddedBeforeDoesNothing() {
    Line l = Line.of("14");

    Variant v = Variant.empty("1", "14");
    l.addVariant(v);
    assertFalse(l.addVariant(v));
  }

  @Test
  void testLinesThatDoNotHaveSameNumberOfVariantsAreNotEqual() {
    Line l = Line.of("14");
    Line l1 = Line.of("14");
    Variant v = Variant.empty("1", "14");
    Variant v1 = Variant.empty("2", "14");
    l.addVariant(v);
    l.addVariant(v1);
    l1.addVariant(v);
    assertNotEquals(l, l1);
  }

  @Test
  void testEqualsOnItselfReturnsTrue() {
    Line l = Line.of("14");
    assertEquals(l, l);
  }

  @Test
  void testLinesSameLinesWithSameVariantsAreEqual() {
    Line l = Line.of("14");
    Line l1 = Line.of("14");
    Variant v = Variant.empty("1", "14");
    Variant v1 = Variant.empty("2", "14");
    l.addVariant(v);
    l.addVariant(v1);
    l1.addVariant(v);
    l1.addVariant(v1);
    assertEquals(l, l1);
  }

  @Test
  void testLinesThatHaveAtLeastOneDifferentVariantAreNotEqual() {
    Line l = Line.of("14");
    Line l1 = Line.of("14");
    Variant v = Variant.empty("1", "14");
    Variant v1 = Variant.empty("2", "14");
    l.addVariant(v);
    l.addVariant(v1);
    l1.addVariant(v1);
    l1.addVariant(v);
    assertNotEquals(l, l1);
  }

  @Test
  void testObjectIsNotEqualToLine() {
    Object o = new Object();
    Line l = Line.of("14");
    assertNotEquals(l, o);
  }

  @Test
  void testLineIsNotEqualToNull() {
    Line l = Line.of("14");
    assertNotEquals(null, l);
  }

  @Test
  void hashCodeOfSemanticallyEqualLinesAreEqual() {
    Line l = Line.of("14");
    Line l1 = Line.of("14");
    Variant v = Variant.empty("1", "14");
    l.addVariant(v);
    l1.addVariant(v);
    assertEquals(l.hashCode(), l1.hashCode());
  }

  @Test
  void getVariantNamedWithNullThrowsException() {
    var sut = Line.of("13");
    assertThrows(NullPointerException.class, () -> sut.getVariantNamed(null));
  }

  @Test
  void getVariantNamedWhenAbsentInEmptyLineThrowsException() {
    var sut = Line.of("12");
    var v = Variant.empty("v", "12");
    var name = v.getName();
    assertThrows(NoSuchElementException.class, () -> sut.getVariantNamed(name));
  }

  @Test
  void containsVariantNullNamedThrowsException() {
    var sut = Line.of("11");
    assertThrows(NullPointerException.class, () -> sut.containsVariantNamed(null));
  }

  @Test
  void containsVariantNamedWhenVariantPresentReturnsTrue() {
    var sut = Line.of("10");
    var v = Variant.empty("find me", "10");
    sut.addVariant(v);
    assertTrue(sut.containsVariantNamed(v.getName()));
  }

  @Test
  void containsVariantNameWhenVariantAbsentReturnsFalse() {
    var sut = Line.of("9");
    var v = Variant.empty("absent", "9");
    assertFalse(sut.containsVariantNamed(v.getName()));
  }

  @Test
  void getVariantNamedWhenPresentReturnsSuchVariant() {
    var sut = Line.of("8");
    var v1 = Variant.empty("present", "8");
    var v2 = Variant.empty("present too", "8");
    var name = v1.getName();
    sut.addVariant(v1);
    sut.addVariant(v2);
    assertEquals(v1, sut.getVariantNamed(name));
  }
}
