package org.sat4j.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.Counter;
import org.sat4j.reader.LecteurDimacs;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class BugSAT175 {

    @Test
    public void testWithInternalIterator() throws ParseFormatException,
            IOException, ContradictionException, TimeoutException {
        var satSolver = SolverFactory.newDefault();
        var reader = new LecteurDimacs(satSolver);
        var p = reader.parseInstance("src/test/testfiles/bug175.cnf");
        var counter = new Counter();
        SolutionFoundListener sfl = new SolutionFoundListener() {

            @Override
            public void onSolutionFound(int[] solution) {
                counter.inc();
            }

        };
        var enumerator = new SearchEnumeratorListener(sfl);
        satSolver.setSearchListener(enumerator);
        assertTrue(satSolver.isSatisfiable());
        assertEquals(2, enumerator.getNumberOfSolutionFound());
    }

    @Test
    public void testWithExternalIterator() throws ParseFormatException,
            IOException, ContradictionException, TimeoutException {
        var satSolver = SolverFactory.newDefault();
        var reader = new LecteurDimacs(satSolver);
        var p = reader.parseInstance("src/test/testfiles/bug175.cnf");

        var enumerator = new ModelIterator(satSolver);
        while (enumerator.isSatisfiable()) {
            assertNotNull(enumerator.model());
        }
        assertEquals(2, enumerator.numberOfModelsFoundSoFar());
    }

}
