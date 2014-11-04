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
package org.fenixedu.academic.domain.phd.migration.common;

import java.util.HashMap;

import org.fenixedu.academic.domain.phd.migration.common.exceptions.InvalidFinalGradeException;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;

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