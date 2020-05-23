package org.sat4j.pb.orders;

import org.sat4j.minisat.core.ILits;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.pb.constraints.pb.PBConstr;

public enum Bumper {

    ANY {
        @Override
        boolean isBumpable(ILits voc, int i) {
            return true;
        }
    },

    ASSIGNED {
        @Override
        boolean isBumpable(ILits voc, int i) {
            return !voc.isUnassigned(i);
        }
    },

    FALSIFIED {
        @Override
        boolean isBumpable(ILits voc, int i) {
            return voc.isFalsified(i);
        }
    };

    public void varBumpActivity(ILits voc, BumpStrategy bumpStrategy,
            IOrder order, PBConstr constr, int i) {
        if (isBumpable(voc, constr.get(i))) {
            bumpStrategy.varBumpActivity(order, constr, i);
        }
    }

    abstract boolean isBumpable(ILits voc, int i);

}
