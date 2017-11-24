package cipher;

import misc.ModMatrix;
import misc.StringUtil;

public class HillCipher implements Cipher {

    private final double[][] keys = new double[][]{
            {17, 17, 5},
            {21, 18, 21},
            {2, 2, 19}
    };
    private double[][] inverse;

    public HillCipher() {
        ModMatrix modMatrix = new ModMatrix (keys);
        inverse = modMatrix.inverse (modMatrix).getDoubleData ();
    }

    @Override
    public String encrypt(String plain) {
        return crypt (plain, keys);
    }

    @Override
    public String decrypt(String cipher) {
        return crypt (cipher, inverse).toLowerCase ();
    }

    private String crypt(String s, double[][] matrix) {
        StringBuilder builder = StringUtil.pickup (s.toUpperCase (), 'A', 'Z');
        int mod = builder.length () % 3;
        if (mod == 1)
            builder.append ("XX");
        else if (mod == 2)
            builder.append ('X');
        char[] group;
        for (int i = 0; i < builder.length () - 2; i += 4) {
            group = toChars (
                    matrixMultiply (
                            new double[]{builder.charAt (i) - 'A',
                                    builder.charAt (i + 1) - 'A',
                                    builder.charAt (i + 2) - 'A'},
                            matrix));
            for (int j = 0; j < group.length; j++) {
                builder.setCharAt (i + j, group[j]);
            }
            builder.insert (i + 3, ' ');
        }
        return builder.toString ();
    }

    private char[] toChars(double[] ints) {
        char[] chars = new char[ints.length];
        for (int i = 0; i < ints.length; i++) {
            chars[i] = (char) (ints[i] % 26 + 'A');
        }
        return chars;
    }

    private double[] matrixMultiply(double[] column, double[][] matrix) {
        double[] result = new double[column.length];
        for (int i = 0; i < column.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i] += column[j] * matrix[i][j];
            }
        }
        return result;
    }

}
