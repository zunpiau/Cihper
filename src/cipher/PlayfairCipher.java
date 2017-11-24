package cipher;

import misc.StringUtil;

import java.util.HashMap;

public class PlayfairCipher implements Cipher {

    private final char[][] SHEET = {
            {'M', 'O', 'N', 'A', 'R'},
            {'C', 'H', 'Y', 'B', 'D'},
            {'E', 'F', 'G', 'I', 'K'},
            {'L', 'P', 'Q', 'S', 'T'},
            {'U', 'V', 'W', 'X', 'Z'}
    };
    private final static char APPEND_CHAR = 'K';
    private final static char REMAIN_CHAR = 'J';
    private final HashMap<Character, Integer> CHARS_X;
    private final HashMap<Character, Integer> CHARS_Y;
    private Operation encryptOperation;
    private Operation decryptOperation;

    public PlayfairCipher() {
        CHARS_X = new HashMap<> (26);
        CHARS_Y = new HashMap<> (26);
        encryptOperation = (x, y, sameLine) -> {
            if (sameLine) {
                return SHEET[x][(y + 1) % 5];
            } else {
                return SHEET[(x + 1) % 5][y];
            }
        };
        decryptOperation = (x, y, sameLine) -> {
            if (sameLine) {
                return SHEET[x][(y + 4) % 5];
            } else {
                return SHEET[(x + 4) % 5][y];
            }
        };
        buildIndex ();
    }

    private void buildIndex() {
        char[] chars;
        for (int j = 0; j < SHEET.length; j++) {
            chars = SHEET[j];
            for (int k = 0; k < chars.length; k++) {
                CHARS_X.put (chars[k], j);
                CHARS_Y.put (chars[k], k);
            }
        }
        CHARS_X.put (REMAIN_CHAR, CHARS_X.get ('I'));
        CHARS_Y.put (REMAIN_CHAR, CHARS_Y.get ('I'));
    }

    @Override
    public String encrypt(String plain) {
        return crypt (plain, encryptOperation);
    }

    @Override
    public String decrypt(String cipher) {
        return crypt (cipher, decryptOperation);
    }

    private String crypt(String s, Operation operation) {
        StringBuilder builder = StringUtil.pickup (s.toUpperCase (), 'A', 'Z');
        for (int i = 0; i < builder.length () - 1; i += 3) {
            if (builder.charAt (i) == builder.charAt (i + 1))
                builder.insert (i + 1, APPEND_CHAR);
            builder.insert (i + 2, ' ');
        }
        if (builder.length () % 3 != 0)
            builder.append (APPEND_CHAR);

        int x1, y1, x2, y2;
        char char1, char2, charEncrypt1, charEncrypt2;
        for (int i = 0; i < builder.length () - 1; i += 3) {
            char1 = builder.charAt (i);
            char2 = builder.charAt (i + 1);
            x1 = CHARS_X.get (char1);
            y1 = CHARS_Y.get (char1);
            x2 = CHARS_X.get (char2);
            y2 = CHARS_Y.get (char2);
            if (x1 != x2 && y1 != y2) {
                charEncrypt1 = SHEET[x1][y2];
                charEncrypt2 = SHEET[x2][y1];
            } else {
                charEncrypt1 = operation.operation (x1, y1, x1 == x2);
                charEncrypt2 = operation.operation (x2, y2, x1 == x2);
            }
            builder.setCharAt (i, charEncrypt1);
            builder.setCharAt (i + 1, charEncrypt2);
        }
        return builder.toString ();
    }

    @FunctionalInterface
    private interface Operation {

        char operation(int x, int y, boolean sameLine);
    }

}
