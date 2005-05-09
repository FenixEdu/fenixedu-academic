/*
 * Created on 27/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class JavaAreaType2SqlAreaTypeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof AreaType) {
            AreaType areaType = (AreaType) source;
            return areaType.name();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
           
            String src = (String) source;
            if (src.equals("0"))
                return null;
            return AreaType.valueOf(src);
        }

        return source;

    }

}