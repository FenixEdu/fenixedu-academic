package net.sourceforge.fenixedu.domain.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class QuestionScale implements Serializable {

    private MultiLanguageString[] scale;
    private String[] scaleValues;
    private final static String SCALE_KEY = "qs$";
    private final static String SCALE_VALUE_SEPARATOR = "_#_";

    private static final long serialVersionUID = 1L;

    public QuestionScale(MultiLanguageString[] scale, String[] values) {
	if (scale.length != values.length) {
	    throw new DomainException("error.questionChoice.scaleDontMatchValues");
	}
	setScale(scale);
	setScaleValues(values);
    }

    public int getScaleLength() {
	return getScale() != null ? getScale().length : 0;
    }

    public String exportAsString() {
	final StringBuilder result = new StringBuilder();
	for (int iter = 0; iter < getScale().length; iter++) {
	    final String scaleItem = getScale()[iter].exportAsString();
	    final String scaleValue = getScaleValues()[iter];
	    result.append(SCALE_KEY);
	    result.append(scaleItem.length() + scaleValue.length() + SCALE_VALUE_SEPARATOR.length());
	    result.append(':');
	    result.append(scaleValue);
	    result.append(SCALE_VALUE_SEPARATOR);
	    result.append(scaleItem);
	}
	return result.toString();
    }

    public static QuestionScale importFromString(String string) {
	if (string == null) {
	    return null;
	}

	List<MultiLanguageString> scalePortions = new ArrayList<MultiLanguageString>();
	List<String> valuePortions = new ArrayList<String>();
	for (int iter = 0; iter < string.length();) {
	    int length = 0;
	    int collonPosition = string.indexOf(':', iter + 3);

	    if (StringUtils.isNumeric(string.substring(iter + 3, collonPosition))) {
		length = Integer.parseInt(string.substring(iter + 3, collonPosition));
		String scalePortion = string.substring(collonPosition + 1, collonPosition + 1 + length);
		int index = scalePortion.indexOf(SCALE_VALUE_SEPARATOR);
		String value = scalePortion.substring(0, index);
		String scale = scalePortion.substring(index + 3);
		valuePortions.add(valuePortions.size(), value);
		scalePortions.add(scalePortions.size(), MultiLanguageString.importFromString(scale));
	    }
	    iter = collonPosition + 1 + length;
	}

	return new QuestionScale(scalePortions.toArray(new MultiLanguageString[] {}), valuePortions.toArray(new String[] {}));
    }

    @Override
    public String toString() {
	StringBuilder string = new StringBuilder();
	for (int iter = 0; iter < getScaleLength(); iter++) {
	    string.append(getScale()[iter].toString());
	    string.append(" -#- ");
	}
	return string.toString();
    }

    public MultiLanguageString[] getScale() {
	return scale;
    }

    public void setScale(MultiLanguageString[] scale) {
	this.scale = scale;
    }

    public void setScaleValues(String[] scaleValues) {
	this.scaleValues = scaleValues;
    }

    public String[] getScaleValues() {
	return scaleValues;
    }

    public String getLabelByValue(String scaleValue) {
	for (int iter = 0; iter < getScaleValues().length; iter++) {
	    if (scaleValue.equals(getScaleValues()[iter])) {
		return getScale()[iter].toString();
	    }
	}
	return null;
    }
}
