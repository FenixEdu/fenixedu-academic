/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.BundleUtil;

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
            addChoice(enumConstant.name(),
                    BundleUtil.getStringFromResourceBundle("resources.EnumerationResources", enumConstant.name()));
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

    public RadioGroupQuestion addChoice(String value, String label) {
        this.choices.add(new QuestionChoice(value, label, this.showValues));
        return this;
    }

    public boolean isShowValues() {
        return showValues;
    }

    public List<QuestionChoice> getChoices() {
        return choices;
    }

    @Override
    public Integer getValueAsInteger() {
        try {
            return getValue() != null ? Integer.valueOf(getValue()) : null;
        } catch (final NumberFormatException ex) {
            return null;
        }
    }

}
