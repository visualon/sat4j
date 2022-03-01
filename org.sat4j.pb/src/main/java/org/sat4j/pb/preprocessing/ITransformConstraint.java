/**
 * 
 */
package org.sat4j.pb.preprocessing;

import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.ContradictionException;

/**
 * @author Thibault Falque
 *
 */
public interface ITransformConstraint {
    void addConstraintToSolver(IPBSolver solver) throws ContradictionException;
}
