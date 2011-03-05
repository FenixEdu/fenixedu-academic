/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextArea;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryTextBoxQuestionRenderer extends InputRenderer {

    private Integer maxLength;

    public Integer getMaxLength() {
	return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
	this.maxLength = maxLength;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		Boolean textArea = (Boolean) getContext().getProperties().get("textArea");

		if (textArea != null && textArea) {
		    HtmlTextArea htmlTextArea = new HtmlTextArea();
		    htmlTextArea.setRows(5);
		    htmlTextArea.setColumns(80);
		    htmlTextArea.setValue(object != null ? object.toString() : null);
		    return htmlTextArea;
		} else {
		    HtmlTextInput htmlTextInput = new HtmlTextInput();
		    htmlTextInput.setMaxLength(getMaxLength());
		    htmlTextInput.setValue(object != null ? object.toString() : null);
		    return htmlTextInput;
		}
	    }
	};
    }
}
