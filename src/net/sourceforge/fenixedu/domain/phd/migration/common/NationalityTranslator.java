package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Country;

public class NationalityTranslator {
    private static final HashMap<Integer, String> translationMap = new HashMap<Integer, String>();

    public static Country translate(String value) {
	return Country.readByTwoLetterCode(translationMap.get(Integer.valueOf(value)));
    }
}
