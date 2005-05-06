/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author Tânia Pousão
 *  
 */

public class JavaExemptionGratuityType2SqlExemptionGratuityTypeFieldConversion implements
        FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof ExemptionGratuityType) {
            ExemptionGratuityType exemptionGratuityType = (ExemptionGratuityType) source;
            return exemptionGratuityType.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return ExemptionGratuityType.valueOf(src);
        }
        return source;

    }

}