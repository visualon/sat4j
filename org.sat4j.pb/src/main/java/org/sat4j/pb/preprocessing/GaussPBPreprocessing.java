/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thibault Falque
 *
 */
public class GaussPBPreprocessing extends AbstractPBPreprocessing {
    private final List<PBPreprocessingConstraintGroup> groups;
    private IConstraintGroupSelectionStrategy strategy;

    /**
     * @param next
     * @param groups
     */
    public GaussPBPreprocessing(PBPreprocessing next) {
        super(next);
        this.groups = new ArrayList<>();
    }

    @Override
    protected List<PBPreprocessingConstraint> internalPreprocess(
            List<PBPreprocessingConstraint> constraints) {
        for (PBPreprocessingConstraint pbCtr : constraints) {
            if (!strategy.add(groups, pbCtr)) {
                PBPreprocessingConstraintGroup group = new PBPreprocessingConstraintGroup();
                group.add(pbCtr);
                groups.add(group);
            }
        }

        return null;

    }

}
