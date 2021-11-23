/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.List;

/**
 * @author Thibault Falque
 *
 */
public final class NullPBPreprocessing implements PBPreprocessing {
    private static final PBPreprocessing INSTANCE = new NullPBPreprocessing();

    private NullPBPreprocessing() {

    }

    public static PBPreprocessing instance() {
        return INSTANCE;
    }

    @Override
    public List<PBPreprocessingConstraint> preprocess(
            List<PBPreprocessingConstraint> constraints) {
        return constraints;
    }

}
