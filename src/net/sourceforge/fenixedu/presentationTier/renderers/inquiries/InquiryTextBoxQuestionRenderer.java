/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextArea;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryTextBoxQuestionRenderer extends InputRenderer {

    private Integer maxLength;
    private String defaultSize;

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		Boolean textArea = (Boolean) getContext().getProperties().get("textArea");
		Boolean readOnly = (Boolean) getContext().getProperties().get("readOnly");

		if (textArea != null && textArea) {
		    HtmlTextArea htmlTextArea = new HtmlTextArea();
		    htmlTextArea.setRows(5);
		    htmlTextArea.setColumns(50);
		    String value = object != null ? object.toString() : null;
		    if (readOnly && StringUtils.isEmpty(value)) {
			value = RenderUtils.getResourceString("INQUIRIES_RESOURCES", "label.inquiry.question.notAnswered");
		    }
		    htmlTextArea.setValue(value);
		    htmlTextArea.setReadOnly(readOnly);
		    return htmlTextArea;
		} else {
		    HtmlTextInput htmlTextInput = new HtmlTextInput();
		    htmlTextInput.setSize(getDefaultSize().toString());
		    htmlTextInput.setMaxLength(getMaxLength());
		    String value = object != null ? object.toString() : null;
		    if (readOnly && StringUtils.isEmpty(value)) {
			value = RenderUtils.getResourceString("INQUIRIES_RESOURCES", "label.inquiry.question.notAnswered");
		    }
		    htmlTextInput.setValue(value);
		    htmlTextInput.setReadOnly(readOnly);
		    return htmlTextInput;
		}
	    }
	};
    }

    public Integer getMaxLength() {
	return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
	this.maxLength = maxLength;
    }

    public void setDefaultSize(String defaultSize) {
	this.defaultSize = defaultSize;
    }

    public String getDefaultSize() {
	return defaultSize;
    }
}
