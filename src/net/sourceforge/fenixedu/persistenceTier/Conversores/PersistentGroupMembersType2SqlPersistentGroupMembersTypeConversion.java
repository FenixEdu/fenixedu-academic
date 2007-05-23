/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class PersistentGroupMembersType2SqlPersistentGroupMembersTypeConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof PersistentGroupMembersType) {
	    PersistentGroupMembersType s = (PersistentGroupMembersType) source;
	    return s.name();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {
	    String src = (String) source;
	    return PersistentGroupMembersType.valueOf(src);
	}
	return source;
    }
}
