package com.jpm.interview.vlam;


import com.jpm.interview.vlam.data.Fund;
import com.jpm.interview.vlam.util.Logger;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 */
public class PortfolioBuilder {

    // we need a Map rather than a set because Fund.equals() only check name so Set.contains() won't work
    private Map<Fund,Fund> portfolio;
    private boolean isBuilt = false;


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
        this.isBuilt = false;
    }

    /**
     *
     */
    private void build() {
Logger.debug(this.portfolio.toString());

        // this contains all non top-level element...
        Set<Fund> toRemove = new HashSet<>();

        // 1. for all fund, get its children and add those to the toRemove set, also find the latest from portfolio
        //    and add it back to the parent...
        this.portfolio.values().stream().forEach(f -> {
            f.getFunds().forEach(c -> {
                // every child we encounter is not top-level element...
                toRemove.add(c);
                // the one in 'portfolio' is the MOST accurate...
                Fund latest = this.portfolio.get(c);
                if (latest != null) {
                    f.addFund(latest);
                }
            });
        });
        toRemove.stream().forEach(this.portfolio::remove);

        // 2. update fund value (just in case the input contains inconsistency)
        this.portfolio.values().stream().forEach(f -> {
            f.depthFirstTraversal(Fund::updateValue);
        });

        this.isBuilt = true;
    }


    public List<Fund> get() {
        if (!this.isBuilt) this.build();
        return this.portfolio.keySet().stream().collect(Collectors.toList());
    }

}
