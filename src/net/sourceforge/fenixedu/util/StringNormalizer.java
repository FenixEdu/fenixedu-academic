package net.sourceforge.fenixedu.util;

import sun.text.Normalizer;

public class StringNormalizer {

    public static String normalize(String string) {
	return Normalizer.normalize(string, Normalizer.DECOMP, Normalizer.DONE).replaceAll(
		"[^\\p{ASCII}]", "").toLowerCase();
    }

    public static void normalize(String[] words) {
	for (int i = 0; i < words.length; i++) {
	    words[i] = normalize(words[i]);
	}
    }

    public static String normalizeAndRemoveMinorChars(String string) {
	final String normalizedString = normalize(string);
	final StringBuilder stringBuilder = new StringBuilder();
	for (final char c : normalizedString.toCharArray()) {
	    final int i = c;
	    if (i >= 32 || i == 10 || i == 13) {
		stringBuilder.append(c);
	    }
	}
	return stringBuilder.toString();
    }

}
