package org.sat4j.pb.constraints.pb;

import org.sat4j.pb.core.PBSolverStats;

public interface IConflictFactory {

    IConflict createConflict(PBConstr cpb, int level, boolean noRemove,
            boolean skip, IPostProcess postprocess,
            IWeakeningStrategy removeStrategy,
            AutoDivisionStrategy autoDivisionStrategy, PBSolverStats stats);
}
