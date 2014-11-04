/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.commons.StringNormalizer;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Decomposition of human names in their distinct components: prefix, given names, family names, suffix
 * 
 * @author Pedro Santos (pedro.miguel.santos@tecnico.ulisboa.pt)
 */
public class HumanName {
    private final String prefix;

    private final String givenNames;

    private final String familyNames;

    private final String suffix;

    private static final Set<String> knwonPrefixes = new HashSet<>();

    private static final Set<String> knwonSuffixes = new HashSet<>();

    private static final Set<String> prepositions = new HashSet<>();

    private static String[] capitalizationDelimiters = { " ", "-", "Mc" };

    static {
        // english
        knwonPrefixes.add("mr");
        knwonPrefixes.add("master");
        knwonPrefixes.add("mister");
        knwonPrefixes.add("mrs");
        knwonPrefixes.add("miss");
        knwonPrefixes.add("ms");
        knwonPrefixes.add("rev"); //Reverend
        knwonPrefixes.add("fr"); //Father
        knwonPrefixes.add("dr"); //Doctor
        knwonPrefixes.add("prof"); //Professor

        knwonPrefixes.add("pres"); //President
        knwonPrefixes.add("gov");//Governor
        knwonPrefixes.add("amb");//Ambassador
        knwonPrefixes.add("sen");//Senator
        knwonPrefixes.add("sec");//Secretary

        knwonPrefixes.add("coach");

        knwonPrefixes.add("ofc");//Officer
        knwonPrefixes.add("pvt"); //Private
        knwonPrefixes.add("cpl"); //Corporal
        knwonPrefixes.add("sgt"); //Sargent
        knwonPrefixes.add("maj"); //Major
        knwonPrefixes.add("capt"); //Captain
        knwonPrefixes.add("cmdr"); //Commander
        knwonPrefixes.add("lt"); //Lieutenant
        knwonPrefixes.add("col"); //Colonel
        knwonPrefixes.add("gen"); //General

        //portuguese
        knwonPrefixes.add("sr");
        knwonPrefixes.add("sra");
        knwonPrefixes.add("dr"); //Doutor
        knwonPrefixes.add("prof"); //Professor

        prepositions.add("da");
        prepositions.add("das");
        prepositions.add("do");
        prepositions.add("dos");
        prepositions.add("de");
    }

    /**
     * Construct a {@link HumanName} from its components.
     * 
     * @param prefix Name prefix, or null.
     * @param givenNames Given names, space separated.
     * @param familyNames Family names, space separated.
     * @param suffix Name suffix, or null.
     */
    public HumanName(String prefix, String givenNames, String familyNames, String suffix) {
        super();
        this.prefix = cleanupName(prefix);
        this.givenNames = cleanupName(givenNames);
        this.familyNames = cleanupName(familyNames);
        this.suffix = cleanupName(suffix);
    }

    public static boolean namesMatch(String name, String query) {
        List<String> nameParts =
                Arrays.asList(StringNormalizer.normalizeAndRemoveAccents(name).toLowerCase().trim().split("\\s+"));
        List<String> queryParts =
                Arrays.asList(StringNormalizer.normalizeAndRemoveAccents(query).toLowerCase().trim().split("\\s+"));
        return nameParts.containsAll(queryParts);
    }

    public static HumanName decompose(String fullname, boolean normalizeCapitalization) {
        return decompose(normalizeCapitalization ? nameCapitalization(fullname) : fullname);
    }

    private static HumanName decompose(String fullname) {
        String[] parts = fullname.split("\\s+");
        List<String> prefixes = new ArrayList<>();
        List<String> given = new ArrayList<>();
        List<String> family = new ArrayList<>();
        List<String> sufixes = new ArrayList<>();
        Joiner joiner = Joiner.on(' ');
        int i;
        for (i = 0; i < parts.length && knwonPrefixes.contains(parts[i].toLowerCase().replace('.', ' ').trim()); i++) {
            prefixes.add(parts[i]);
        }
        int j;
        for (j = parts.length - 1; j > 0 && knwonSuffixes.contains(parts[j].toLowerCase().replace('.', ' ').trim()); j--) {
            prefixes.add(parts[j]);
        }
        Collections.reverse(sufixes);
        int givenCount = (j - i + 1 >= 4) ? 2 : 1;
        int k;
        for (k = i; k < parts.length && givenCount > 0; k++) {
            given.add(parts[k]);
            if (!prepositions.contains(parts[k])) {
                givenCount--;
            }
        }
        for (; k <= j; k++) {
            family.add(parts[k]);
        }
        return new HumanName(Strings.emptyToNull(joiner.join(prefixes)), joiner.join(given), joiner.join(family),
                Strings.emptyToNull(joiner.join(sufixes)));
    }

    /**
     * Normalizes the capitalization of the name components, does not change the current
     * 
     * @return a {@link HumanName} instance resulting of the capitalization
     */
    public HumanName normalizeCapitalization() {
        return new HumanName(nameCapitalization(prefix), nameCapitalization(givenNames), nameCapitalization(familyNames),
                nameCapitalization(suffix));
    }

    /**
     * The prefix part of a name, like Mr., Mrs., etc
     * 
     * @return {@link String} with the prefix or null if none
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Given names (forenames).
     * 
     * @return {@link String} with the given names, space separated, trimmed
     */
    public String getGivenNames() {
        return givenNames;
    }

    /**
     * Family names (surnames).
     * 
     * @return {@link String} with the family names, space separated, trimmed
     */
    public String getFamilyNames() {
        return familyNames;
    }

    /**
     * The suffix part of a name, like II, Phd, Jr, etc
     * 
     * @return {@link String} with the suffix or null if none
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Computes the full name from all parts
     * 
     * @return {@link String} with the complete name, trimmed
     */
    public String getFullname() {
        return (prefix != null ? prefix + " " : "") + givenNames + " " + familyNames + (suffix != null ? suffix + " " : "");
    }

    public static String cleanupName(String name) {
        if (name == null) {
            return null;
        }
        return Strings.emptyToNull(CharMatcher.WHITESPACE.trimAndCollapseFrom(name, ' '));
    }

    public static String nameCapitalization(String name) {
        return capitalizeFully(name, prepositions, capitalizationDelimiters);
    }

    public static String capitalizeFully(String str, Set<String> exceptions, final String... delimiters) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        return capitalize(str.toLowerCase(), exceptions, delimiters);
    }

    public static String capitalize(final String str, Set<String> exceptions, final String... delimiters) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        Multimap<Integer, String> exceptionBySize = HashMultimap.create();
        for (String exception : exceptions) {
            exceptionBySize.put(exception.length() + 1, " " + exception);
        }
        final char[] buffer = str.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
            if (isDelimiterEnd(buffer, i, delimiters) && !isException(buffer, i, exceptionBySize)) {
                capitalizeNext = true;
            }
        }
        return new String(buffer);
    }

    private static boolean isDelimiterEnd(char[] buffer, int i, String[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(buffer[i]);
        }
        for (final String delimiter : delimiters) {
            if (i - delimiter.length() + 1 < 0) {
                continue;
            }
            if (delimiter.equals(String.valueOf(buffer, i - delimiter.length() + 1, delimiter.length()))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isException(char[] buffer, int i, Multimap<Integer, String> exceptionBySize) {
        for (Integer size : exceptionBySize.keySet()) {
            if (i + size > buffer.length) {
                continue;
            }
            if (exceptionBySize.get(size).contains(String.valueOf(buffer, i, size))) {
                return true;
            }
        }
        return false;
    }
}
