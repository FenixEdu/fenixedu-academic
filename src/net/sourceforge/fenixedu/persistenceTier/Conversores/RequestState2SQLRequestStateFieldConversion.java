/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.RequestState;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class RequestState2SQLRequestStateFieldConversion implements FieldConversion {
    
    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof RequestState) {
            RequestState s = (RequestState) source;
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
            return RequestState.valueOf(src);
        }
        return source;
    }
}
