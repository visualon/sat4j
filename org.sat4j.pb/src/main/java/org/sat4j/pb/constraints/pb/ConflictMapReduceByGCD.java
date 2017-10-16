package org.sat4j.pb.constraints.pb;

import org.sat4j.pb.core.PBSolverStats;

public class ConflictMapReduceByGCD extends ConflictMap {

    public ConflictMapReduceByGCD(PBConstr cpb, int level) {
        super(cpb, level);
    }

    public ConflictMapReduceByGCD(PBConstr cpb, int level, boolean noRemove) {
        super(cpb, level, noRemove);
    }

    public ConflictMapReduceByGCD(PBConstr cpb, int level, boolean noRemove,
            boolean skip, IPostProcess postProcessing, PBSolverStats stats) {
        super(cpb, level, noRemove, skip, postProcessing, stats);
    }

    public static IConflict createConflict(PBConstr cpb, int level,
            PBSolverStats stats) {
        return new ConflictMapReduceByGCD(cpb, level, true, false,
                NoPostProcess.instance(), stats);
    }

    public static IConflict createConflict(PBConstr cpb, int level,
            boolean noRemove, boolean skip, IPostProcess postprocess,
            PBSolverStats stats) {
        return new ConflictMapReduceByGCD(cpb, level, noRemove, skip,
                postprocess, stats);
    }

    public static IConflictFactory factory() {
        return new IConflictFactory() {
            @Override
            public IConflict createConflict(PBConstr cpb, int level,
                    boolean noRemove, boolean skip, IPostProcess postprocess,
                    PBSolverStats stats) {
                return ConflictMapReduceByGCD.createConflict(cpb, level,
                        noRemove, skip, postprocess, stats);
            }
        };
    }

    @Override
    void divideCoefs() {
        int gcd = reduceCoeffsByGCD();
        if (gcd > 1) {
            stats.numberOfReductionsByGCD++;
        }
    }

}
