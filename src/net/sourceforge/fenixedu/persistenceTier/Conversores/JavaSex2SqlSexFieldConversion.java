package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.person.Sex;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaSex2SqlSexFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof Sex) {
			final Sex sex = (Sex) source;
			return sex.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
			final String sexString = (String) source;
			return Sex.valueOf(sexString);
        }
        return source;
    }
}