/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.system.WhenToSendEmail;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaCronScriptStateWhenToSendEmail2CronScriptStateWhenToSendEmailFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof WhenToSendEmail) {
            WhenToSendEmail whenToSendEmail = (WhenToSendEmail) source;
            return whenToSendEmail.name();
        }
        return source;

    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String src = (String) source;
            return WhenToSendEmail.valueOf(src);
        }
        return source;

    }

}
