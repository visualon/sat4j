/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004, 2019 Artois University and CNRS
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

package org.sat4j.tools.counting;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

import org.sat4j.core.Vec;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVec;
import org.sat4j.tools.SolverDecorator;

/**
 * ApproxMC implements the approximate model counter proposed by Chakraborty,
 * Meel and Vardi.
 * 
 * @author Romain WALLON
 */
public class ApproxMC2 extends SolverDecorator<ISolver> {

    /**
     * The {@code serialVersionUID} of this {@link Serializable} class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default EPSILON value (tolerance) to use as parameter for the
     * algorithm.
     */
    public static final double DEFAULT_EPSILON = .1;

    /**
     * The default DELTA value (confidence) to use as parameter for the
     * algorithm.
     */
    public static final double DEFAULT_DELTA = .1;

    /**
     * The EPSILON parameter for the algorithm, i.e. the tolerance of the count.
     */
    private final double epsilon;

    /**
     * The DELTA parameter for the algorithm, i.e. the confidence of the count.
     */
    private final double delta;

    private final ParityConstraintGenerator generator;

    /**
     * The model counter used to perform an exact count of models in the cells.
     */
    private final IModelCounter counter;

    /**
     * The number of cells that have been found.
     */
    private int nCells;

    /**
     * Creates a new approximate model counter. The tolerance and confidence are
     * the default ones.
     * 
     * @param solver
     *            The solver to use as an oracle.
     * 
     * @see #DEFAULT_EPSILON
     * @see #DEFAULT_DELTA
     * @see #ApproxMC(ISolver, double, double)
     */
    public ApproxMC2(ISolver solver) {
        this(solver, DEFAULT_EPSILON, DEFAULT_DELTA);
    }

    /**
     * Creates a new approximate model counter.
     * 
     * @param solver
     *            The solver to use as an oracle.
     * @param epsilon
     *            The tolerance of the count.
     * @param delta
     *            the confidence of the count.
     */
    public ApproxMC2(ISolver solver, double epsilon, double delta) {
        super(solver);
        this.counter = ModelCounterAdapter.newInstance(solver);
        this.epsilon = epsilon;
        this.delta = delta;
        this.generator = new ParityConstraintGenerator(solver);

    }

    /**
     * Counts an approximation {@code m} of the number of models of the
     * underlying formula {@code F}. This number verifies
     * {@code (#F / (1 + epsilon) <= m <= (1 + epsilon) #F)} with probability
     * {@code (1 - delta)}.
     * 
     * @return An approximate count of the number of solutions.
     */
    public BigInteger countSolutions() {
        int threshold = computeThreshold();
        long y = boundedSAT(threshold);
        if (y < threshold) {
            // The number of models is already exact.
            return BigInteger.valueOf(y);
        }

        nCells = 2;
        int t = computeIterCount();
        IVec<BigInteger> counts = new Vec<BigInteger>(t);

        // Computing the number of models for t random formulae.
        for (int i = 0; i < t; i++) {
            BigInteger count = core(threshold);
            if (count != null) {
                counts.push(count);
            }
        }

        // The approximate count is the median of all counts.
        return findMedian(counts);
    }

    /**
     * Computes the threshold to apply w.r.t. the tolerance wanted for the
     * algorithm.
     * 
     * @return The bound for the number of models to find when invoking the
     *         (bounded) SAT oracle.
     * 
     * @see #boundedSAT(long)
     */
    protected int computeThreshold() {
        return (int) (1 + 9.84 * (1 + (epsilon / (1 + epsilon)))
                * (1 + 1 / epsilon) * (1 + 1 / epsilon));
    }

    /**
     * Computes the number of iterations to perform w.r.t. the confidence wanted
     * for the algorithm.
     * 
     * @return The number of iterations to perform.
     */
    protected int computeIterCount() {
        return (int) (17 * Math.log(3 / delta) / Math.log(2));
    }

