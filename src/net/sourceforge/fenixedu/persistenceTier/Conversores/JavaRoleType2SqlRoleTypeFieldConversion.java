package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaRoleType2SqlRoleTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof RoleType) {
	    final RoleType roleType = (RoleType) source;
	    return roleType.toString();
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {
	    final String roleTypeString = (String) source;
	    return RoleType.valueOf(roleTypeString);
	}
	return source;
    }

}