/*
 * Season2EnumSeasonFieldConversion.java
 *
 * 2003/03/26
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import net.sourceforge.fenixedu.util.Season;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Season2EnumSeasonFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        if (source instanceof Season) {
            Season s = (Season) source;
            return s.getseason();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new Season(src);
        }

        return source;

    }

}