/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.math.BigInteger;

import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * @author Thibault Falque
 *
 */
public class PBPreprocessingConstraint implements ITransformConstraint {
    private final IVecInt literals;
    private final IVec<BigInteger> coeffs;
    private final BigInteger weight;

    /**
     * @param literals
     * @param coeffs
     * @param weight
     */
    private PBPreprocessingConstraint(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger weight) {
        this.literals = literals;
        this.coeffs = coeffs;
        this.weight = weight;
    }

    public static PBPreprocessingConstraint newInstance(IVecInt literals,
            IVec<BigInteger> coeffs, BigInteger weight) {
        return new PBPreprocessingConstraint(literals.clone(), coeffs.clone(),
                weight);
    }

    @Override
    public void addConstraintToSolver(ISolver solver) {
        // TODO Auto-generated method stub

    }

}
