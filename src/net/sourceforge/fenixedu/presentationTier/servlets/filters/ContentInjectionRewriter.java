package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestRewriteFilter.RequestRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class ContentInjectionRewriter extends RequestRewriter {

    public static final String HAS_CONTEXT_PREFIX_STRING = "HAS_CONTEXT";
    
    public static final String HAS_CONTEXT_PREFIX = "<!-- " + HAS_CONTEXT_PREFIX_STRING + " -->";
    
    public static final String BLOCK_HAS_CONTEXT_PREFIX = "<!-- BLOCK_HAS_CONTEXT -->";
    
    public static final String END_BLOCK_HAS_CONTEXT_PREFIX = "<!-- END_BLOCK_HAS_CONTEXT -->";

    public static final String CONTEXT_ATTRIBUTE_NAME = FilterFunctionalityContext.CONTEXT_ATTRIBUTE_NAME + "_PATH";

    private static final String LINK_IDENTIFIER = "<a";

    private static final String FORM_IDENTIFIER = "<form";

    private static final String IMG_IDENTIFIER = "<img";

    private static final String AREA_IDENTIFIER = "<area";

    private static final int LENGTH_OF_HAS_CONTENT_PREFIX = HAS_CONTEXT_PREFIX.length();

    private final String contextPath;

    public ContentInjectionRewriter(final HttpServletRequest httpServletRequest) {
	super(httpServletRequest);
	final FunctionalityContext functionalityContext = AbstractFunctionalityContext.getCurrentContext(httpServletRequest);
	contextPath = functionalityContext == null ? null : functionalityContext.getCurrentContextPath();
    }

    @Override
    public StringBuilder rewrite(final StringBuilder source) {
	if (contextPath == null || contextPath.length() == 0) {
	    return source;
	}

	final StringBuilder response = new StringBuilder();

	int iOffset = 0;

	while (true) {
	    final int indexOfAopen = source.indexOf(LINK_IDENTIFIER, iOffset);
	    final int indexOfFormOpen = source.indexOf(FORM_IDENTIFIER, iOffset);
	    final int indexOfImgOpen = source.indexOf(IMG_IDENTIFIER, iOffset);
	    final int indexOfAreaOpen = source.indexOf(AREA_IDENTIFIER, iOffset);
	    final int indexOfBlockHasContextopen = source.indexOf(BLOCK_HAS_CONTEXT_PREFIX, iOffset);
	    
	    if (firstIsMinValue(indexOfAopen, indexOfFormOpen, indexOfImgOpen, indexOfAreaOpen, indexOfBlockHasContextopen)) {
		if (!isPrefixed(source, indexOfAopen)) {
		    final int indexOfAclose = source.indexOf(">", indexOfAopen);
		    if (indexOfAclose >= 0) {
			final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAopen, indexOfAclose);
			if (indexOfHrefBodyStart >= 0) {
			    final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			    final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			    if (indexOfHrefBodyEnd >= 0) {
				final int indexOfJavaScript = source.indexOf("javascript:", indexOfHrefBodyStart);
				final int indexOfMailto = source.indexOf("mailto:", indexOfHrefBodyStart);
				if ((indexOfJavaScript < 0 || indexOfJavaScript > indexOfHrefBodyEnd) &&
					(indexOfMailto < 0 || indexOfMailto > indexOfHrefBodyEnd)) {
				    final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
				    boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart
					    && indexOfCardinal < indexOfHrefBodyEnd;
				    if (hasCardinal) {
					response.append(source, iOffset, indexOfCardinal);
				    } else {
					response.append(source, iOffset, indexOfHrefBodyEnd);
				    }

				    final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
				    if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
					response.append('?');
				    } else {
					response.append("&amp;");
				    }
				    appendContextParameter(response);

				    if (hasCardinal) {
					response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
				    }

				    iOffset = continueToNextToken(response, source, indexOfHrefBodyEnd, indexOfAclose);
				    continue;
				}
			    }
			}
		    }
		}
		iOffset = continueToNextToken(response, source, iOffset, indexOfAopen);
		continue;
	    } else if (firstIsMinValue(indexOfFormOpen, indexOfAopen, indexOfImgOpen, indexOfAreaOpen, indexOfBlockHasContextopen)) {
		if (!isPrefixed(source, indexOfFormOpen)) {
		    final int indexOfFormClose = source.indexOf(">", indexOfFormOpen);
		    if (indexOfFormClose >= 0) {
			final int indexOfFormActionBodyStart = findFormActionBodyStart(source, indexOfFormOpen, indexOfFormClose);
			if (indexOfFormActionBodyStart >= 0) {
			    final int indexOfFormActionBodyEnd = findFormActionBodyEnd(source, indexOfFormActionBodyStart);
			    if (indexOfFormActionBodyEnd >= 0) {
				iOffset = continueToNextToken(response, source, iOffset, indexOfFormClose);
				appendContextAttribute(response);
				continue;
			    }
			}
		    }
		}
		iOffset = continueToNextToken(response, source, iOffset, indexOfFormOpen);
		continue;
	    } else if (firstIsMinValue(indexOfImgOpen, indexOfAopen, indexOfFormOpen, indexOfAreaOpen, indexOfBlockHasContextopen)) {
		if (!isPrefixed(source, indexOfImgOpen)) {
		    final int indexOfImgClose = source.indexOf(">", indexOfImgOpen);
		    if (indexOfImgClose >= 0) {
			final int indexOfSrcBodyStart = findSrcBodyStart(source, indexOfImgOpen, indexOfImgClose);
			if (indexOfSrcBodyStart >= 0) {
			    final int indexOfSrcBodyEnd = findSrcBodyEnd(source, indexOfSrcBodyStart);
			    if (indexOfSrcBodyEnd >= 0) {
				response.append(source, iOffset, indexOfSrcBodyEnd);

				final int indexOfQmark = source.indexOf("?", indexOfSrcBodyStart);
				if (indexOfQmark == -1 || indexOfQmark > indexOfSrcBodyEnd) {
				    response.append('?');
				} else {
				    response.append("&amp;");
				}
				appendContextParameter(response);

				iOffset = continueToNextToken(response, source, indexOfSrcBodyEnd, indexOfImgClose);
				continue;
			    }
			}
		    }
		}
		iOffset = continueToNextToken(response, source, iOffset, indexOfImgOpen);
		continue;
	    } else if (firstIsMinValue(indexOfAreaOpen, indexOfAopen, indexOfFormOpen, indexOfImgOpen, indexOfBlockHasContextopen)) {
		if (!isPrefixed(source, indexOfAreaOpen)) {
		    final int indexOfAreaClose = source.indexOf(">", indexOfAreaOpen);
		    if (indexOfAreaClose >= 0) {
			final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAreaOpen, indexOfAreaClose);
			if (indexOfHrefBodyStart >= 0) {
			    final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			    final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			    if (indexOfHrefBodyEnd >= 0) {
				final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
				boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart
					&& indexOfCardinal < indexOfHrefBodyEnd;
				if (hasCardinal) {
				    response.append(source, iOffset, indexOfCardinal);
				} else {
				    response.append(source, iOffset, indexOfHrefBodyEnd);
				}

				final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
				if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
				    response.append('?');
				} else {
				    response.append("&amp;");
				}
				appendContextParameter(response);

				if (hasCardinal) {
				    response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
				}

				iOffset = continueToNextToken(response, source, indexOfHrefBodyEnd, indexOfAreaClose);
				continue;
			    }
			}
		    }
		}
		iOffset = continueToNextToken(response, source, iOffset, indexOfAreaOpen);
		continue;
	    } else if(firstIsMinValue(indexOfBlockHasContextopen, indexOfAopen, indexOfFormOpen, indexOfImgOpen, indexOfAreaOpen)) {
		final int indexOfEndBlockHasContextOpen = source.indexOf(END_BLOCK_HAS_CONTEXT_PREFIX, indexOfBlockHasContextopen);
		if(indexOfEndBlockHasContextOpen == -1) {
		    iOffset = indexOfBlockHasContextopen + BLOCK_HAS_CONTEXT_PREFIX.length();
		} else {
		    response.append(source, iOffset, indexOfEndBlockHasContextOpen);
		    iOffset = indexOfEndBlockHasContextOpen;
		}
		continue;
	    } else {
		response.append(source, iOffset, source.length());
		break;
	    }
	}

	return response;
    }

    private void appendContextParameter(final StringBuilder response) {
	response.append(CONTEXT_ATTRIBUTE_NAME);
	response.append("=");
	response.append(contextPath);
    }

    private void appendContextAttribute(final StringBuilder response) {
	response.append("<input type=\"hidden\" name=\"");
	response.append(CONTEXT_ATTRIBUTE_NAME);
	response.append("\" value=\"");
	response.append(contextPath);
	response.append("\"/>");
    }

    protected int continueToNextToken(final StringBuilder response, final StringBuilder source, final int iOffset,
	    final int indexOfTag) {
	final int nextOffset = indexOfTag + 1;
	response.append(source, iOffset, nextOffset);
	return nextOffset;
    }

    protected boolean isPrefixed(final StringBuilder source, final int indexOfTagOpen) {
	return indexOfTagOpen >= LENGTH_OF_HAS_CONTENT_PREFIX
		&& match(source, indexOfTagOpen - LENGTH_OF_HAS_CONTENT_PREFIX, indexOfTagOpen, HAS_CONTEXT_PREFIX);
    }

    protected boolean match(final StringBuilder source, final int iStart, int iEnd, final String string) {
	if (iEnd - iStart != string.length()) {
	    return false;
	}
	for (int i = 0; i < string.length(); i++) {
	    if (source.charAt(iStart + i) != string.charAt(i)) {
		return false;
	    }
	}
	return true;
    }

    protected boolean firstIsMinValue(final int index, final int... indexes) {
	if (index >= 0) {
	    for (final int otherIndex : indexes) {
		if (otherIndex >= 0 && otherIndex < index) {
		    return false;
		}
	    }
	    return true;
	}
	return false;
    }

}