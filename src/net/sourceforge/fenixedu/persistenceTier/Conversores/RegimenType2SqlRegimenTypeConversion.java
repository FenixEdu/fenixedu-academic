/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.ContractRegimenType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class RegimenType2SqlRegimenTypeConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof ContractRegimenType) {
            ContractRegimenType s = (ContractRegimenType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return ContractRegimenType.valueOf(src);
        }
        return source;
    }

}
