package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ContextBreadCrumRenderer extends OutputRenderer {

    private String separator;

    public String getSeparator() {
	return separator;
    }

    public void setSeparator(String separator) {
	this.separator = separator;
    }

    public ContextBreadCrumRenderer() {
	super();
	setSeparator("&gt;");
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		FilterFunctionalityContext context = (FilterFunctionalityContext) object;
		List<Content> contents = context.getSelectedContents();

		HtmlInlineContainer inlineContainer = new HtmlInlineContainer();

		Iterator<Content> contentIterator = contents.iterator();
		while (contentIterator.hasNext()) {

		    inlineContainer.addChild(getMenuComponent(context, contentIterator.next()));
		    if (contentIterator.hasNext()) {
			inlineContainer.addChild(new HtmlText(getSeparator(), false));
		    }
		}

		return inlineContainer;
	    }

	    private HtmlComponent getMenuComponent(FilterFunctionalityContext context, Content targetContent) {

		HtmlComponent component = new HtmlText(targetContent.getName().getContent());
		List<Content> contents = context.getPathBetween(context.getSelectedTopLevelContainer(), targetContent);

		StringBuilder buffer = new StringBuilder(context.getRequest().getContextPath());
		for (Content content : contents) {
		    buffer.append("/");
		    buffer.append(content.getNormalizedName().getContent());
		}

		if (targetContent.isAvailable()) {
		    final String prefix = targetContent.isPublic() ? ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX
			    : ContentInjectionRewriter.HAS_CONTEXT_PREFIX;
		    HtmlLink link = new HtmlLinkWithPreprendedComment(prefix);

		    HtmlInlineContainer container = new HtmlInlineContainer();
		    container.addChild(component);
		    link.setContextRelative(false);
		    link.setUrl(buffer.toString());
		    link.setBody(container);

		    component = link;
		}

		return component;
	    }

	};
    }
}
