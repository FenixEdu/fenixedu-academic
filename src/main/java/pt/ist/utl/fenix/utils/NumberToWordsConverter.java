/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.utl.fenix.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberToWordsConverter {

    private static final Logger logger = LoggerFactory.getLogger(NumberToWordsConverter.class);

    static private final String[] ONES = { "zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
            "dez", "onze", "doze", "treze", "catorze", "quinze", "dezasseis", "dezassete", "dezoito", "dezanove" };
    static private final String[] ONES_EN = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "treze", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };

    static private final String[] TENS = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta",
            "noventa", "cem" };
    static private final String[] TENS_EN = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty",
            "ninety", "hundred" };

    static private final String[] HUNDREDS = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos",
            "setecentos", "oitocentos", "novecentos", };
    static private final String[] HUNDREDS_EN = { "", "hundred", "two hundred", "three hundred", "four hundred", "five hundred",
            "six hundred", "seven hundred", "eight hundred", "nine hundred", };

    static private final String[] THOUSAND_SINGLE = { "", "mil", "milhão", "bilião" };
    static private final String[] THOUSAND_EN = { "", "thousand", "million", "billion" };

    static private final String[] THOUSAND_PLURAL = { "", "mil", "milhões", "biliões" };

    static private final String ZERO = ONES[0];
    static private final String ZERO_EN = ONES_EN[0];
    static private final String SEPARATOR = " e ";
    static private final String SEPARATOR_EN = " and ";

    // FIXME: locale not used yet
    static public String convert(final int value) {
        return convert(value, new Locale("pt", "PT"));
    }

    static public String convert(final int value, final Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return (value == 0) ? ZERO_EN : thousandsEn(value);
        }
        return (value == 0) ? ZERO : thousands(value);
    }

    static private String ones(final int value) {
        return ONES[value];
    }

    static private String tens(final int value) {
        return (value < 20) ? ones(value) : TENS[value / 10] + ((value % 10 == 0) ? "" : SEPARATOR + ones(value % 10));
    }

    static private String hundreds(final int value) {
        return (value <= 100) ? tens(value) : HUNDREDS[value / 100] + ((value % 100 == 0) ? "" : SEPARATOR + tens(value % 100));
    }

    static private String thousands(final int value) {
        if (value < 1000) {
            return hundreds(value);
        }

        int quotient = value / 1000;
        int remainder = value % 1000;

        return (quotient == 1) ? THOUSAND_SINGLE[quotient] + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder)) : thousands(
                value, 1);
    }

    static private String thousands(final int value, final int level) {
        if (value < 1000) {
            return hundreds(value);
        }

        int quotient = value / 1000;
        int remainder = value % 1000;
        int nextRemainder = quotient % 1000;

        if (nextRemainder == 0) {
            return thousands(quotient, level + 1) + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder));
        } else if (nextRemainder == 1) {
            return thousands(quotient, level + 1) + " " + THOUSAND_SINGLE[level]
                    + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder));
        } else {
            return thousands(quotient, level + 1) + " " + THOUSAND_PLURAL[level]
                    + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder));
        }
    }

    static private String thousandsEn(final int value) {
        if (value < 1000) {
            return hundredsEn(value);
        }

        int quotient = value / 1000;
        int remainder = value % 1000;

        return (quotient == 1) ? THOUSAND_EN[quotient] + ((remainder == 0) ? "" : SEPARATOR_EN + hundredsEn(remainder)) : thousandsEn(
                value, 1);
    }

    static private String thousandsEn(final int value, final int level) {
        if (value < 1000) {
            return hundredsEn(value);
        }

        int quotient = value / 1000;
        int remainder = value % 1000;
        int nextRemainder = quotient % 1000;

        if (nextRemainder == 0) {
            return thousandsEn(quotient, level + 1) + ((remainder == 0) ? "" : SEPARATOR_EN + hundredsEn(remainder));
        } else if (nextRemainder == 1) {
            return thousandsEn(quotient, level + 1) + " " + THOUSAND_EN[level]
                    + ((remainder == 0) ? "" : SEPARATOR_EN + hundredsEn(remainder));
        } else {
            return thousandsEn(quotient, level + 1) + " " + THOUSAND_EN[level]
                    + ((remainder == 0) ? "" : SEPARATOR_EN + hundredsEn(remainder));
        }
    }

    static private String hundredsEn(final int value) {
        return (value <= 100) ? tensEn(value) : HUNDREDS_EN[value / 100]
                + ((value % 100 == 0) ? "" : SEPARATOR_EN + tensEn(value % 100));
    }

    static private String onesEn(final int value) {
        return ONES_EN[value];
    }

    static private String tensEn(final int value) {
        return (value < 20) ? onesEn(value) : TENS_EN[value / 10] + ((value % 10 == 0) ? "" : onesEn(value % 10));
    }

    public static void main(String[] args) {
        int[] values =
                new int[] { 1, 23, 52, 100, 102, 223, 1000, 1023, 2000, 2453, 9000, 10000, 10001, 11342, 100000, 1000000,
                        2000000, 10000000, 100000000, 1000000000 };

        for (final int value : values) {
            int quotient = value / 1000;
            int remainder = value % 1000;
            int nextRemainder = quotient % 1000;

            logger.info(StringUtils.rightPad("V: " + value, 11, ' ') + "\t");
            logger.info(StringUtils.rightPad("Q: " + quotient, 11, ' '));
            logger.info("\tR: " + remainder);
            logger.info("\tRQ: " + nextRemainder);
            logger.info("\twords: " + convert(value));
        }
    }

}
