package org.sat4j.pb.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.Lbool;
import org.sat4j.specs.RandomAccessModel;

public class VERIPBSearchListener implements PBSearchListener<ISolverService> {

    private static final long serialVersionUID = 1L;

    private StringBuilder conflict;
    private StringBuilder reason;
    private String filename;
    private int nConstraints;
    private boolean foundContradiction;
    private FileWriter fw;

    public VERIPBSearchListener(String problemname) {
        int positionDot;
        if (!(problemname.endsWith(".opb"))) {
            positionDot = problemname.lastIndexOf('.');
            this.filename = problemname.substring(0, positionDot);
        } else {
            this.filename = problemname;
        }
        assert this.filename.endsWith(".opb");
        this.filename = this.filename.replace(".opb", ".pbp");
        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }
        try {
            fw = new FileWriter(this.filename, false);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void assumeFileWriter() {
        if (fw == null) {
            throw new IllegalStateException("Proof file not set");
        }
    }

    @Override
    public void init(ISolverService solverService) {
        this.nConstraints = ((IProblem) solverService).nConstraints();
        this.foundContradiction = false;
        try {
            fw.write("pseudo-Boolean proof version 1.0\n");
            fw.write("f " + this.nConstraints + "\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void assuming(int p) {
    }

    @Override
    public void propagating(int p) {
    }

    @Override
    public void enqueueing(int p, IConstr reason) {
    }

    @Override
    public void backtracking(int p) {
    }

    @Override
    public void adding(int p) {
    }

    private String conversion(IConstr c) {
        BigInteger[] coefs = ((PBConstr) c).getCoefs();
        int[] lits = ((PBConstr) c).getLits();
        String conv = "";
        for (int i = 0; i < lits.length; i++) {
            conv += coefs[i] + " ";
            if (lits[i] % 2 == 1) {
                conv += "~";
            }
            conv += "x" + (lits[i] / 2) + " ";
        }
        return conv + ">=" + c.dump().split(">=")[1];
    }

    @Override
    public void learn(IConstr c) {
        this.nConstraints++;
        if (c != null) {
            c.setId(this.nConstraints);
        } else {
            this.foundContradiction = true;
        }
        try {
            fw.write("p " + this.conflict.toString() + "\n");
            if (c != null) {
                fw.write("e " + this.nConstraints + " " + conversion(c)
                        + " ;\n");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(IConstr c) {
        try {
            fw.write("d " + c.getId() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void conflictFound(IConstr confl, int dlevel, int trailLevel) {
    }

    @Override
    public void conflictFound(int p) {
    }

    @Override
    public void solutionFound(int[] model, RandomAccessModel lazyModel) {
    }

    @Override
    public void beginLoop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void end(Lbool result) {
        if (result.toString().equals("F")) {
            try {
                if (!this.foundContradiction) {
                    fw.write("u >= 1 ;\n");
                    this.nConstraints++;
                }
                fw.write("c " + this.nConstraints + "\n");
                fw.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void restarting() {
    }

    @Override
    public void backjump(int backjumpLevel) {
    }

    @Override
    public void cleaning() {
    }

    @Override
    public void learnUnit(int p) {
        try {
            fw.write("p " + this.conflict.toString() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.nConstraints++;
    }

    @Override
    public void onConflict(PBConstr constr) {
        this.conflict = new StringBuilder("" + constr.getId());
    }

    @Override
    public void withReason(PBConstr constr) {
        if (constr != null) {
            this.reason = new StringBuilder("" + constr.getId());
        }
    }

    @Override
    public void weakenOnReason(int p) {
        this.reason.append(" x" + Math.abs(p)).append(" w");
    }

    @Override
    public void weakenOnReason(BigInteger coeff, int p) {
        this.reason.append(" " + coeff).append(" x" + Math.abs(p)).append(" W");
    }

    @Override
    public void weakenOnConflict(int p) {
        this.conflict.append(" x" + Math.abs(p)).append(" w");
    }

    @Override
    public void weakenOnConflict(BigInteger coeff, int p) {
        this.conflict.append(" " + coeff).append(" x" + Math.abs(p))
                .append(" W");
    }

    @Override
    public void multiplyReason(BigInteger coeff) {
        if (coeff.intValue() > 1) {
            this.reason.append(" " + coeff).append(" *");
        }
    }

    @Override
    public void divideReason(BigInteger coeff) {
        if (coeff.intValue() > 1) {
            this.reason.append(" " + coeff).append(" d");
        }
    }

    @Override
    public void multiplyConflict(BigInteger coeff) {
        if (coeff.intValue() > 1) {
            this.conflict.append(" " + coeff).append(" *");
        }
    }

    @Override
    public void divideConflict(BigInteger coeff) {
        if (coeff.intValue() > 1) {
            this.conflict.append(" " + coeff).append(" d");
        }
    }

    @Override
    public void saturateReason() {
        this.reason.append(" s");
    }

    @Override
    public void saturateConflict() {
        this.conflict.append(" s");
    }

    @Override
    public void addReasonAndConflict() {
        this.conflict.append(" " + this.reason).append(" +");
    }

    @Override
    public String toString() {
        return "VERIPB";
    }

}
