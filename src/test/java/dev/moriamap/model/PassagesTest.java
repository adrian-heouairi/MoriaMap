package dev.moriamap.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PassagesTest {
    private List<TransportSchedule> newListTransportScheduleHelper() {
        Variant v = Variant.empty("1", "8");
        Stop s1 = Stop.from("Lourmel", GeographicPosition.NULL_ISLAND);
        Stop s2 = Stop.from("Boucicaut", GeographicPosition.NORTH_POLE);
        TransportSegment tseg = TransportSegment.from(
                s1, s2, "8", "1", Duration.ZERO, 0
        );
        v.addTransportSegment(tseg);
        LocalTime lt = LocalTime.MIN;
        TransportSchedule tsch = new TransportSchedule(lt, s1, v);
        return List.of(tsch);
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
        assertEquals("At Lourmel: line 8 direction Boucicaut (variant 1): 00:00\n",
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
        assertNotEquals(p,o);
    }

    @Test
    void semanticallyEqualInstancesAreEqual() {
        Passages p = Passages.of(newListTransportScheduleHelper());
        Passages p1 = Passages.of(newListTransportScheduleHelper());

        assertEquals(p,p1);
    }

    @Test
    void passageIsNotEqualToNull() {
        Passages p = Passages.of(newListTransportScheduleHelper());
        assertNotEquals(null,p);
    }

    @Test
    void equalInstancesHaveSameHashcode() {
        Passages p = Passages.of(newListTransportScheduleHelper());
        Passages p1 = Passages.of(newListTransportScheduleHelper());
        assertEquals(p.hashCode(),p1.hashCode());
    }
}
