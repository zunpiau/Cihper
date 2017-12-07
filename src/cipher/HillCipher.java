package cipher;

import misc.ModMatrix;
import misc.StringUtil;

public class HillCipher implements Cipher {

    private int[][] key;
    private int[][] inverse;
    private String keyStr;

    public HillCipher(String key) {
        this.key = new int[3][3];
        setKey (key);
    }

    public String getKey() {
        return keyStr;
    }

    public void setKey(String key) {
        try {
            String[] s = key.split (",");
            for (int i = 0; i < 9; i++) {
                this.key[i / 3][i % 3] = Integer.parseInt (s[i]);
            }
            ModMatrix modMatrix = new ModMatrix (this.key);
            inverse = modMatrix.inverse (modMatrix).getIntegerData ();
            keyStr = key;
        } catch (NumberFormatException | ArithmeticException e) {
            setKey (keyStr);
            throw new IllegalArgumentException ("Illegal key");
        }

    }

    @Override
    public String encrypt(String plain) {
        return crypt (plain, key);
    }

    @Override
    public String decrypt(String cipher) {
        return crypt (cipher, inverse).toLowerCase ();
    }

    private String crypt(String s, int[][] matrix) {
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
                            new int[]{builder.charAt (i) - 'A',
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

    private char[] toChars(int[] ints) {
        char[] chars = new char[ints.length];
        for (int i = 0; i < ints.length; i++) {
            chars[i] = (char) (ints[i] % 26 + 'A');
        }
        return chars;
    }

    private int[] matrixMultiply(int[] column, int[][] matrix) {
        int[] result = new int[column.length];
        for (int i = 0; i < column.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i] += column[j] * matrix[i][j];
            }
        }
        return result;
    }

}
