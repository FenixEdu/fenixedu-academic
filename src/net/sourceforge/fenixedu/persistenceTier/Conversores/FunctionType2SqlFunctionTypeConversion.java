/*
 * Created on Oct 3, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class FunctionType2SqlFunctionTypeConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
      
        if (source instanceof FunctionType) {
            FunctionType s = (FunctionType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if(source == null || source.equals("")){
            return null;
        }              
        if (source instanceof String) {
            String src = (String) source;
            return FunctionType.valueOf(src);
        }
        return source;
    }

}
