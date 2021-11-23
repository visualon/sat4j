/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.util.List;

/**
 * @author Thibault Falque
 *
 */
public interface PBPreprocessing {
    List<PBPreprocessingConstraint> preprocess(
            List<PBPreprocessingConstraint> constraints);
}
