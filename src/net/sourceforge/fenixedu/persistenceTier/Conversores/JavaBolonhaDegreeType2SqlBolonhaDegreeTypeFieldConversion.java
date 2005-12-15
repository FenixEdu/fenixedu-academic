package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaBolonhaDegreeType2SqlBolonhaDegreeTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof BolonhaDegreeType) {
            BolonhaDegreeType s = (BolonhaDegreeType) source;
            return s.toString();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return BolonhaDegreeType.valueOf(src);
        }
        return source;

    }

}
