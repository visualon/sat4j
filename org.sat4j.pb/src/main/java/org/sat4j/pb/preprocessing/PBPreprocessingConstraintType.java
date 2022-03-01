/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.math.BigInteger;

import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * @author Thibault Falque
 *
 */
public enum PBPreprocessingConstraintType {
    LE {

        @Override
        public void addConstraintToSolver(IVecInt literals,
                IVec<BigInteger> coeffs, BigInteger degree, IPBSolver solver)
                throws ContradictionException {
            solver.addAtMost(literals, coeffs, degree);

        }

    },
    GE {

        @Override
        public void addConstraintToSolver(IVecInt literals,
                IVec<BigInteger> coeffs, BigInteger degree, IPBSolver solver)
                throws ContradictionException {
            solver.addAtLeast(literals, coeffs, degree);

        }

    },
    EQ

    {

        @Override
        public void addConstraintToSolver(IVecInt literals,
                IVec<BigInteger> coeffs, BigInteger degree, IPBSolver solver)
                throws ContradictionException {
            solver.addExactly(literals, coeffs, degree);

        }

    };

    public abstract void addConstraintToSolver(IVecInt literals,
            IVec<BigInteger> coeffs, BigInteger degree, IPBSolver solver)
            throws ContradictionException;
}
