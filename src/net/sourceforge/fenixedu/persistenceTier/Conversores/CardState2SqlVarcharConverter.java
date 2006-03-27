package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import net.sourceforge.fenixedu.domain.assiduousness.util.CardState;

public class CardState2SqlVarcharConverter implements FieldConversion {
	
	public Object javaToSql(Object source) {
		if (source instanceof CardState) {
			CardState cardState = (CardState) source;
			return cardState.toString();
		}
		return source;
	}
	
	public Object sqlToJava(Object source) {
		if (source instanceof String) {
			return CardState.valueOf((String)source);
		}
		return source;
	}

}
