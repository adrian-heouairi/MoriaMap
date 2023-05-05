package dev.moriamap.model.network;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class PassagesTest {
  private List<TransportSchedule> newListTransportScheduleHelper() {
    Variant v = Variant.empty("1", "8");
    Stop s1 = Stop.from("Lourmel", GeographicPosition.NULL_ISLAND);
    Stop s2 = Stop.from("Boucicaut", GeographicPosition.NORTH_POLE);
    TransportSegment tseg = TransportSegment.from(s1, s2, "8", "1", Duration.ZERO, 0);
    v.addTransportSegment(tseg);
    LocalTime lt = LocalTime.MIN;
    TransportSchedule tsch = new TransportSchedule(lt, s1, v);
    TransportSchedule tsch1 = new TransportSchedule(LocalTime.of(14, 37), s1, v);
    return List.of(tsch1, tsch);
  }

  @Test
  void getTransportSchedules() {
    List<TransportSchedule> ltsch = this.newListTransportScheduleHelper();
    Passages p = Passages.of(ltsch);
    assertEquals(ltsch, p.getTransportSchedules());
  }

  @Test
  void getFullDescription() {
    List<TransportSchedule> ltsch = this.newListTransportScheduleHelper();
    Passages p = Passages.of(ltsch);
    assertEquals(
        """
                        At Lourmel: line 8 direction Boucicaut (variant 1): 14:37:00
                        At Lourmel: line 8 direction Boucicaut (variant 1): 00:00:00
                        """,
        p.getFullDescription());
  }

  @Test
  void passageIsEqualToItSelf() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertEquals(p, p);
  }

  @Test
  void instanceOfDifferentClassIsNotEqual() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    Object o = new Object();
    assertNotEquals(p, o);
  }

  @Test
  void semanticallyEqualInstancesAreEqual() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    Passages p1 = Passages.of(newListTransportScheduleHelper());

    assertEquals(p, p1);
  }

  @Test
  void passageIsNotEqualToNull() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertNotEquals(null, p);
  }

  @Test
  void equalInstancesHaveSameHashcode() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    Passages p1 = Passages.of(newListTransportScheduleHelper());
    assertEquals(p.hashCode(), p1.hashCode());
  }

  @Test
  void inexistantVariantReturnsNull() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertNull(p.getNextTimeWithWrap(LocalTime.now(), "14", "14"));
  }

  @Test
  void weHaveTheCorrectLocalTimeForNextTransport() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertEquals(LocalTime.MIN, p.getNextTimeWithWrap(LocalTime.MIN, "1", "8"));
  }

  @Test
  void timeWrapsToNextDay() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertEquals(LocalTime.MIN, p.getNextTimeWithWrap(LocalTime.of(14, 40), "1", "8"));
  }

  @Test
  void weHaveTheCorrectAmountOfTimeUntilNextTransportArrives() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertEquals(Duration.ofMinutes(2), p.getWaitTimeWithWrap(LocalTime.of(14, 35), "1", "8"));
  }

  @Test
  void waitTimeWithWrapReturnsNullIfThereIsNoTrain() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertNull(p.getWaitTimeWithWrap(LocalTime.of(12, 43), "14", "23"));
  }

  @Test
  void waitTimeWithWrapReturnsCorretTimeWhileWrapingToNextDay() {
    Passages p = Passages.of(newListTransportScheduleHelper());
    assertEquals(Duration.ofMinutes(1), p.getWaitTimeWithWrap(LocalTime.of(23, 59), "1", "8"));
  }
}
