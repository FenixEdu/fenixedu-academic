package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * This renderer renderes bread crumbs based on the current functionalities
 * context.
 * 
 * @author cfgi
 */
public class BreadCrumbsRenderer extends OutputRenderer {

    private String separator;

    private Map<String, String> links;

    public BreadCrumbsRenderer() {
	super();

	setSeparator("&gt;");
	links = new HashMap<String, String>();
    }

    public void setLinkFor(String name, String value) {
	links.put(name, value);
    }

    public String getLinkFor(String name) {
	return links.get(name);
    }

    public String getSeparator() {
	return this.separator;
    }

    /**
     * The text separator used when separating the different links. By default
     * &gt; is used.
     * 
     * @param separator
     */
    public void setSeparator(String separator) {
	this.separator = separator;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		HtmlContainer container = new HtmlInlineContainer();

		Content content = (Content) object;

		List<Content> contents = RootDomainObject.getInstance().getRootPortal().getPathTo(content);
		Iterator<Content> contentIterator = contents.iterator();

		while (contentIterator.hasNext()) {
		    container.addChild(createLink(contentIterator.next()));
		    if (contentIterator.hasNext()) {
			container.addChild(new HtmlText(getSeparator(), false));
		    }
		}

		return container;
	    }

	    private HtmlComponent createLink(Content content) {
		String linkToFormat = getLinkFor(content.getClass().getSimpleName());
		if (linkToFormat != null) {
		    HtmlLink link = content.isPublic() ? new HtmlLinkWithPreprendedComment(ChecksumRewriter.NO_CHECKSUM_PREFIX)
			    : new HtmlLink();
		    link.setModuleRelative(true);
		    link.setContextRelative(true);
		    link.setText(content.getName().getContent());
		    link.setUrl(RenderUtils.getFormattedProperties(linkToFormat, content));

		    return link;
		} else {
		    return new HtmlText(content.getName().getContent());
		}
	    }

	};
    }
}
