/*
 * JavaEspecializacao2SqlEspecializacaoFieldConversion.java
 *
 * Created on 18 de Novembro de 2002, 17:37
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaSpecialization2SqlSpecializationFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Specialization) {
            Specialization s = (Specialization) source;
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
            return Specialization.valueOf(src);
        }

        return source;

    }
}