/*
 * Created on 6/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier.middleware.utils;

import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author jpvl
 */
public abstract class LessonTypeUtils {

    public static TipoAula convertLessonType(String type) {
        if (type.equals("Teo"))
            return new TipoAula(TipoAula.TEORICA);
        else if (type.equals("Pra"))
            return new TipoAula(TipoAula.PRATICA);
        else if (type.equals("Lab"))
            return new TipoAula(TipoAula.LABORATORIAL);
        else if (type.equals("TP"))
            return new TipoAula(TipoAula.TEORICO_PRATICA);
        else {
            throw new IllegalArgumentException("AULA SEM TIPO DEFINIDO (tipo=" + type + ")");
        }
    }
}