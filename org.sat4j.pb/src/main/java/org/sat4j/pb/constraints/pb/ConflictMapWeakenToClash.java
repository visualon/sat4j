/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004, 2012 Artois University and CNRS
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 *
 * Based on the original MiniSat specification from:
 *
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 *
 * Contributors:
 *   CRIL - initial API and implementation
 *******************************************************************************/

package org.sat4j.pb.constraints.pb;

import java.math.BigInteger;

import org.sat4j.pb.core.PBSolverStats;

/**
 * 
 * 
 * @author Romain WALLON
 * 
 * @version 0.1.0
 */
public class ConflictMapWeakenToClash extends ConflictMap {

    public ConflictMapWeakenToClash(PBConstr cpb, int level, boolean noRemove,
            SkipStrategy skip, IPreProcess preprocess,
            IPostProcess postProcessing, IWeakeningStrategy weakeningStrategy,
            AutoDivisionStrategy autoDivisionStrategy, PBSolverStats stats) {
        super(cpb, level, noRemove, skip, preprocess, postProcessing,
                weakeningStrategy, autoDivisionStrategy, stats);
    }

    public static IConflict createConflict(PBConstr cpb, int level,
            boolean noRemove, SkipStrategy skip, IPreProcess preprocess,
            IPostProcess postProcessing, IWeakeningStrategy weakeningStrategy,
            AutoDivisionStrategy autoDivisionStrategy, PBSolverStats stats) {
        return new ConflictMapWeakenToClash(cpb, level, noRemove, skip,
                preprocess, postProcessing, weakeningStrategy,
                autoDivisionStrategy, stats);
    }

    public static IConflictFactory factory() {
        return new IConflictFactory() {
            @Override
            public IConflict createConflict(PBConstr cpb, int level,
                    boolean noRemove, SkipStrategy skip, IPreProcess preprocess,
                    IPostProcess postprocess,
                    IWeakeningStrategy weakeningStrategy,
                    AutoDivisionStrategy autoDivisionStrategy,
                    PBSolverStats stats) {
                return ConflictMapWeakenToClash.createConflict(cpb, level,
                        noRemove, skip, preprocess, postprocess,
                        weakeningStrategy, autoDivisionStrategy, stats);
            }

            @Override
            public String toString() {
                return "Weaken reason to find a multiple of the pivot";
            }
        };
    }

    /**
     * Divides the constraint so as to get a coefficient equal to
     * {@code BigInteger#ONE} for {@code litImplied}.
     */
    @Override
    protected BigInteger reduceUntilConflict(int litImplied, int ind,
            BigInteger[] reducedCoefs, BigInteger degreeReduced, IWatchPb wpb) {
        int nLitImplied = litImplied ^ 1;
        BigInteger coefImplied = reducedCoefs[ind];
        BigInteger coefNImplied = weightedLits.get(nLitImplied);
        BigInteger degree = degreeReduced;

        int cmp = coefImplied.compareTo(coefNImplied);
        if (cmp < 0) {
            // The coefficient is bigger in the conflict.
            BigInteger[] res = coefNImplied.divideAndRemainder(coefImplied);
            if (res[1].signum() != 0) {
                BigInteger mult = res[0].add(BigInteger.ONE);
                for (int i = 0; i < reducedCoefs.length; i++) {
                    if (i == ind) {
                        // This coefficient becomes the same coefficient as in
                        // the conflict.
                        // This is an implicit weakening.
                        reducedCoefs[i] = coefNImplied;

                    } else {
                        // This coefficient is simply multiplied.
                        reducedCoefs[i] = reducedCoefs[i].multiply(mult);
                    }
                }

                // Applying the weakening to the degree.
                degree = degree.multiply(mult).subtract(coefImplied)
                        .subtract(res[1]);
                degree = saturation(reducedCoefs, degree, wpb);
            }

        } else if (cmp > 0) {
            // The coefficient is bigger in the reason.
            // It has to be weakened to a multiple of coefNImplied.
            BigInteger modulo = coefImplied.mod(coefNImplied);
            reducedCoefs[ind] = coefImplied.subtract(modulo);
            degree = degree.subtract(modulo);
            degree = saturation(reducedCoefs, degree, wpb);

        } else {
            // In this case, both coefficients are equal.
            // There is nothing to do.
        }

        return super.reduceUntilConflict(litImplied, ind, reducedCoefs, degree,
                wpb);
    }

}
