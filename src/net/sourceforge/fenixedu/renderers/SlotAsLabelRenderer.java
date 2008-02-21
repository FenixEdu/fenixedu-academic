package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class SlotAsLabelRenderer extends OutputRenderer {

    private String bundle;

    public String getBundle() {
	return this.bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		final String key = (String) object;

		final String label = RenderUtils.getResourceString(getBundle(), key);
		return new HtmlText(label);
	    }

	};
    }

}
