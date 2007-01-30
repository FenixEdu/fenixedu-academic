/**
 * 
 */
package net.sourceforge.fenixedu.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * 
 * @author Carla Penedo (carla.penedo@ist.utl.pt)
 * @author Luis Cruz
 * @author Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StringFormatter {

    private static String specialChars = "/:-,.()'+";

    private static Set<String> allLowerSet = new HashSet<String>();

    private static Set<String> allCapSet = new HashSet<String>();

    static {
        // PT
        // artigos
        allLowerSet.add("a");
        allLowerSet.add("as");
        allLowerSet.add("o");
        allLowerSet.add("os");
        allLowerSet.add("um");
        allLowerSet.add("uns");
        allLowerSet.add("uma");
        allLowerSet.add("umas");

        // preposicoes
        allLowerSet.add("com");
        allLowerSet.add("de");
        allLowerSet.add("em");
        allLowerSet.add("para");
        allLowerSet.add("por");
        allLowerSet.add("sobre");

        // contrac��es de artigos com preposi��es
        allLowerSet.add("ao");
        allLowerSet.add("aos");
        allLowerSet.add("�");
        allLowerSet.add("�s");
        allLowerSet.add("do");
        allLowerSet.add("dos");
        allLowerSet.add("da");
        allLowerSet.add("das");
        allLowerSet.add("no");
        allLowerSet.add("nos");
        allLowerSet.add("na");
        allLowerSet.add("nas");

        // conjun��es
        allLowerSet.add("e");

        // EN
        allLowerSet.add("and");
        allLowerSet.add("at");
        allLowerSet.add("by");
        allLowerSet.add("for");
        allLowerSet.add("in");
        allLowerSet.add("of");
        allLowerSet.add("on");
        allLowerSet.add("the");
        allLowerSet.add("to");
        allLowerSet.add("with");

        // FR
        allLowerSet.add("au");
        allLowerSet.add("dans");
        allLowerSet.add("des");
        allLowerSet.add("du");
        allLowerSet.add("en");
        allLowerSet.add("et");
        allLowerSet.add("la");
        allLowerSet.add("le");
        allLowerSet.add("un");
        allLowerSet.add("une");
        allLowerSet.add("par");
        allLowerSet.add("d");
        allLowerSet.add("l");

        // GERM
        allLowerSet.add("der");
        allLowerSet.add("und");

        // IT
        allLowerSet.add("dei");
        allLowerSet.add("degli");
        allLowerSet.add("del");
        allLowerSet.add("dell");
        allLowerSet.add("delle");
        allLowerSet.add("di");
        allLowerSet.add("ed");
        allLowerSet.add("nei");
        allLowerSet.add("nel");

        // SP
        allLowerSet.add("y");
    }

    static {
        allCapSet.add("i");
        allCapSet.add("i");
        allCapSet.add("ii");
        allCapSet.add("iii");
        allCapSet.add("iv");
        allCapSet.add("v");
        allCapSet.add("vi");
        allCapSet.add("vii");
        allCapSet.add("viii");
        allCapSet.add("ix");
        allCapSet.add("x");

        allCapSet.add("b");
        allCapSet.add("c");
        allCapSet.add("a)");
        allCapSet.add("b)");
        allCapSet.add("c)");
        allCapSet.add("d)");
        allCapSet.add("e)");
        allCapSet.add("3b");
        allCapSet.add("3d");
        allCapSet.add("4a");
        allCapSet.add("m");
        allCapSet.add("d");
        allCapSet.add("(m)");
        allCapSet.add("(d)");
        allCapSet.add("lm");
        allCapSet.add("ml");
        allCapSet.add("md");
        allCapSet.add("dm");
        allCapSet.add("l/m");
        allCapSet.add("m/l");
        allCapSet.add("m/d");
        allCapSet.add("d/m");
        allCapSet.add("(lm)");
        allCapSet.add("(ml)");
        allCapSet.add("(md)");
        allCapSet.add("(dm)");
        allCapSet.add("(l/m)");
        allCapSet.add("(m/l)");
        allCapSet.add("(m/d)");
        allCapSet.add("(d/m)");

        allCapSet.add("(a)");
        allCapSet.add("(p)");
        allCapSet.add("(sie)");
        allCapSet.add("(sm)");
        allCapSet.add("(cad)");
        allCapSet.add("cad/cam");
        allCapSet.add("ic"); // ex: IC Design for Testability
    }

    /**
     * Capitalizes a string that may contain whitespaces and special characters.
     * This method uses <code>capitalizeWord</code> and
     * <code>capitalizeWordWithSpecChars<code>.
     *
     * @param   uglyDuckling    the string to capitalize.
     * @return  the capitalized string.
     */
    public static String prettyPrint(String uglyDuckling) {
        uglyDuckling = removeDuplicateSpaces(uglyDuckling.trim());
        String[] lowerCaseName = uglyDuckling.toLowerCase().split(" ");
        StringBuffer capitalizedName = new StringBuffer();

        for (int i = 0; i < lowerCaseName.length; i++) {

            if (!containsNoneSpecialChars(lowerCaseName[i]) && !allCapSet.contains(lowerCaseName[i])) {
                capitalizedName.append(capitalizeWordWithSpecChars(lowerCaseName[i]));
            } else {
                // The first word is always capitalized (some courses' name
                // begin with an article)
                // (ex: Os L�seres na Instrumenta��o e Medida)
                if (i == 0) {
                    capitalizedName.append(WordUtils.capitalize(lowerCaseName[i]));
                } else {
                    // Exception to the general case: if "A" is the last word
                    // converts to UPPERCASE
                    // (needed for courses that occur in alternative semesters)
                    if (i == (lowerCaseName.length - 1) & lowerCaseName[i].equals("a")) {
                        capitalizedName.append(lowerCaseName[i].toUpperCase());
                    } else {
                        capitalizedName.append(capitalizeWord(lowerCaseName[i]));
                    }
                }
            }

            capitalizedName.append(" ");
        }

        return capitalizedName.toString().substring(0, capitalizedName.length() - 1);
    }

    /**
     * Capitalizes a string that contains special characters. This method uses
     * <code>capitalizeWord</code> to capitalize the substrings between the
     * special characters.
     * 
     * @param uglyWord
     *            the string to capitalize.
     * @return the capitalized string.
     */
    private static String capitalizeWordWithSpecChars(String uglyWord) {
        StringBuffer prettyWord = new StringBuffer();

        int startPos = 0;

        for (int index = indexOfAnySpecChar(uglyWord, 0); index >= 0; index = indexOfAnySpecChar(
                uglyWord, startPos)) {
            prettyWord.append(capitalizeWord(uglyWord.substring(startPos, startPos + index)));
            prettyWord.append(uglyWord.substring(startPos + index, startPos + index + 1));
            startPos += (index + 1);

            if (containsNoneSpecialChars(uglyWord.substring(startPos))) {
                prettyWord.append(capitalizeWord(uglyWord.substring(startPos)));
            }
        }

        return prettyWord.toString();
    }

    /**
     * Capitalizes a string according to its type. The first letter is changed
     * to title case (no other letters are changed) if the string isn't supposed
     * to be in upper (e.g.: roman numeration) or lower case (e.g.: articles and
     * prepositions).
     * 
     * @param uglyWord
     *            the string to capitalize
     * @return the capitalized word
     */
    private static String capitalizeWord(String uglyWord) {
        StringBuffer prettyWord = new StringBuffer();

        if (allCapSet.contains(uglyWord)) {
            prettyWord.append(uglyWord.toUpperCase());
        } else {
            if (allLowerSet.contains(uglyWord)) {
                prettyWord.append(uglyWord);
            } else {
                prettyWord.append(WordUtils.capitalize(uglyWord));
            }
        }
        return prettyWord.toString();
    }

    /**
     * Checks that a string does not contain special characters (only
     * alphanumeric ones).
     * 
     * @param string
     *            the string to check
     * @return <code>true</code> if the strings contains a special character
     */
    private static boolean containsNoneSpecialChars(String string) {
        return StringUtils.containsNone(string, specialChars);
    }

    /**
     * Finds the first index of a special character within the given string
     * after a start position.
     * 
     * @param string
     *            the string to check
     * @param startPos
     *            the position to start from
     * @return the first index of a special character
     */
    private static int indexOfAnySpecChar(String string, int startPos) {
        return StringUtils.indexOfAny(string.substring(startPos), specialChars);
    }

    /**
     * 
     * @param string
     * @return
     */
    public static String normalize(String string) {
        String spacesReplacedString = removeDuplicateSpaces(string.trim());
        return StringNormalizer.normalize(spacesReplacedString).toLowerCase();
    }

    private static String removeDuplicateSpaces(String string) {
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll(" ");
    }

    /**
     * 
     * @param string
     * @return
     */
    public static String splitCamelCaseString(String string) {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (char c : string.toCharArray()) {
            if (first) {
                first = false;
            } else if (Character.isUpperCase(c)) {
                result.append(' ');
            }
            result.append(c);
        }

        return result.toString();
    }

    /**
     * 
     * @param string
     * @return
     */
    public static String convertToDBStyle(String string) {
        return splitCamelCaseString(string).replace(' ', '_').toUpperCase();
    }

}
