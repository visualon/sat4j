package org.sat4j.pb.constraints.pb;

import org.sat4j.pb.core.PBSolverStats;

public class ConflictMapReduceByPowersOf2 extends ConflictMap {

    public ConflictMapReduceByPowersOf2(PBConstr cpb, int level,
            boolean noRemove, boolean skip, IPostProcess postProcessing,
            AutoDivisionStrategy autoDivisionStrategy, PBSolverStats stats) {
        super(cpb, level, noRemove, skip, postProcessing,
                IWeakeningStrategy.UNASSIGNED_FIRST, autoDivisionStrategy,
                stats);
    }

    public static IConflict createConflict(PBConstr cpb, int level,
            boolean noRemove, boolean skip, IPostProcess postprocess,
            IWeakeningStrategy weakeningStrategy,
            AutoDivisionStrategy autoDivisionStrategy, PBSolverStats stats) {
        return new ConflictMapReduceByPowersOf2(cpb, level, noRemove, skip,
                postprocess, autoDivisionStrategy, stats);
    }

    public static IConflictFactory factory() {
        return new IConflictFactory() {
            @Override
            public IConflict createConflict(PBConstr cpb, int level,
                    boolean noRemove, boolean skip, IPostProcess postprocess,
                    IWeakeningStrategy weakeningStrategy,
                    AutoDivisionStrategy autoDivisionStrategy,
                    PBSolverStats stats) {
                return ConflictMapReduceByPowersOf2.createConflict(cpb, level,
                        noRemove, skip, postprocess, weakeningStrategy,
                        autoDivisionStrategy, stats);
            }

            @Override
            public String toString() {
                return "Divide by two the constraint during conflict analysis if all coefficients are even";
            }
        };
    }

    @Override
    void divideCoefs() {
        int nbBits = reduceCoeffsByPower2();
        if (nbBits > 0) {
            stats.numberOfReductionsByPower2++;
            stats.numberOfRightShiftsForCoeffs = stats.numberOfRightShiftsForCoeffs
                    + nbBits;
        }
    }

}
