package org.sat4j.pb;

import java.io.PrintWriter;

import org.sat4j.pb.reader.OPBReader2012;
import org.sat4j.pb.tools.CardConstrLearningSolver;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.TimeoutException;

public class CardConstrLearningSolverLauncher {

    private final CardConstrLearningSolver<IPBSolver> solver;

    private final long solverStart;

    /* Temporally launcher ; do not use */
    @Deprecated
    public static void main(String args[]) throws Exception {
        new CardConstrLearningSolverLauncher(args[0]);
    }

    public CardConstrLearningSolverLauncher(String instance) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                long solvingTime = System.currentTimeMillis() - solverStart;
                PrintWriter out = new PrintWriter(System.out, true);
                solver.printStat(out);
                out.println("c solving time: " + solvingTime + "ms");
            }
        });
        solver = new CardConstrLearningSolver<IPBSolver>(
                SolverFactory.newCuttingPlanes());
        Reader reader;
        if (instance.endsWith(".opb") || instance.endsWith(".opb.bz2")) {
            reader = new OPBReader2012(solver);
        } else {
            reader = new DimacsReader(solver);
        }
        System.out.println(solver.toString("c "));
        System.out.println("c reading instance");
        try {
            reader.parseInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        solver.setVerbose(true);
        if (System.getProperties().getProperty("nopreprocessing") != null)
            solver.setPreprocessing(false);
        else if (System.getProperties().getProperty("riss") != null) {
            solver.setPreprocessing(false);
            solver.setRissLocation(System.getProperties().getProperty("riss"));
            solver.setInstance(instance);
        } else
            solver.setPreprocessing(true);
        if (System.getProperties().getProperty("printcards") != null)
            solver.setPrintCards(true);
        else
            solver.setPrintCards(false);
        this.solverStart = System.currentTimeMillis();
        if (System.getProperties().getProperty("printcards") != null) {
            solver.setPrintCards(true);
            preproc(reader);
        } else {
            solver.setPrintCards(false);
            solve(reader);
        }

    }

    private void solve(Reader reader) {
        try {
            if (solver.isSatisfiable()) {
                System.out.println("s SATISFIABLE");
                System.out.print("v ");
                PrintWriter out = new PrintWriter(System.out, true);
                int[] model = solver.model();
                reader.decode(model, out);
                out.flush();
                System.out.println();
            } else {
                System.out.println("s UNSATISFIABLE");
            }
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void preproc(Reader reader) {
        this.solver.init();
    }
}