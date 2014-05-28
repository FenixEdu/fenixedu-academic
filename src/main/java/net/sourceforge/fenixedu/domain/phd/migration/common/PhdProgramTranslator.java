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
package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.phd.PhdProgram;

public class PhdProgramTranslator {
    private static final HashMap<Integer, String> translationMap = new HashMap<Integer, String>();

    static {
        translationMap.put(1, "DEC");
        translationMap.put(2, "DEM");
        translationMap.put(3, "DEMec");
        translationMap.put(4, "DEEC");
        translationMap.put(5, "DEQuim");
        translationMap.put(6, "DEMM");
        translationMap.put(7, "DEF");
        translationMap.put(8, "DEN");
        translationMap.put(9, "DMat");
        translationMap.put(10, "DEIC");
        translationMap.put(11, "DEGI");
        translationMap.put(20, "DES");
        translationMap.put(25, "Dquim");
        translationMap.put(26, "Dbiotec");
        translationMap.put(27, "DF");
        translationMap.put(28, "DEFT");
        translationMap.put(29, "DPRU");
        translationMap.put(30, "DET");
        translationMap.put(31, "DEAmb");
        translationMap.put(32, "DEAer");
        translationMap.put(33, "DEMat");
        translationMap.put(34, "");
        translationMap.put(35, "DCE");
        translationMap.put(36, "Dtransp");
        translationMap.put(37, "DEBiom");
        translationMap.put(38, "Darq");
        translationMap.put(39, "DEPE");
        translationMap.put(40, "Dgeo");
    }

    public static PhdProgram translate(String value) {
        return PhdProgram.readByAcronym(translationMap.get(Integer.valueOf(value)));
    }
}
