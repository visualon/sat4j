/**
 * 
 */
package org.sat4j.pb;

import java.math.BigInteger;
import java.util.Arrays;

import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.pb.constraints.pb.SubsetSum;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * @author Thibault Falque
 *
 */
public class PreprocessingPBDecorator extends PBSolverDecorator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final SubsetSum subset;

    private static final int MAX_ELEMENT = 100;

    private static final int MAX_SUM = 4000;

    public PreprocessingPBDecorator(IPBSolver solver) {
        super(solver);
        this.subset = new SubsetSum(MAX_SUM, MAX_ELEMENT);
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        PBConstr ctr = (PBConstr) super.addAtMost(literals, coeffs, degree);
        subset.setElements(coeffs.toArray());
        applySubsetSum(degree, ctr.getSumCoefs().intValue());
        return ctr;
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger degree) throws ContradictionException {
        PBConstr ctr = (PBConstr) super.addAtMost(literals, coeffs, degree);
        int[] coeffsInt = Arrays.stream(coeffs.toArray())
                .mapToInt(BigInteger::intValue).toArray();
        subset.setElements(coeffsInt);
        applySubsetSum(degree.intValue(), ctr.getSumCoefs().intValue());
        return ctr;
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        // TODO Auto-generated method stub
        return super.addAtLeast(literals, coeffs, degree);
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger degree) throws ContradictionException {
        // TODO Auto-generated method stub
        return super.addAtLeast(literals, coeffs, degree);
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        // TODO Auto-generated method stub
        return super.addExactly(literals, coeffs, weight);
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger weight) throws ContradictionException {
        // TODO Auto-generated method stub
        return super.addExactly(literals, coeffs, weight);
    }

    private void applySubsetSum(int degree, int sumCoeff) {
        for (int i = degree; i < sumCoeff; i++) {
            subset.sumExists(i);
        }
    }

}
