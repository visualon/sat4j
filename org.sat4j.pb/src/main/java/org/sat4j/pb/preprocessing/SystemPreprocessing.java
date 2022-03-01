/**
 * 
 */
package org.sat4j.pb.preprocessing;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sat4j.specs.IteratorInt;

/**
 * @author Thibault Falque
 *
 */
public class SystemPreprocessing {
    private final List<PBPreprocessingConstraint> equations;
    private final Set<Integer> variables = new HashSet<>();

    /**
     * 
     * @param constraints
     */
    public SystemPreprocessing(List<PBPreprocessingConstraint> constraints) {
        this.equations = constraints;
        for (PBPreprocessingConstraint constraint : this.equations) {
            for (IteratorInt it = constraint.getLiterals().iterator(); it
                    .hasNext();) {
                this.variables.add(it.next());
            }
        }
    }

    /**
     * 
     * @return
     */
    public List<PBPreprocessingConstraint> compute() {
        int currentLine = 0;

        for (Integer i : variables) {
            int bestEquation = computeBestEquation(currentLine, i);
            Collections.swap(equations, currentLine, bestEquation);
            applyGaussianElimination(currentLine, i);
            currentLine++;

        }

        return null;
    }

    /**
     * 
     * @param currentLine
     * @param lit
     * @return
     */
    private int computeBestEquation(int currentLine, int lit) {
        int bestEquation = currentLine;
        BigInteger min = equations.get(currentLine).getCoeff(lit);
        for (int i = currentLine + 1; i < equations.size(); i++) {
            BigInteger currentCoeff = equations.get(i).getCoeff(lit);
            if (currentCoeff.equals(BigInteger.ZERO))
                continue;
            if ((min.equals(BigInteger.ZERO))
                    || (min.compareTo(currentCoeff) == 1)) {
                bestEquation = i;
                min = currentCoeff;
            }
        }
        return bestEquation;
    }

    /**
     * 
     * @param currentLine
     * @param v
     */
    void applyGaussianElimination(int currentLine, int v) {
        PBPreprocessingConstraint p = equations.get(currentLine);
        BigInteger c = p.getCoeff(v);
        for (int i = currentLine + 1; i < equations.size(); i++) {
            PBPreprocessingConstraint e = equations.get(i);
            if (e.getCoeff(v).equals(BigInteger.ZERO))
                continue;
            equations.set(i, p.mult(e.getCoeff(v)).sub(e.mult(c)));
        }
    }

}
