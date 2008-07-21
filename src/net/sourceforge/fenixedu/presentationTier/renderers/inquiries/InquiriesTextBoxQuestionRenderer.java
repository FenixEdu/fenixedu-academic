/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextArea;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesTextBoxQuestionRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		Boolean textArea = (Boolean) getContext().getProperties().get("textArea");

		final HtmlSimpleValueComponent htmlTextInput = textArea != null && textArea ? new HtmlTextArea()
			: new HtmlTextInput();
		htmlTextInput.setValue(object != null ? object.toString() : StringUtils.EMPTY);
		return htmlTextInput;
	    }
	};
    }

}
