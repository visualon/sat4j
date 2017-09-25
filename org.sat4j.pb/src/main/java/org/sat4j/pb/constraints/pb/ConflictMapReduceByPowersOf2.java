package org.sat4j.pb.constraints.pb;

import org.sat4j.pb.core.PBSolverStats;

public class ConflictMapReduceByPowersOf2 extends ConflictMap {

    // public ConflictMapReduceByPowersOf2(PBConstr cpb, int level) {
    // super(cpb, level);
    // // TODO Auto-generated constructor stub
    // }
    //
    // public ConflictMapReduceByPowersOf2(PBConstr cpb, int level,
    // boolean noRemove) {
    // super(cpb, level, noRemove);
    // // TODO Auto-generated constructor stub
    // }

    public ConflictMapReduceByPowersOf2(PBConstr cpb, int level,
            boolean noRemove, boolean skip, IPostProcess postProcessing,
            PBSolverStats stats) {
        super(cpb, level, noRemove, skip, postProcessing, stats);
        // TODO Auto-generated constructor stub
    }

    // public static IConflict createConflict(PBConstr cpb, int level,
    // PBSolverStats stats) {
    // return new ConflictMapReduceByPowersOf2(cpb, level, true,
    // NoPostProcess.instance(), stats);
    // }

    public static IConflict createConflict(PBConstr cpb, int level,
            boolean noRemove, boolean skip, PBSolverStats stats) {
        return new ConflictMapReduceByPowersOf2(cpb, level, noRemove, skip,
                NoPostProcess.instance(), stats);
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
