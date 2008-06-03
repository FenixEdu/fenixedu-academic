package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.research.Researcher;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ResearcherAllowedContacts extends OutputRenderer {

    private String keyForStudents;
    private String keyForMedia;
    private String keyForOtherResearchers;
    private String bundle;

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		Researcher researcher = (Researcher) object;
		StringBuffer buffer = new StringBuffer("");

		addContentToBuffer(buffer, researcher.getAllowsContactByStudents(), getTextFromBundle(getKeyForStudents(), getBundle()));
		addContentToBuffer(buffer, researcher.getAllowsContactByMedia(), getTextFromBundle(getKeyForMedia(), getBundle()));
		addContentToBuffer(buffer, researcher.getAllowsContactByOtherResearchers(), getTextFromBundle(getKeyForOtherResearchers(), getBundle()));

		HtmlText text = new HtmlText(buffer.toString());
		HtmlInlineContainer container = new HtmlInlineContainer();
		container.addChild(text);
		
		return container;
	    }

	    private void addContentToBuffer(StringBuffer buffer, Boolean shouldBeAdded, String text) {
		if (shouldBeAdded) {
		    if (buffer.length() > 0) {
			buffer.append(", ");
		    }
		    buffer.append(text);
		}
	    }

	    private String getTextFromBundle(String key, String bundle) {
		return RenderUtils.getResourceString(bundle, key);
	    }

	};
    }

    public String getKeyForStudents() {
	return keyForStudents;
    }

    public void setKeyForStudents(String keyForStudents) {
	this.keyForStudents = keyForStudents;
    }

    public String getKeyForMedia() {
	return keyForMedia;
    }

    public void setKeyForMedia(String keyForMedia) {
	this.keyForMedia = keyForMedia;
    }

    public String getKeyForOtherResearchers() {
	return keyForOtherResearchers;
    }

    public void setKeyForOtherResearchers(String keyForOtherResearchers) {
	this.keyForOtherResearchers = keyForOtherResearchers;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

}
