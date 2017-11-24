package misc;

/*
* From : https://github.com/PraAnj/Modular-Matrix-Inverse-Java
* */

import java.math.BigInteger;

public class ModMatrix {

    private int nrows;
    private int ncols;
    private BigInteger[][] data;
    private final BigInteger mod = new BigInteger ("26");

    public ModMatrix(double[][] dat) {
        this.data = new BigInteger[dat.length][dat[0].length];
        for (int i = 0; i < dat.length; i++) {
            for (int i1 = 0; i1 < dat[i].length; i1++) {
                this.data[i][i1] = new BigInteger (((int) dat[i][i1]) + "");
            }
        }
        this.nrows = dat.length;
        this.ncols = dat[0].length;
    }

    private ModMatrix(int nrow, int ncol) {
        this.nrows = nrow;
        this.ncols = ncol;
        data = new BigInteger[nrow][ncol];
    }

    private int getNrows() {
        return nrows;
    }

    private int getNcols() {
        return ncols;
    }

    public double[][] getDoubleData() {
        double[][] doubles = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int i1 = 0; i1 < data[i].length; i1++) {
                doubles[i][i1] = data[i][i1].doubleValue ();
            }
        }
        return doubles;
    }

    private BigInteger getValueAt(int i, int j) {
        return data[i][j];
    }

    private void setValueAt(int i, int j, BigInteger value) {
        data[i][j] = value;
    }

    private int size() {
        return ncols;
    }

    private static ModMatrix transpose(ModMatrix matrix) {
        ModMatrix transposedMatrix = new ModMatrix (matrix.getNcols (), matrix.getNrows ());
        for (int i = 0; i < matrix.getNrows (); i++) {
            for (int j = 0; j < matrix.getNcols (); j++) {
                transposedMatrix.setValueAt (j, i, matrix.getValueAt (i, j));
            }
        }
        return transposedMatrix;
    }

    private static BigInteger determinant(ModMatrix matrix) {

        if (matrix.size () == 1) {
            return matrix.getValueAt (0, 0);
        }
        if (matrix.size () == 2) {
            return (matrix.getValueAt (0, 0).multiply (matrix.getValueAt (1, 1)))
                    .subtract ((matrix.getValueAt (0, 1).multiply (matrix.getValueAt (1, 0))));
        }
        BigInteger sum = new BigInteger ("0");
        for (int i = 0; i < matrix.getNcols (); i++) {
            sum = sum.add (
                    changeSign (i).multiply (
                            matrix.getValueAt (0, i).multiply (
                                    determinant (
                                            createSubMatrix (
                                                    matrix, 0, i)))));
        }
        return sum;
    }

    private static BigInteger changeSign(int i) {
        if (i % 2 == 0) {
            return new BigInteger ("1");
        } else {
            return new BigInteger ("-1");
        }
    }

    private static ModMatrix createSubMatrix(ModMatrix matrix, int excluding_row, int excluding_col) {
        ModMatrix mat = new ModMatrix (matrix.getNrows () - 1, matrix.getNcols () - 1);
        int r = -1;
        for (int i = 0; i < matrix.getNrows (); i++) {
            if (i == excluding_row) {
                continue;
            }
            r++;
            int c = -1;
            for (int j = 0; j < matrix.getNcols (); j++) {
                if (j == excluding_col) {
                    continue;
                }
                mat.setValueAt (r, ++c, matrix.getValueAt (i, j));
            }
        }
        return mat;
    }

    private ModMatrix cofactor(ModMatrix matrix) {
        ModMatrix mat = new ModMatrix (matrix.getNrows (), matrix.getNcols ());
        for (int i = 0; i < matrix.getNrows (); i++) {
            for (int j = 0; j < matrix.getNcols (); j++) {
                mat.setValueAt (i, j, (changeSign (i)
                        .multiply (changeSign (j))
                        .multiply (determinant (createSubMatrix (matrix, i, j)))).mod (mod));
            }
        }

        return mat;
    }

    public ModMatrix inverse(ModMatrix matrix) {
        return (transpose (cofactor (matrix)).dc (determinant (matrix)));
    }

    private ModMatrix dc(BigInteger d) {
        BigInteger inv = d.modInverse (mod);
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                data[i][j] = (data[i][j].multiply (inv)).mod (mod);
            }
        }
        return this;
    }
}