/**
 * 
 */
package org.sat4j.pb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.core.ConstrGroup;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.pb.constraints.pb.SubsetSum;
import org.sat4j.pb.preprocessing.IConstraintGroupSelectionStrategy;
import org.sat4j.pb.preprocessing.PBPreprocessingConstraint;
import org.sat4j.pb.preprocessing.PBPreprocessingConstraintGroup;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * @author Thibault Falque
 * @author Romain Wallon
 */
public class GaussPreprocessingPBDecorator extends PBSolverDecorator {
    private final List<PBPreprocessingConstraintGroup> groups;
    private IConstraintGroupSelectionStrategy strategy;

    private final SubsetSum subset;

    private static final int MAX_ELEMENT = 100;

    private static final int MAX_SUM = 4000;

    public GaussPreprocessingPBDecorator(IPBSolver solver) {
        super(solver);
        this.groups = new ArrayList<>();
        this.subset = new SubsetSum(MAX_SUM, MAX_ELEMENT);
    }

    public IConstr updateDegree(IConstr ctr) {
        if (!(ctr instanceof PBConstr)) {
            return ctr;
        }
        PBConstr pbCtr = (PBConstr) ctr;
        int[] coeffs = new int[ctr.size()];

        for (int i = 0; i < ctr.size(); i++) {
            coeffs[i] = (pbCtr.getCoefs()[i].intValue());
        }

        subset.setElements(coeffs);

    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        PBConstr ctr = super.addAtMost(literals, coeffs, degree);

    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger degree) throws ContradictionException {
        // TODO Auto-generated method stub
        return super.addAtMost(literals, coeffs, degree);
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
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger weight) throws ContradictionException {

        PBPreprocessingConstraint pbCtr = PBPreprocessingConstraint
                .newInstance(literals, coeffs, weight);
        if (!strategy.add(groups, pbCtr)) {
            PBPreprocessingConstraintGroup group = new PBPreprocessingConstraintGroup();
            group.add(pbCtr);
            groups.add(group);
        }
        return new ConstrGroup();
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
    
        return new ConstrGroup();
    }

    @Override
    public void preprocessing() {
        super.preprocessing();

    }

}
