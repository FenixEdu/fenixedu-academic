/*
 * Season2EnumSeasonFieldConversion.java
 *
 * 2003/03/26
 */

package ServidorPersistente.Conversores;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.Season;

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