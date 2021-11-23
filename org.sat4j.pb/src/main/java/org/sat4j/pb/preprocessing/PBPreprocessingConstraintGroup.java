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
public class PBPreprocessingConstraintGroup {
    private final List<PBPreprocessingConstraint> lists;

    public PBPreprocessingConstraintGroup() {
        this.lists = new ArrayList<>();
    }

    public void add(PBPreprocessingConstraint constraint) {
        lists.add(constraint);
    }

    /**
     * @return the lists
     */
    public List<PBPreprocessingConstraint> getLists() {
        return lists;
    }

}
