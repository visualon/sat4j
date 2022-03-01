/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.List;

/**
 * @author Thibault Falque
 *
 */
public abstract class AbstractPBPreprocessing implements PBPreprocessing {

    private final PBPreprocessing next;

    public AbstractPBPreprocessing(PBPreprocessing next) {
        this.next = next;
    }

    public AbstractPBPreprocessing() {
        this(NullPBPreprocessing.instance());
    }

    @Override
    public List<PBPreprocessingConstraint> preprocess(
            List<PBPreprocessingConstraint> constraints) {
        return next.preprocess(internalPreprocess(constraints));
    }

    protected abstract List<PBPreprocessingConstraint> internalPreprocess(
            List<PBPreprocessingConstraint> constraints);
}
