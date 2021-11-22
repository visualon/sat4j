/**
 * 
 */
package org.sat4j.pb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.core.ConstrGroup;
import org.sat4j.pb.preprocessing.IConstraintGroupSelectionStrategy;
import org.sat4j.pb.preprocessing.PBPreprocessing;
import org.sat4j.pb.preprocessing.PBPreprocessingConstraint;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.FakeConstr;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

/**
 * @author Thibault Falque
 * @author Romain Wallon
 */
public class PreprocessibleSolver extends PBSolverDecorator {
    private final List<PBPreprocessingConstraint> constraints;
    private IConstraintGroupSelectionStrategy strategy;
    private final PBPreprocessing preprocessor;

    private boolean added;

    public PreprocessibleSolver(IPBSolver solver,
            PBPreprocessing preprocessor) {
        super(solver);
        this.preprocessor = preprocessor;
        this.constraints = new ArrayList<>();

    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newAtMost(literals, coeffs, degree));
        return FakeConstr.instance();
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger degree) throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newAtMost(literals, coeffs, degree));
        return FakeConstr.instance();
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newAtLeast(literals, coeffs, degree));
        return FakeConstr.instance();
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger degree) throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newAtLeast(literals, coeffs, degree));
        return FakeConstr.instance();
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs,
            BigInteger weight) throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newExactly(literals, coeffs, weight));
        return new ConstrGroup();
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        constraints.add(
                PBPreprocessingConstraint.newExactly(literals, coeffs, weight));
        return new ConstrGroup();
    }

    @Override
    public void preprocessing() {
        super.preprocessing();

    }

    private boolean addConstraints() {
        if (!added) {
            added = true;
            for (PBPreprocessingConstraint c : preprocessor
                    .preprocess(constraints)) {
                try {
                    c.addConstraintToSolver(decorated());
                } catch (ContradictionException e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isSatisfiable(boolean global) throws TimeoutException {
        if (!addConstraints()) {
            return false;
        }
        return super.isSatisfiable(global);
    }

    @Override
    public boolean isSatisfiable(IVecInt assumps, boolean global)
            throws TimeoutException {
        if (!addConstraints()) {
            return false;
        }
        return super.isSatisfiable(assumps, global);
    }

    @Override
    public boolean isSatisfiable() throws TimeoutException {
        if (!addConstraints()) {
            return false;
        }
        return super.isSatisfiable();
    }

    @Override
    public boolean isSatisfiable(IVecInt assumps) throws TimeoutException {
        if (!addConstraints()) {
            return false;
        }
        return super.isSatisfiable(assumps);
    }

}
