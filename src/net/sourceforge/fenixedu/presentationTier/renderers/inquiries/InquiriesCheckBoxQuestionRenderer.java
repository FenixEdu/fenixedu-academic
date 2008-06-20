/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesCheckBoxQuestionRenderer extends InputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {

	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		final HtmlCheckBox htmlCheckBox = new HtmlCheckBox();
		if (object != null && (Boolean.valueOf(object.toString()) || object.toString().equalsIgnoreCase("on"))) {
		    htmlCheckBox.setChecked(true);
		}
		return htmlCheckBox;
	    }
	};
    }

}
