/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.List;

/**
 * @author Thibault Falque
 *
 */
public interface IConstraintGroupSelectionStrategy {
    boolean add(PBPreprocessingConstraintGroup group,
            PBPreprocessingConstraint constraint);

    default boolean add(List<PBPreprocessingConstraintGroup> groups,
            PBPreprocessingConstraint constraint) {
        boolean added = false;

        for (PBPreprocessingConstraintGroup group : groups) {
            if (add(group, constraint)) {
                added = true;
            }
        }
        return added;
    }
}
