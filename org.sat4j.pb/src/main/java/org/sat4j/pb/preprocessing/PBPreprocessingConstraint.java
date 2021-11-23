/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.math.BigInteger;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
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
     * @return the literals
     */
    public IVecInt getLiterals() {
        return literals;
    }

    public BigInteger getCoeff(int lit) {
        int index = literals.indexOf(lit);
        if (index == -1) {
            return BigInteger.ZERO;
        }
        return coeffs.get(index);
    }

    /**
     * @return the coeffs
     */
    public IVec<BigInteger> getCoeffs() {
        return coeffs;
    }

    /**
     * @return the weight
     */
    public BigInteger getWeight() {
        return weight;
    }

    /**
     * @return the type
     */
    public PBPreprocessingConstraintType getType() {
        return type;
    }

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

    public PBPreprocessingConstraint mult(BigInteger coeff) {
        IVec<BigInteger> localCoeffs = new Vec<>(this.coeffs.size());

        for (int i = 0; i < this.coeffs.size(); i++) {
            localCoeffs.push(this.coeffs.get(i).multiply(coeff));
        }

        return PBPreprocessingConstraint.newExactly(this.literals, localCoeffs,
                this.weight.multiply(coeff));
    }

    public PBPreprocessingConstraint sub(PBPreprocessingConstraint other) {
        IVecInt finalLits = new VecInt();
        IVec<BigInteger> localCoeffs = new Vec<>();
        for (IteratorInt it = this.literals.iterator(); it.hasNext();) {
            int lit = it.next();
            BigInteger localCoeff = this.getCoeff(lit)
                    .subtract(other.getCoeff(lit));
            if (localCoeff.equals(BigInteger.ZERO)) {
                continue;
            }
            finalLits.push(lit);
            localCoeffs.push(localCoeff);
        }
        return PBPreprocessingConstraint.newExactly(finalLits, localCoeffs,
                weight.subtract(other.getWeight()));
    }

}
