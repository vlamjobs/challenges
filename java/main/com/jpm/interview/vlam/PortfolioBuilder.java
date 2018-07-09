package com.jpm.interview.vlam;


import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.util.Logger;

import java.util.*;
import java.util.stream.Collectors;


/**
 * To go around the constraints in the Fund class and to handle unordered input
 * e.g.:
 *      C,E,1000
 *      A,B,400
 *      B,C,2000
 *      ... etc
 *
 *      where processing input line by line cannot easily work out the parent-child relationship
 *      (or more accurately to avoid doing complex tree building and wait until all input are accepted
 *       then perform normalization on the nodes)
 *
 */
public class PortfolioBuilder {
    // we need a Map rather than a set because the later won't allow us to get element from it
    // (which we needed to because two Funds are considered equal if their names equal)
    private Map<Fund,Fund> portfolio;
    private boolean isBuilt = false;        // flag to tell if normalized


    /**
     *
     */
    public PortfolioBuilder() {
        this.portfolio = new LinkedHashMap<>();
    }


    /**
     *
     * @param fund
     */
    public void add(Fund fund) {

        Fund same = this.portfolio.get(fund);
        if (same != null) {
            // add each child of 'fund' to 'same'
            fund.getFunds().forEach(same::addFund);
        } else {
            // simple case, just add it to the portfolio...
            this.portfolio.put(fund, fund);
        }

        // process its children also...
        fund.getFunds().forEach(this::add);

        // every add operation mark as not-normalized
        this.isBuilt = false;
    }

    /**
     * TODO: can further optimize by doing all these (except depthFirstTraversal) in add() above...
     */
    private void build() {
        // 0. so calling normalized builder multiple times is more efficient
        if (this.isBuilt) return;
        Logger.debug(this.portfolio.toString());

        // this contains all non top-level element...
        Set<Fund> toRemove = new HashSet<>();

        // 1. for all fund, get its children and add those to the toRemove set, also find the latest from portfolio
        //    and add it back to the parent
        this.portfolio.values().stream().forEach(f -> f.getFunds().forEach(c -> {
            // every child we encounter is not top-level element...
            toRemove.add(c);
            // the one in 'this.portfolio' is the MOST accurate...
            Fund latest = this.portfolio.get(c);
            if (latest != null) {
                f.addFund(latest);
            }
        }));
        toRemove.stream().forEach(this.portfolio::remove);

        // 2. update fund value (just in case the input contains inconsistency) and parent-child relationship
        this.portfolio.values().stream().forEach(f -> f.depthFirstTraversal(Fund::update));

        // 3. finally flag as done
        this.isBuilt = true;
    }


    /**
     *
     * @return the portfolio - i.e. list of top-level fund(s), normalized (i.e. isBuilt)
     */
    public List<Fund> get() {
        if (!this.isBuilt) this.build();
        return this.portfolio.keySet().stream().collect(Collectors.toList());
    }

    /**
     * clear all
     */
    public void reset() {
        this.portfolio.clear();
        this.isBuilt = false;
    }


}
