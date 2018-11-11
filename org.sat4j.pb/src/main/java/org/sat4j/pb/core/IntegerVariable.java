package org.sat4j.pb.core;

import java.io.Serializable;

import org.sat4j.core.ReadOnlyVecInt;
import org.sat4j.specs.IVecInt;

/**
 * Handle bounded positive integer variables through a binary decomposition.
 * 
 * @author lonca
 * 
 */
public class IntegerVariable implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final IVecInt vars;

    public IntegerVariable(IVecInt vars) {
        this.vars = new ReadOnlyVecInt(vars);
    }

    public IVecInt getVars() {
        return this.vars;
    }

    public int nVars() {
        return this.vars.size();
    }
}
