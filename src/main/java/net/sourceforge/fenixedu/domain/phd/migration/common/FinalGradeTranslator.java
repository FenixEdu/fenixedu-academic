package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.InvalidFinalGradeException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;

public class FinalGradeTranslator {
    private static final HashMap<String, PhdThesisFinalGrade> translationMap = new HashMap<String, PhdThesisFinalGrade>();

    static {
        // TODO finish Acronyms
        translationMap.put("AP", PhdThesisFinalGrade.PRE_BOLONHA_APPROVED);
        translationMap.put("D", null);
        translationMap.put("DL", PhdThesisFinalGrade.PRE_BOLONHA_APPROVED_WITH_PLUS_PLUS);
        translationMap.put("MB", PhdThesisFinalGrade.APPROVED_WITH_PLUS);
        translationMap.put("MD", PhdThesisFinalGrade.APPROVED_WITH_PLUS_PLUS);
        translationMap.put("RE", PhdThesisFinalGrade.NOT_APPROVED);
        translationMap.put("RP", null);
    }

    public static PhdThesisFinalGrade translate(String key) {
        if (key.equals("RP") || key.equals("D")) {
            throw new InvalidFinalGradeException();
        }

        return translationMap.get(key);

    }
}