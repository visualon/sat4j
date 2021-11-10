/**
 * 
 */
package org.sat4j.pb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.core.ConstrGroup;
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

    public GaussPreprocessingPBDecorator(IPBSolver solver) {
        super(solver);
        this.groups = new ArrayList<>();
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {

        return new ConstrGroup();
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
    public void preprocessing() {
        super.preprocessing();

    }

}
