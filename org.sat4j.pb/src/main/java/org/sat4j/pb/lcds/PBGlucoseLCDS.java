/**
 * 
 */
package org.sat4j.pb.lcds;

import org.sat4j.minisat.core.ConflictTimer;
import org.sat4j.minisat.core.DataStructureFactory;
import org.sat4j.minisat.core.Glucose2LCDS;
import org.sat4j.minisat.core.LearnedConstraintsDeletionStrategy;
import org.sat4j.minisat.core.Solver;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.Constr;

/**
 * @author wallon
 *
 */
public class PBGlucoseLCDS<D extends DataStructureFactory>
        extends Glucose2LCDS<D> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final transient ILBDComputerStrategy lbdStrategy;

    private PBGlucoseLCDS(Solver<D> solver, ConflictTimer timer,
            ILBDComputerStrategy lbdStrategy) {
        super(solver, timer);
        this.lbdStrategy = lbdStrategy;
    }

    public static <D extends DataStructureFactory> LearnedConstraintsDeletionStrategy newIgnoreUnassigned(
            Solver<D> solver, ConflictTimer timer) {
        return new PBGlucoseLCDS<D>(solver, timer,
                new IgnoreUnassignedLiteralsLBDComputerStrategy());
    }

    public static <D extends DataStructureFactory> LearnedConstraintsDeletionStrategy newEffectiveOnly(
            Solver<D> solver, ConflictTimer timer) {
        return new PBGlucoseLCDS<D>(solver, timer,
                new EffectiveLiteralsOnlyLBDComputerStrategy());
    }

    public static <D extends DataStructureFactory> LearnedConstraintsDeletionStrategy newUnassignedDifferent(
            Solver<D> solver, ConflictTimer timer) {
        return new PBGlucoseLCDS<D>(solver, timer,
                new UnassignedLiteralsHaveDifferentLevelLBDComputerStrategy());
    }

    public static <D extends DataStructureFactory> LearnedConstraintsDeletionStrategy newUnassignedSame(
            Solver<D> solver, ConflictTimer timer) {
        return new PBGlucoseLCDS<D>(solver, timer,
                new UnassignedLiteralsHaveSameLevelLBDComputerStrategy());
    }

    public static <D extends DataStructureFactory> LearnedConstraintsDeletionStrategy newDegree(
            Solver<D> solver, ConflictTimer timer) {
        return new PBGlucoseLCDS<D>(solver, timer,
                new DegreeLBDComputerStrategy());
    }

    @Override
    public void init() {
        super.init();
        lbdStrategy.init(solver.nVars());
    }

    @Override
    protected int computeLBD(Constr constr) {
        if (constr instanceof PBConstr) {
            return lbdStrategy.computeLBD(solver.getVocabulary(),
                    (PBConstr) constr);
        }
        return super.computeLBD(constr);
    }

    @Override
    public String toString() {
        return "Glucose LCDS adapted to PB, with LBD computation strategy: "
                + lbdStrategy;
    }

}
