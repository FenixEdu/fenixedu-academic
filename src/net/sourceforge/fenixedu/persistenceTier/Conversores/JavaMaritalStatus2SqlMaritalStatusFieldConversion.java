/*
 * JavaMaritalStatus2SqlMaritalStatusFieldConversion.java
 *
 * Created on 14 de Novembro de 2002, 10:12
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.person.MaritalStatus;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author David Santos
 */
public class JavaMaritalStatus2SqlMaritalStatusFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof MaritalStatus) {
            MaritalStatus s = (MaritalStatus) source;
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
            return MaritalStatus.valueOf(src);
        }
        return source;

    }
}