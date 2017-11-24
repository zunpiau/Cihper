package misc;

public class StringUtil {

    public static StringBuilder pickup(String s, char low, char up) {
        StringBuilder builder = new StringBuilder ();
        char c;
        for (int i = 0; i < s.length (); i++) {
            c = s.charAt (i);
            if (c >= low && c <= up)
                builder.append (c);
        }
        return builder;
    }

}
