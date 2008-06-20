/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class QuestionChoice implements Serializable {

    private String value;

    private String label;

    private boolean showLabel;

    public QuestionChoice(String value, String label, boolean showLabel) {
	this.value = value;
	this.label = label;
	this.showLabel = showLabel;
    }

    public String getValue() {
	return value;
    }

    public String getLabel() {
	return label;
    }

    public boolean isShowLabel() {
	return showLabel;
    }

}
