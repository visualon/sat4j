package org.sat4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;

public class CLITest {

    @Test
    public void testDecisionModeSAT() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-yes-ok.cnf" };
        launcher.run(args);
        assertEquals(ExitCode.SATISFIABLE, launcher.getExitCode());
    }

    @Test
    public void testDecisionModeUNSAT() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-no-ok.cnf" };
        launcher.run(args);
        assertEquals(ExitCode.UNSATISFIABLE, launcher.getExitCode());
    }

    @Test
    public void testDecisionModeUNSATPROOF() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-no-ok.cnf" };
        System.setProperty("UNSATPROOF", "true");
        launcher.run(args);
        assertEquals(ExitCode.UNSATISFIABLE, launcher.getExitCode());
        assertTrue(new File(args[0] + ".rupproof").exists());
    }

    @Test
    public void testDecisionModeMinOne() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-yes-ok.cnf" };
        System.setProperty("minone", "true");
        launcher.run(args);
        assertEquals(ExitCode.SATISFIABLE, launcher.getExitCode());
    }

    @Test
    public void testDecisionModeAllExternal() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-yes-ok.cnf" };
        System.setProperty("all", "external");
        launcher.run(args);
        assertEquals(ExitCode.SATISFIABLE, launcher.getExitCode());
    }

    @Test
    public void testDecisionModeAllInternal() {
        BasicLauncher<ISolver> launcher = new BasicLauncher<ISolver>(
                SolverFactory.instance());
        String[] args = { "src/test/testfiles/aim-50-yes-ok.cnf" };
        System.setProperty("all", "internal");
        launcher.run(args);
        assertEquals(ExitCode.OPTIMUM_FOUND, launcher.getExitCode());
    }
}
