/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RadioGroupQuestion extends InquiriesQuestion {

    private boolean showValues = false;

    private List<QuestionChoice> choices = new ArrayList<QuestionChoice>();

    private RadioGroupQuestion(String label, boolean showValues, QuestionHeader header) {
	super(label, header);
	this.showValues = showValues;
    }

    public RadioGroupQuestion(String label, int minValue, int maxValue, boolean showValues, QuestionHeader header) {
	this(label, showValues, header);
	for (int i = minValue; i <= maxValue; i++) {
	    addChoice(String.valueOf(i), String.valueOf(i));
	}
    }

    public RadioGroupQuestion(String label, Class enumClass, boolean showValues, QuestionHeader header) {
	this(label, showValues, header);
	for (Enum enumConstant : (Enum[]) enumClass.getEnumConstants()) {
	    addChoice(enumConstant.name(), RenderUtils.getEnumString(enumConstant));
	}
    }

    public RadioGroupQuestion(String label, boolean showValues, String... values) {
	this(label, showValues, (QuestionHeader) null);
	for (String value : values) {
	    addChoice(value, value);
	}
    }

    public RadioGroupQuestion(String label, int minValue, int maxValue, boolean showValues) {
	this(label, minValue, maxValue, showValues, null);
    }

    public RadioGroupQuestion(String label, Class enumClass, boolean showValues) {
	this(label, enumClass, showValues, null);
    }

    private void addChoice(String value, String label) {
	this.choices.add(new QuestionChoice(value, label, this.showValues));
    }

    public boolean isShowValues() {
	return showValues;
    }

    public List<QuestionChoice> getChoices() {
	return choices;
    }

    public Integer getValueAsInteger() {
	try {
	    return getValue() != null ? Integer.valueOf(getValue()) : null;
	} catch (final NumberFormatException ex) {
	    return null;
	}
    }

}
