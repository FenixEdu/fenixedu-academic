package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.research.result.publication.BookRole;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class BookRole2SqlBookRoleConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

        if (source instanceof BookRole) {
            BookRole s = (BookRole) source;
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
            return BookRole.valueOf(src);
        }
        return source;
    }
}
