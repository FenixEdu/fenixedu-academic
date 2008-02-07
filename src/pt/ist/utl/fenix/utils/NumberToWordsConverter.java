package pt.ist.utl.fenix.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class NumberToWordsConverter {

    static private final String[] ONES = { "zero" , "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "catorze", "quinze", "dezasseis", "dezassete", "dezoito", "dezanove" };
    static private final String[] TENS = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa", "cem" };
    static private final String[] HUNDREDS = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos", };

    static private final String[] THOUSAND_SINGLE = { "", "mil", "milhão", "bilião" };
    static private final String[] THOUSAND_PLURAL = { "", "mil", "milhões", "biliões" };

    static private final String ZERO = ONES[0];
    static private final String SEPARATOR = " e ";

    //TODO: locale not used yet
    static public String convert(final int value) {
	return convert(value, new Locale("pt", "PT"));
    }

    static public String convert(final int value, final Locale locale) {
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

	return (quotient == 1) ? THOUSAND_SINGLE[quotient] + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder)) : thousands(value, 1);
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
	    return thousands(quotient, level + 1) + " " + THOUSAND_SINGLE[level] + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder));
	} else {
	    return thousands(quotient, level + 1) + " " + THOUSAND_PLURAL[level] + ((remainder == 0) ? "" : SEPARATOR + hundreds(remainder));
	}
    }

    public static void main(String[] args) {
	int [] values = new int [] {1, 23, 52, 100, 102, 223, 1000, 1023, 2000, 2453, 9000, 10000, 10001, 11342, 100000, 1000000, 2000000, 10000000, 100000000, 1000000000};

	for (final int value : values) {
	    int quotient = value / 1000;
	    int remainder = value % 1000;
	    int nextRemainder = quotient % 1000;
	    
	    System.out.print(StringUtils.rightPad("V: " + value, 11, ' ') + "\t");
	    System.out.print(StringUtils.rightPad("Q: " + quotient, 11, ' '));
	    System.out.print("\tR: " + remainder);
	    System.out.print("\tRQ: " + nextRemainder);
	    System.out.println("\twords: " + convert(value));
	}
    }

}
