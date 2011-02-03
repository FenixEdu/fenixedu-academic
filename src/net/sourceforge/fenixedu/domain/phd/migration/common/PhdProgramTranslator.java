package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.phd.PhdProgram;

public class PhdProgramTranslator {
    private static final HashMap<Integer, String> translationMap = new HashMap<Integer, String>();

    static {
	// TODO fetch missing acronyms
	translationMap.put(1, "DEC");
	translationMap.put(2, "");
	translationMap.put(3, "DEMec");
	translationMap.put(4, "DEEC");
	translationMap.put(5, "DEQuim");
	translationMap.put(6, "");
	translationMap.put(7, "");
	translationMap.put(8, "DEN");
	translationMap.put(9, "DMat");
	translationMap.put(10, "DEIC");
	translationMap.put(11, "");
	translationMap.put(20, "DES");
	translationMap.put(25, "Dquim");
	translationMap.put(26, "Dbiotec");
	translationMap.put(27, "DF");
	translationMap.put(28, "DEFT");
	translationMap.put(29, "");
	translationMap.put(30, "DET");
	translationMap.put(31, "DEAmb");
	translationMap.put(32, "DEAer");
	translationMap.put(33, "DEMat");
	translationMap.put(34, "");
	translationMap.put(35, "");
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
