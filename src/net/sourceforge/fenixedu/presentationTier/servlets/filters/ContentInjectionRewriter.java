package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter;

public class ContentInjectionRewriter extends RequestRewriter {

	public static final String CONTEXT_ATTRIBUTE_NAME = FilterFunctionalityContext.CONTEXT_ATTRIBUTE_NAME + "_PATH";

	public ContentInjectionRewriter(final HttpServletRequest httpServletRequest) {
		super(httpServletRequest);
	}

	@Override
	protected String getContextPath(final HttpServletRequest httpServletRequest) {
		final FunctionalityContext functionalityContext = AbstractFunctionalityContext.getCurrentContext(httpServletRequest);
		String currentContextPath = functionalityContext == null ? null : functionalityContext.getCurrentContextPath();
		return currentContextPath == null ? null : currentContextPath.replace("<", "&lt;").replace(">", "&gt;");
	}

	@Override
	protected String getContextAttributeName() {
		return CONTEXT_ATTRIBUTE_NAME;
	}

	static public String buildContextAttribute(final String prefix) {
		return ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + Container.getContextPath(prefix);
	}

	@Override
	protected boolean isPrefixed(final StringBuilder source, final int indexOfTagOpen) {
		return indexOfTagOpen >= LENGTH_OF_HAS_CONTENT_PREFIX
				&& match(source, indexOfTagOpen - LENGTH_OF_HAS_CONTENT_PREFIX, indexOfTagOpen, HAS_CONTEXT_PREFIX);
	}

}
