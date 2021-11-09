/**
 * 
 */
package org.sat4j.pb.preprocessing;

import org.sat4j.specs.ISolver;

/**
 * @author Thibault Falque
 *
 */
public interface ITransformConstraint {
    void addConstraintToSolver(ISolver solver);
}
