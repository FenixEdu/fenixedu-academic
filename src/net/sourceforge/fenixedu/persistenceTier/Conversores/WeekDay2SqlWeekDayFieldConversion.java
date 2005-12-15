/**
* Nov 9, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WeekDay2SqlWeekDayFieldConversion implements FieldConversion {

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof WeekDay) {
            final WeekDay weekDay = (WeekDay) obj;
            return weekDay.toString();
        }
        return obj;
    }

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        WeekDay weekDay = null;
        if (obj instanceof String) {
            final String weekDayString = (String) obj;
            return WeekDay.valueOf(weekDayString);
        }
        return weekDay;
    } 
}


