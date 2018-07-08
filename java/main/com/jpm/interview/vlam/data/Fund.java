package com.jpm.interview.vlam.data;

import com.jpm.interview.vlam.exception.DataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Fund
 *
 * Note:
 *   1. the name of the fund is its unique id (equals and hashCode would be based on this field only)
 *   2. using smallest monetary unit (cent) for fund value
 *   3. if the same fund (see 1. by the definition of same) with different value (or structure) is added
 *      to the parent twice, the later one takes precedence
 *   4. no check if B(100) is added to A but later found out B value is inconsistent with the sum
 *      of its constituents values (e.g. B is C(80) + D(30)) - need some normalization
 *
 */
public class Fund {
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
     * add to our list of children (but to make sure there is no duplicate)
     *
     * @param f
     */
    public void addFund(Fund f) {
        int pos = this.funds.indexOf(f);
        if (pos != -1) {
            Fund old = this.funds.get(pos);
            this.funds.set(pos, f);
            // this.value += (f.getValue() - old.getValue());
        } else {
            this.funds.add(f);
            // this.value += f.getValue();
        }
    }

    /**
     * update this.value (summation of all children values)...
     */
    public void updateValue() {
        if (!this.getFunds().isEmpty())     // otherwise leaf node will have zero value...
            this.value = this.getFunds().stream().mapToDouble(Fund::getValue).sum();
    }

    /**
     *
     * @param consumer
     */
    /*
    public void depthFirstTraversal(Consumer<Fund> consumer) {
        this.funds.stream().forEach(f -> {
            if (f.getFunds().isEmpty()) {
                consumer.accept(f);
            } else {
                f.depthFirstTraversal(consumer);
            }
        });
    }
    */
    public void depthFirstTraversal(Consumer<Fund> consumer) {
        this.funds.forEach(f -> {
            f.getFunds().forEach(c -> {
               c.depthFirstTraversal(consumer);
            });
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
        // sb.append("[");
        sb.append(this.name).append(": ").append(this.value).append(" children: ").append(this.funds);
        // sb.append("]");
        return sb.toString();
    }

}
