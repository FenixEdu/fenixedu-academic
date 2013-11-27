package pt.utl.ist.scripts.process.updateData.fixNames;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;

public class DBField2Cap extends CronTask {
    private static DBField2Cap field = new DBField2Cap();

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

        // contracções de artigos com preposições
        allLowerSet.add("ao");
        allLowerSet.add("aos");
        allLowerSet.add("à");
        allLowerSet.add("às");
        allLowerSet.add("do");
        allLowerSet.add("dos");
        allLowerSet.add("da");
        allLowerSet.add("das");
        allLowerSet.add("no");
        allLowerSet.add("nos");
        allLowerSet.add("na");
        allLowerSet.add("nas");

        // conjunções
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

    @Override
    public void runTask() {
        try {
            fixCurricularCourseNames();
            fixExecutionCourseNames();
            fixPersonNames();
        } catch (Exception e) {
            getLogger().error("Unable to run fixNames." + e);
            throw new Error(e);
        }
    }

    // CURRICULAR COURSES
    public static void fixCurricularCourseNames() throws Exception {

        for (final DegreeModule degreeModule : Bennu.getInstance().getDegreeModulesSet()) {
            if (degreeModule.isCurricularCourse()) {
                final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                String capitalizedName = prettyPrint(curricularCourse.getName());
                curricularCourse.setName(capitalizedName);
            }
        }

    }

    // EXECUTION COURSES
    public static void fixExecutionCourseNames() throws Exception {

        for (final ExecutionCourse executionCourse : Bennu.getInstance().getExecutionCoursesSet()) {
            String capitalizedName = prettyPrint(executionCourse.getNome());
            executionCourse.setNome(capitalizedName);
        }

    }

    // PERSON AND PARENTS
    public static void fixPersonNames() throws Exception {

        for (final Party party : Bennu.getInstance().getPartysSet()) {
            if (party.isPerson()) {
                final Person pessoa = (Person) party;
                String capitalizedName = prettyPrint(pessoa.getName());
                pessoa.setName(capitalizedName);
                if (pessoa.getNameOfFather() != null) {
                    String capitalizedNameFather = prettyPrint(pessoa.getNameOfFather());
                    pessoa.setNameOfFather(capitalizedNameFather);
                }
                if (pessoa.getNameOfMother() != null) {
                    String capitalizedNameMother = prettyPrint(pessoa.getNameOfMother());
                    pessoa.setNameOfMother(capitalizedNameMother);
                }
            }
        }

    }

    /**
     * Capitalizes a string that may contain whitespaces and special characters.
     * This method uses <code>capitalizeWord</code> and <code>capitalizeWordWithSpecChars<code>.
     * 
     * @param uglyDuckling the string to capitalize.
     * @return the capitalized string.
     */
    public static String prettyPrint(String uglyDuckling) {
        String[] lowerCaseName = uglyDuckling.toLowerCase().split(" ");
        StringBuilder capitalizedName = new StringBuilder();

        for (int i = 0; i < lowerCaseName.length; i++) {

            if (!containsNoneSpecialChars(lowerCaseName[i]) && !allCapSet.contains(lowerCaseName[i])) {
                capitalizedName.append(capitalizeWordWithSpecChars(lowerCaseName[i]));
            } else {
                // The first word is always capitalized (some courses' name
                // begin with an article)
                // (ex: Os Líseres na Instrumentação e Medida)
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
     * Capitalizes a string that contains special characters. This method uses <code>capitalizeWord</code> to capitalize the
     * substrings between the
     * special characters.
     * 
     * @param uglyWord
     *            the string to capitalize.
     * @return the capitalized string.
     */
    private static String capitalizeWordWithSpecChars(String uglyWord) {
        StringBuilder prettyWord = new StringBuilder();

        int startPos = 0;

        for (int index = indexOfAnySpecChar(uglyWord, 0); index >= 0; index = indexOfAnySpecChar(uglyWord, startPos)) {
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
        StringBuilder prettyWord = new StringBuilder();

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

}
