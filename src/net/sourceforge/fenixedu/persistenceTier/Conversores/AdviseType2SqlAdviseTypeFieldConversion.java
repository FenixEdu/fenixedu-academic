/**
* Nov 10, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.teacher.AdviseType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Ricardo Rodrigues
 *
 */

public class AdviseType2SqlAdviseTypeFieldConversion  implements FieldConversion {

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof AdviseType) {
            final AdviseType adviseType = (AdviseType) obj;
            return adviseType.toString();
        }
        return obj;
    }

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        AdviseType adviseType = null;
        if (obj instanceof String) {
            final String adviseTypeString = (String) obj;
            return AdviseType.valueOf(adviseTypeString);
        }
        return adviseType;
    } 
}
