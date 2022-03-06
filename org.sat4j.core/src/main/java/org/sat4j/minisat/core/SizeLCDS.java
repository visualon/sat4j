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
package org.sat4j.minisat.core;

import java.util.Comparator;

import org.sat4j.annotations.Feature;
import org.sat4j.specs.Constr;
import org.sat4j.specs.IVec;

@Feature(value = "deletion", parent = "expert")
final class SizeLCDS implements LearnedConstraintsDeletionStrategy {
    private static final long serialVersionUID = 1L;
    private final ConflictTimer timer;
    private final Solver<? extends DataStructureFactory> solver;

    private static final Comparator<Constr> comparator = new SizeComparator();

    SizeLCDS(Solver<? extends DataStructureFactory> solver,
            ConflictTimer timer) {
        this.timer = timer;
        this.solver = solver;
    }

    public void reduce(IVec<Constr> learnedConstrs) {
        learnedConstrs.sort(comparator);
        int i, j;
        for (i = j = learnedConstrs.size() / 2; i < learnedConstrs
                .size(); i++) {
            var c = learnedConstrs.get(i);
            if (c.locked() || c.size() == 2) {
                learnedConstrs.set(j++, learnedConstrs.get(i));
            } else {
                c.remove(solver);
                solver.slistener.delete(c);
            }
        }
        if (solver.isVerbose()) {
            solver.out.log(solver.getLogPrefix() + "cleaning " //$NON-NLS-1$
                    + (learnedConstrs.size() - j) + " clauses out of " //$NON-NLS-1$
                    + learnedConstrs.size());
        }
        learnedConstrs.shrinkTo(j);
    }

    public ConflictTimer getTimer() {
        return this.timer;
    }

    @Override
    public String toString() {
        return "Sized based learned constraints deletion strategy with timer "
                + timer;
    }

    public void init() {
        // do nothing
    }

    public void onClauseLearning(Constr constr) {
        // do nothing

    }

    public void onConflictAnalysis(Constr reason) {
        if (reason.learnt()) {
            solver.claBumpActivity(reason);
        }
    }

    public void onPropagation(Constr from, int propagated) {
        // do nothing
    }
}