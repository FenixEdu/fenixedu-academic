package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.GuideSituation;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaGuideSituation2SqlGuideSituationFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof GuideSituation) {
			GuideSituation s = (GuideSituation) source;
			return s.getSituation();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new GuideSituation(src);
		} else {
			return source;
		}
	}

}