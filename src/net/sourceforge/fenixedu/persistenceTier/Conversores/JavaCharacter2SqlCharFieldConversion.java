package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class JavaCharacter2SqlCharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof Character) {
			final Character character = (Character) source;
			return character.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
			final String charString = (String) source;
			return charString.charAt(0);
        }
        return source;
    }
}