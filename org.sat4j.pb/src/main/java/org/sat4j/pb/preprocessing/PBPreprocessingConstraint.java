/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.math.BigInteger;

import org.sat4j.core.Vec;
import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.IteratorInt;

/**
 * @author Thibault Falque
 *
 */
public class PBPreprocessingConstraint implements ITransformConstraint {
    private final IVecInt literals;
    private final IVec<BigInteger> coeffs;
    private final BigInteger weight;

    private final PBPreprocessingConstraintType type;

    /**
     * @param literals
     * @param coeffs
     * @param weight
     */
    private PBPreprocessingConstraint(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger weight, PBPreprocessingConstraintType type) {
        this.literals = literals;
        this.coeffs = coeffs;
        this.weight = weight;
        this.type = type;
    }

    public static PBPreprocessingConstraint newExactly(IVecInt literals,
            IVec<BigInteger> coeffs, BigInteger weight) {
        return new PBPreprocessingConstraint(literals.clone(), coeffs.clone(),
                weight, PBPreprocessingConstraintType.EQ);
    }

    public static PBPreprocessingConstraint newExactly(IVecInt literals,
            IVecInt coeffs, int weight) {

        return new PBPreprocessingConstraint(literals.clone(),
                toBigInteger(coeffs), BigInteger.valueOf(weight),
                PBPreprocessingConstraintType.EQ);
    }

    public static PBPreprocessingConstraint newAtMost(IVecInt literals,
            IVec<BigInteger> coeffs, BigInteger weight) {
        return new PBPreprocessingConstraint(literals.clone(), coeffs.clone(),
                weight, PBPreprocessingConstraintType.LE);
    }

    public static PBPreprocessingConstraint newAtMost(IVecInt literals,
            IVecInt coeffs, int weight) {

        return new PBPreprocessingConstraint(literals.clone(),
                toBigInteger(coeffs), BigInteger.valueOf(weight),
                PBPreprocessingConstraintType.LE);
    }

    public static PBPreprocessingConstraint newAtLeast(IVecInt literals,
            IVec<BigInteger> coeffs, BigInteger weight) {
        return new PBPreprocessingConstraint(literals.clone(), coeffs.clone(),
                weight, PBPreprocessingConstraintType.GE);
    }

    public static PBPreprocessingConstraint newAtLeast(IVecInt literals,
            IVecInt coeffs, int weight) {

        return new PBPreprocessingConstraint(literals.clone(),
                toBigInteger(coeffs), BigInteger.valueOf(weight),
                PBPreprocessingConstraintType.GE);
    }

    /**
     * @param coeffs
     * @return
     */
    private static IVec<BigInteger> toBigInteger(IVecInt coeffs) {
        IVec<BigInteger> localCoeffs = new Vec<>(coeffs.size());

        for (IteratorInt it = coeffs.iterator(); it.hasNext();) {
            localCoeffs.push(BigInteger.valueOf(it.next()));
        }
        return localCoeffs;
    }

    @Override
    public void addConstraintToSolver(IPBSolver solver)
            throws ContradictionException {
        this.type.addConstraintToSolver(literals, coeffs, weight, solver);

    }

}
