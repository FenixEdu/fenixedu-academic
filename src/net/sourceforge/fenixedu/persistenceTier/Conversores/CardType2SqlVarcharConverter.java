package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.CardType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class CardType2SqlVarcharConverter implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof CardType) {
            CardType cardType = (CardType) source;
            return cardType.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return CardType.valueOf((String) source);
        }
        return source;
    }

}
