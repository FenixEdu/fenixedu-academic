/*
 * JavaTipoCurso2SqlTipoCursoFieldConversion.java
 * 
 * Created on 21 de Novembro de 2002, 15:57
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCycleType2SqlCycleTypeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof CycleType) {
            CycleType s = (CycleType) source;
            return s.toString();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return CycleType.valueOf(src);
        }

        return source;

    }
}