/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.List;

import org.sat4j.pb.constraints.pb.SubsetSum;

/**
 * @author Thibault Falque
 *
 */
public class MaxDegreePBPreprocessing extends AbstractPBPreprocessing {
    private final SubsetSum subset;

    private static final int MAX_ELEMENT = 100;

    private static final int MAX_SUM = 4000;

    public MaxDegreePBPreprocessing(PBPreprocessing next) {
        super(next);
        this.subset = new SubsetSum(MAX_SUM, MAX_ELEMENT);
    }

    @Override
    protected List<PBPreprocessingConstraint> internalPreprocess(
            List<PBPreprocessingConstraint> constraints) {
        // TODO Auto-generated method stub
        return null;
    }

}
