/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PartyType2SQLPartyTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {

	if (source instanceof PartyTypeEnum) {
	    PartyTypeEnum s = (PartyTypeEnum) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	
	if(source == null || source.equals("")){
	    return null;
	
	} else if (source instanceof String) {            
	    String src = (String) source;            
	    return PartyTypeEnum.valueOf(src);
	}
	return source;
    }
}
