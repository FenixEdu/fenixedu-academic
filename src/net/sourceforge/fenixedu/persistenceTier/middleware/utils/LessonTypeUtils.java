/*
 * Created on 6/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier.middleware.utils;

import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author jpvl
 */
public abstract class LessonTypeUtils {

    public static ShiftType convertLessonType(String type) {
        if (type.equals("Teo"))
            return ShiftType.TEORICA;
        else if (type.equals("Pra"))
            return ShiftType.PRATICA;
        else if (type.equals("Lab"))
            return ShiftType.LABORATORIAL;
        else if (type.equals("TP"))
            return ShiftType.TEORICO_PRATICA;
        else {
            throw new IllegalArgumentException("AULA SEM TIPO DEFINIDO (tipo=" + type + ")");
        }
    }
}