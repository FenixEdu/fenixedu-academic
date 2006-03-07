package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.MultiLanguageString.ExistingLanguageException;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class MultiLanguageString2SqlMultiLanguageStringConversion implements FieldConversion {

	private Integer letterCodeSize = 2;

	public Object javaToSql(Object source) throws ConversionException {
		if (source instanceof MultiLanguageString) {
			return ((MultiLanguageString)source).toString();

		}
		return source;
	}

	public Object sqlToJava(Object source) throws ConversionException {
		if (source instanceof String) {
			String src = (String) source;

			final String[] MLSFromSql = src.split("\001");

			if (MLSFromSql != null) {
				MultiLanguageString multiLanguageString = new MultiLanguageString();

				for (int i = 0; i < MLSFromSql.length; i++) {
					final String language = MLSFromSql[i].substring(0, letterCodeSize);
					final String content = MLSFromSql[i].substring(letterCodeSize);

					try {
						multiLanguageString.addContent(Language.valueOf(language), content);
					} catch (ExistingLanguageException e) {
						e.printStackTrace();
					}
				}
				return multiLanguageString;
			}
			return null;
		}
		return source;
	}
}