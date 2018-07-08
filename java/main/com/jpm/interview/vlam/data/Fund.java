package com.jpm.interview.vlam.data;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * Fund
 *
 * Note:
 *   1. the name of the fund is its unique id (equals and hashCode are based on this field only)
 *   2. using smallest monetary unit (cent) for fund value (better rounding handling)
 *   3. if the same fund (see 1. by the definition of same) with different value (or structure) is added
 *      to the parent twice, the later one takes precedence
 *   4. no check on the consistency of our value against sum of our children values at the time of addFund()
 *   5. no parent-child relationship is set when doing addFund()
 *
 *   6. for 4. and 5. above it requires normalization, this class provides node-level update()
 *   (no traversal - make use of depthFirstTraversal())
 *
 */
public class Fund {
    private Fund parent = null;
    private String name;        // see 1. above
    private double value;       // see 2. above, this is cent...
    private List<Fund> funds;


    /**
     *
     * @param name
     * @param value
     */
    public Fund(String name, double value) {
        this.name = name;
        this.value = value;
        this.funds = new ArrayList<>();
    }

    /**
     *
     * @param name
     */
    public Fund(String name) {
        this(name, 0d);
    }

    /*
     *
     * @return
     *
    public Fund getParent() { return this.parent; } */

    /**
     *
     * @return the name of the Fund
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the value (if there is sub-funds, the aggregate value)...
     */
    public double getValue() {
        return this.value;
    }

    /**
     *
     * @return
     */
    public List<Fund> getFunds() {
        return this.funds;
    }

    /**
     * add to our list of children (make sure there is no duplicate)
     *
     * @param f
     */
    public void addFund(Fund f) {
        int pos = this.funds.indexOf(f);
        if (pos != -1) {
            this.funds.set(pos, f);
        } else {
            this.funds.add(f);
            this.value += f.getValue();
        }
    }

    /**
     * update:
     * 1. value (summation of all children values)
     * 2. parent for all children
     *
     */
    public void update() {
        this.getFunds().forEach(f -> f.parent = this);
        if (!this.getFunds().isEmpty())     // i.e. do not update value for leaf node, take it as it is...
            this.value = this.getFunds().stream().mapToDouble(Fund::getValue).sum();
    }

    /**
     *
     * @return
     */
    public Fund findRoot() {
        Fund current = this;
        while (current.parent != null)
            current = current.parent;
        return current;
    }

    /**
     * Depth-First-Traversal and for each node call consumer.accept()
     *
     * @param consumer
     */
    public void depthFirstTraversal(Consumer<Fund> consumer) {
        this.funds.forEach(f -> {
            f.getFunds().forEach(c -> c.depthFirstTraversal(consumer));
            consumer.accept(f);
        });
        consumer.accept(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fund fund = (Fund) o;
        return Objects.equals(name, fund.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(": ").append(this.value).append(" parent: ").append(this.parent == null ? "-" : this.parent.name).append(" children: ").append(this.funds);
        return sb.toString();
    }


}
