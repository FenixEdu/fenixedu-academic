/*
 * JavaTipoCurso2SqlTipoCursoFieldConversion.java
 * 
 * Created on 21 de Novembro de 2002, 15:57
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaTipoCurso2SqlTipoCursoFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof DegreeType) {
            DegreeType s = (DegreeType) source;
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
            return DegreeType.valueOf(src);
        }

        return source;

    }
}