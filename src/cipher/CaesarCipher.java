package cipher;

public class CaesarCipher implements Cipher {

    private final Operation encryptOperation;
    private final Operation decryptOperation;
    private int offset;

    public CaesarCipher(int offset) {
        this.offset = offset;
        encryptOperation = c -> (char) ((c - 'a' + this.offset) % 26 + 'A');
        decryptOperation = c -> (char) ((c - 'A' - this.offset + 26) % 26 + 'a');
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String encrypt(String plain) {
        return crypt (plain, 'a', 'z', encryptOperation);
    }

    @Override
    public String decrypt(String cipher) {
        return crypt (cipher, 'A', 'Z', decryptOperation);
    }

    private String crypt(String s, char low, char up, Operation operation) {
        char[] chars = new char[s.length ()];
        s.getChars (0, s.length (), chars, 0);
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= low && chars[i] <= up)) {
                chars[i] = operation.operate (chars[i]);
            }
        }
        return new String (chars);
    }

    private interface Operation {

        char operate(char c);
    }

}