    /**
     * Counts an epsilon-approximate estimate of the model count of the
     * underlying formula, by partitioning the space of all the models into
     * "small" cells containing at most {@code pivot} models.
     * 
     * @param thresh
     *            The bound for the number of models in a cell.
     * 
     * @return The estimate of the model count, or {@code null} to report a
     *         counting error, i.e;, when all generated cells were either empty
     *         or too big.
     */
    private BigInteger core(int thresh) {
        generator.generate(nVars());

        // Counting with all parity constraints.
        long count = boundedSAT(thresh);
        generator.deactivate();
        if (count >= thresh) {
            return null;
        }

        // This is equivalent to mPrev = log2(nCells)
        int mPrev = Integer.SIZE - Integer.numberOfLeadingZeros(nCells) - 1;
        int m = logSatSearch(thresh, mPrev);
        BigInteger nSols = BigInteger.valueOf(boundedSAT(m, thresh));

        // Forgetting all parity constraints used in this run.
        generator.clear();

        return nSols.shiftLeft(m);
    }

    /**
     * Performs a logarithmic search for the number of parity constraints to
     * consider.
     * 
     * @param thresh
     *            The maximum number of models to consider.
     * @param mPrev
     *            The previous number of parity constraints that were added.
     * 
     * @return The best number of parity constraints to add.
     */
    private int logSatSearch(int thresh, int mPrev) {
        int loIndex = 0;
        int hiIndex = nVars() - 1;
        int m = mPrev;
        int[] bigCell = new int[nVars()];
        Arrays.fill(bigCell, -1);
        bigCell[0] = 1;
        bigCell[nVars() - 1] = 0;

        for (;;) {
            long y = boundedSAT(m, thresh);
            if (y >= thresh) {
                // There are too many constraints.
                if (bigCell[m + 1] == 0) {
                    return m + 1;
                }
                Arrays.fill(bigCell, 1, m + 1, 1);
                loIndex = m;
                if (Math.abs(m - mPrev) < 3) {
                    m++;
                } else if (m << 1 < nVars() - 1) {
                    m <<= 1;
                } else {
                    m = (hiIndex + m) >> 1;
                }

            } else {
                // There is enough constraints, but maybe too many.
                if (bigCell[m - 1] == 1) {
                    return m;
                }
                Arrays.fill(bigCell, m, nVars(), 0);
                hiIndex = m;
                if (Math.abs(m - mPrev) < 3) {
                    m--;
                } else {
                    m = (m + loIndex) >> 1;
                }
            }
        }
    }

    /**
     * Counts up to {@code bound} model of the current formula, by enumerating
     * these models.
     * 
     * @param bound
     *            The maximum number of models to count.
     * 
     * @return The number of model that have been counted.
     */
    private long boundedSAT(int bound) {
        counter.setBound(bound);
        BigInteger foundModels = counter.countModels();
        counter.reset();
        return foundModels.longValue();
    }

    /**
     * Counts up to {@code bound} model of the current formula, by enumerating
     * these models.
     * 
     * @param m
     *            The number of parity constraints to consider.
     * @param bound
     *            The maximum number of models to count.
     * 
     * @return The number of model that have been counted.
     */
    private long boundedSAT(int m, int bound) {
        generator.activate(m);
        long count = boundedSAT(bound);
        generator.deactivate(m);
        return count;
    }

    /**
     * Computes the median of a vector of values.
     * 
     * @param values
     *            The values to compute the median of.
     * 
     * @return The median of the vector of values, or {@link BigInteger#ZERO} if
     *         the vector is empty (which, in the case of model counting, means
     *         that the formula is unsatisfiable).
     */
    protected static BigInteger findMedian(IVec<BigInteger> values) {
        if (values.isEmpty()) {
            return BigInteger.ZERO;
        }

        // The values need to be sorted to find the median...
        values.sort(new Comparator<BigInteger>() {
            @Override
            public int compare(BigInteger o1, BigInteger o2) {
                return o1.compareTo(o2);
            }
        });

        // ... which is at the middle-th position of the vector.
        return values.get(values.size() >> 1);
    }

}
