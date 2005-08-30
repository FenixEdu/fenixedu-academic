/**
* Aug 29, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;


/**
 * @author Ricardo Rodrigues
 *
 */

public class StudentPersonalDataAuthorizationChoice2sqlStudentPersonalDataAuthorizationChoiceFieldConversion implements
        FieldConversion {

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object source) throws ConversionException {
        if ( source instanceof StudentPersonalDataAuthorizationChoice){
            StudentPersonalDataAuthorizationChoice spdaChoice = (StudentPersonalDataAuthorizationChoice) source;
            return spdaChoice.name();
        }
        return source;
    }

    /* (non-Javadoc)
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object source) throws ConversionException {
        if(source instanceof String){
            String spdaChoice = (String) source;
            return StudentPersonalDataAuthorizationChoice.valueOf(spdaChoice);
        }
        return source;
    }

}


