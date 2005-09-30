package pt.utl.ist.util;

public class StringAppender {
    public static final String append(final String string1, final String string2,
            final String... strings) {
        final StringBuilder builder = new StringBuilder();
        builder.append(string1);
        builder.append(string2);
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

}