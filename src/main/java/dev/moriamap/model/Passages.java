package dev.moriamap.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Passages object represents every passage/departure of every transport
 * at a given stop for one day. A Passages object containing nothing can
 * exist, if no transports are circulating for a day.
 */
public final class Passages {
    private final List<TransportSchedule> transportSchedules;

    private Passages(List<TransportSchedule> transportSchedules) {
        this.transportSchedules = new ArrayList<>(transportSchedules);
    }

    /**
     * {@return a new Passages object having the given list of TransportSchedule}
     * @param transportSchedules the TransportSchedules that this Passages object will contain
     */
    public static Passages of(List<TransportSchedule> transportSchedules) {
        return new Passages(transportSchedules);
    }

    /**
     * {@return the list of TransportSchedule of this Passages}
     */
    public List<TransportSchedule> getTransportSchedules() {
        return new ArrayList<>(this.transportSchedules);
    }

    /**
     * Returns a big unsorted string containing one line for each scheduled
     * passage/departure for one day at the stop this Passages object is for.
     * Example of line:
     * At La Fourche: line 13 direction Châtillon Montrouge (variant 3): 19:57
     * @return a descriptive string containing one line for each contained TransportSchedule
     */
    public String getFullDescription() {
        StringBuilder res = new StringBuilder();
        for (TransportSchedule tsch : transportSchedules) {
            res.append("At ");
            res.append(tsch.stop().getName());

            res.append(": ");

            res.append("line ");
            res.append(tsch.variant().getLineName());
            res.append(" ");

            res.append("direction ");
            res.append(tsch.variant().getEnd());
            res.append(" ");

            res.append("(variant ");
            res.append(tsch.variant().getName());
            res.append(")");

            res.append(": ");

            res.append(PrettyPrinter.formatLocalTime( tsch.time() ));

            res.append("\n");
        }

        return res.toString();
    }

    /**
     * @return this.transportSchedules == ((Passages)object).transportSchedules
     */
    @Override public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        Passages other = (Passages) object;
        return other.transportSchedules.equals(this.transportSchedules);
    }

    /**
     * @return the hash code of this Passages object.
     */
    @Override public int hashCode() {
        final int prime = 17;
        int hash = 1;

        for (TransportSchedule tsch : this.transportSchedules) {
            hash *= prime;
            hash += tsch.hashCode();
        }

        return hash;
    }
    
    private List<TransportSchedule> getTransportScheduleForTheGivenLineAndVariant(String lineName, String variantName) {
        List<TransportSchedule>res = new ArrayList<>();
        for (TransportSchedule ts:transportSchedules) {
            if (ts.variant().getName().equals(variantName) && ts.variant().getLineName().equals(lineName)) {
                res.add(ts);
            }
        }
        return res;
    }

    /**
     * This method returns the time at which the next transport of
     * (lineName, variantName) will come at the Stop this Passages is for. 
     * It returns null if there is no time. If there are no transports left
     * for the day, the time of the first transport of the next day is returned.
     * @param waitStart time at which we start waiting at the stop
     * @param variantName of the transport we are waiting for
     * @param lineName of the transport we are waiting for
     * @return time at which next transport will come.
     * If  waitStart is equal to the next transport time, it returns waitStart
     */
    public LocalTime getNextTimeWithWrap(LocalTime waitStart, String variantName, String lineName) {
        Duration min = Duration.ofHours(25);
        LocalTime target = null;
        List<TransportSchedule> tsByLineAndVariant = getTransportScheduleForTheGivenLineAndVariant(lineName,variantName);
        for (TransportSchedule ts : tsByLineAndVariant) {
            Duration d = Duration.between(waitStart,ts.time()); 
            if (d.isZero()) return waitStart;
            if (d.isNegative()) {
                d = d.plus(Duration.ofDays(1));
            }
            if (d.compareTo(min) < 0) {
                target = ts.time();
                min = d;
            }
        }
        return target;
    }

    /**
     * This method returns the time one has to wait to ride the next transport
     * of (lineName, variantName) coming to the Stop this Passages is for. It
     * returns a Duration of zero if there is transport at waitStart.
     * If there are no transports, it returns null. If the next transport comes
     * the next day (after midnight), that's okay.
     * @param waitStart time at which we start waiting
     * @param variantName of the transport we are waiting for
     * @param lineName of the transport we are waiting for
     * @return the amount of time we have to wait until the next transport arrives.
     */
    public Duration getWaitTimeWithWrap(LocalTime waitStart, String variantName, String lineName) {
        LocalTime nextTransportTime = getNextTimeWithWrap(waitStart,variantName,lineName);
        Duration toWait = null;
        if (nextTransportTime != null) {
            toWait = Duration.between(waitStart,nextTransportTime);
            if (toWait.isNegative()) toWait = toWait.plusDays(1);
        }
        return toWait;
    }
}
