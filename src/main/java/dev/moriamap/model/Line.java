package dev.moriamap.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of a transport network.
 */
public final class Line {

    // Name of this line
    private final String name;

    // Variants of this line
    private final List<Variant> variants;

    /**
     * Class constructor specifying name.
     *<p>
     *     The list of variants is initialized as an empty list.
     *</p>
     *
     * @param  name the name of this Line
     */
    private Line(String name) {
        this.name = name;
        this.variants = new ArrayList<>();
    }

    /**
     * {@return the name of this line}
     */
    public String getName() {
        return this.name;
    }

    /**
     * {@return a copy of this line's variants list}
     */
    public List<Variant> getVariants() {
        List <Variant> res = new ArrayList<>(this.variants.size());
        for (Variant v : this.variants)
            res.add(v);

        return res;
    }

    /**
     * Static factory.
     *
     * @param name of this line
     * @throws IllegalArgumentException if name is null
     * @return a new Line with the given name
     */
    public static Line of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name is not allowed");
        }
        return new Line(name);
    }

    /**
     * Checks if this line contains the given variant.
     *
     * @param v variant that we are looking for
     * @return true if this.variants contains v.
     */
    public boolean containsVariant(Variant v) {
        return this.variants.contains(v);
    }

    /**
     * Adds the given variant.
     * <p>
     * If this line already contains the given variant, the variants list remains unchanged.
     * </p>
     *
     * @param v variant to be added
     * @return true if the given variant was added
     * @throws IllegalArgumentException if the given variant is null
     */
    public boolean addVariant(Variant v) {
        if (v == null) {
            throw new IllegalArgumentException("Null variant is not allowed");
        }

        if (this.containsVariant(v)) {
            return false;
        }

        return this.variants.add(v);
    }

    /**
     * Check if this line is equal to the given line.
     * <p>
     *     Two lines are equal if they have the same name and the same
     *     (by a call to equals) variants in the same order.
     * </p>
     * @param object to be compared to
     * @return true if this is equal to object
     */
    @Override public boolean equals(Object object) {
        if (this ==  object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        Line other = (Line) object;
        if (other.variants.size() != this.variants.size())
            return false;
        for (int i = 0; i < this.variants.size(); i++) {
            if (!this.variants.get(i).equals(other.variants.get(i)))
                return false;
        }
        return this.name.equals(other.name);
    }


}
