/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AccountabilityType2SQLAccountabilityTypeFieldConversion implements FieldConversion {
    
    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof AccountabilityTypeEnum) {
            AccountabilityTypeEnum s = (AccountabilityTypeEnum) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
          if(source == null || source.equals("")){
              return null;
          }            
          else if (source instanceof String) {            
            String src = (String) source;            
            return AccountabilityTypeEnum.valueOf(src);
        }
        return source;
    }
}
