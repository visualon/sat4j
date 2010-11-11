/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004-2008 Daniel Le Berre
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 * 
 * Based on the original MiniSat specification from:
 * 
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 * 
 *******************************************************************************/
package org.sat4j;

import junit.framework.TestCase;

import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.SingleSolutionDetector;

public class SingleSolutionTest extends TestCase {

	public SingleSolutionTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		solver = LightFactory.instance().defaultSolver();
		detector = new SingleSolutionDetector(solver);
		detector.newVar(3);
	}

	/*
	 * Test method for
	 * 'org.sat4j.tools.SingleSolutionDetector.hasASingleSolution()'
	 */
	public void testHasASingleSolution() throws ContradictionException,
			TimeoutException {
		IVecInt clause = new VecInt();
		clause.push(1).push(2);
		detector.addClause(clause);
		clause.clear();
		clause.push(-1).push(-2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable());
		assertFalse(detector.hasASingleSolution());
	}

	/*
	 * Test method for
	 * 'org.sat4j.tools.SingleSolutionDetector.hasASingleSolution()'
	 */
	public void testHasNoSingleSolution() throws ContradictionException,
			TimeoutException {
		IVecInt clause = new VecInt();
		clause.push(1).push(2);
		detector.addClause(clause);
		clause.clear();
		clause.push(-1).push(-2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable());
		clause.clear();
		clause.push(-1).push(2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable());
		assertTrue(detector.hasASingleSolution());
		clause.clear();
		clause.push(1).push(-2);
		detector.addClause(clause);
		assertFalse(detector.isSatisfiable());
		try {
			assertFalse(detector.hasASingleSolution());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK
		}
	}

	/*
	 * Test method for
	 * 'org.sat4j.tools.SingleSolutionDetector.hasASingleSolution()'
	 */
	public void testHasNoSingleSolutionUNSAT() throws ContradictionException,
			TimeoutException {
		IVecInt clause = new VecInt();
		clause.push(1).push(2);
		detector.addClause(clause);
		clause.clear();
		clause.push(-1).push(-2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable());
		clause.clear();
		clause.push(-1).push(2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable());
		clause.clear();
		clause.push(1).push(-2);
		detector.addClause(clause);
		assertFalse(detector.isSatisfiable());
		try {
			assertFalse(detector.hasASingleSolution());
			fail();
		} catch (UnsupportedOperationException e) {
			// OK
		}
	}

	/*
	 * Test method for
	 * 'org.sat4j.tools.SingleSolutionDetector.hasASingleSolution(IVecInt)'
	 */
	public void testHasASingleSolutionIVecInt() throws ContradictionException,
			TimeoutException {
		IVecInt clause = new VecInt();
		clause.push(1).push(2);
		detector.addClause(clause);
		IVecInt assumptions = new VecInt();
		assumptions.push(1);
		assertTrue(detector.isSatisfiable(assumptions));
		assertFalse(detector.hasASingleSolution(assumptions));
		clause.clear();
		clause.push(-1).push(2);
		detector.addClause(clause);
		assertTrue(detector.isSatisfiable(assumptions));
		assertTrue(detector.hasASingleSolution(assumptions));
		clause.clear();
		clause.push(-1).push(-2);
		detector.addClause(clause);
		assertFalse(detector.isSatisfiable(assumptions));
		try {
			assertFalse(detector.hasASingleSolution(assumptions));
			fail();
		} catch (UnsupportedOperationException e) {
			// OK
		}
	}

	private ISolver solver;

	private SingleSolutionDetector detector;
}
