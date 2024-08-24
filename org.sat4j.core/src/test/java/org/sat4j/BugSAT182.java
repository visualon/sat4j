package org.sat4j;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.constraints.CardinalityDataStructureYanMin;
import org.sat4j.minisat.constraints.MixedDataStructureDanielHT;
import org.sat4j.minisat.core.DataStructureFactory;
import org.sat4j.minisat.core.Solver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.SearchEnumeratorListener;
import org.sat4j.tools.SolutionFoundListener;

public class BugSAT182 {

    @Test
    public void firstIssue() throws ContradictionException, TimeoutException {
        Solver<DataStructureFactory> solver = SolverFactory.newGlucose21();
        solver.setDataStructureFactory(new MixedDataStructureDanielHT());
        solver.addClause(-1, 2);
        SolutionFoundListener sfl = new SolutionFoundListener() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void onSolutionFound(int[] solution) {
                assertNotEquals(0, solution.length);
            }
        };
        SearchEnumeratorListener enumerator = new SearchEnumeratorListener(sfl);
        solver.setSearchListener(enumerator);
        assertTrue(solver.isSatisfiable());
    }

    @Test
    public void secondIssue() throws ContradictionException, TimeoutException {
        Solver<DataStructureFactory> solver = SolverFactory.newGlucose21();
        solver.setDataStructureFactory(new CardinalityDataStructureYanMin());
        solver.addClause(-1, 2);
        SolutionFoundListener sfl = new SolutionFoundListener() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void onSolutionFound(int[] solution) {
                assertNotEquals(0, solution.length);
            }
        };
        SearchEnumeratorListener enumerator = new SearchEnumeratorListener(sfl);
        solver.setSearchListener(enumerator);
        assertTrue(solver.isSatisfiable());
    }
}
