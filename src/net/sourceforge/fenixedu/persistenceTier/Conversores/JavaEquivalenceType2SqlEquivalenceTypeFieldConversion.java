package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.EnrolmentEquivalenceType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class JavaEquivalenceType2SqlEquivalenceTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof EnrolmentEquivalenceType) {
            EnrolmentEquivalenceType src = (EnrolmentEquivalenceType) source;
            return src.getEquivalenceType();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new EnrolmentEquivalenceType(src);
        }
        return source;

    }

}