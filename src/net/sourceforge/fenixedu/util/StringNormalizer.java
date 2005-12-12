package net.sourceforge.fenixedu.util;

import sun.text.Normalizer;

public class StringNormalizer {

    public static String normalize(String string) {
        return Normalizer.normalize(string, Normalizer.DECOMP, Normalizer.DONE).replaceAll("[^\\p{ASCII}]", "");
    }

}
