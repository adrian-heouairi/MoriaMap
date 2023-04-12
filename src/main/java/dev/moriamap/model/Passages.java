package dev.moriamap.model;

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

            res.append(tsch.time().toString());

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
}
