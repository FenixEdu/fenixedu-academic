/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryCheckBoxQuestionRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Boolean readOnly = (Boolean) getContext().getProperties().get("readOnly");
		final HtmlCheckBox htmlCheckBox = new HtmlCheckBox();
		if (object != null
			&& (Boolean.valueOf(object.toString()) || object.toString().equalsIgnoreCase("on") || object.toString()
				.equals("1"))) {
		    htmlCheckBox.setChecked(true);
		}
		if (readOnly) {
		    htmlCheckBox.setOnClick("return false;");
		}
		return htmlCheckBox;
	    }
	};
    }
}
