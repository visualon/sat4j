/**
 * 
 */
package org.sat4j.pb.orders;

import org.sat4j.minisat.core.IOrder;
import org.sat4j.pb.constraints.pb.PBConstr;

/**
 * @author wallon
 *
 */
public enum BumpStrategy {

    ALWAYS_ONE {
        @Override
        public void varBumpActivity(IOrder order, PBConstr constr, int i) {
            order.updateVar(constr.get(i));
        }
    },

    DEGREE {
        @Override
        public void varBumpActivity(IOrder order, PBConstr constr, int i) {
            double value = constr.getDegree().doubleValue();
            if (value >= Integer.MAX_VALUE) {
                value = Integer.MAX_VALUE;
            }
            order.updateVar(constr.get(i), value);
        }
    },

    COEFFICIENT {
        @Override
        public void varBumpActivity(IOrder order, PBConstr constr, int i) {
            double value = constr.getCoef(i).doubleValue();
            if (value >= Integer.MAX_VALUE) {
                value = Integer.MAX_VALUE;
            }
            order.updateVar(constr.get(i), value);
        }
    },

    RATIO {
        @Override
        public void varBumpActivity(IOrder order, PBConstr constr, int i) {
            double value = constr.getDegree().doubleValue();
            if (value >= Integer.MAX_VALUE) {
                value = Integer.MAX_VALUE;
            } else {
                value /= constr.getCoef(i).doubleValue();
            }
            order.updateVar(constr.get(i), value);
        }
    };

    public abstract void varBumpActivity(IOrder order, PBConstr constr, int i);
}
