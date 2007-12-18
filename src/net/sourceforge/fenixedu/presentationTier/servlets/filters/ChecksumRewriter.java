package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestRewriteFilter.RequestRewriter;

import org.apache.commons.codec.digest.DigestUtils;

public class ChecksumRewriter extends RequestRewriter {

    public static final String CHECKSUM_ATTRIBUTE_NAME = "_request_checksum_";

    private static String calculateChecksum(final StringBuilder source, final int start, final int end) {
	return calculateChecksum(source.substring(start, end));
    }

    private static boolean isRelevantPart(final String part) {
	return part.length() > 0
		&& !part.startsWith(CHECKSUM_ATTRIBUTE_NAME)
		&& !part.startsWith("page=")
		&& !part.startsWith("org.apache.struts.action.LOCALE")
		&& !part.startsWith("javax.servlet.request.")
		&& !part.startsWith("ok");
    }

    private static String calculateChecksum(final TreeSet<String> strings) {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final String string : strings) {
	    stringBuilder.append(string);
	}
	final IUserView userView = AccessControl.getUserView();
	stringBuilder.append(userView.getPrivateConstantForDigestCalculation());
	final String checksum = new String(DigestUtils.shaHex(stringBuilder.toString()));
//	System.out.println("Generating checksum for: " + stringBuilder.toString() + " --> " + checksum);
	return checksum;
    }

    public static String calculateChecksum(final String requestString) {
	final int indexLastCardinal = requestString.lastIndexOf('#');
	final String string = indexLastCardinal >= 0 ? requestString.substring(0, indexLastCardinal) : requestString;
	final String[] parts = string.split("\\?|&amp;|&");

	final TreeSet<String> strings = new TreeSet<String>();
	for (final String part : parts) {
	    if (isRelevantPart(part)) {
		final int indexOfEquals = part.indexOf('=');
		if (indexOfEquals >= 0) {
		    strings.add(part.substring(0, indexOfEquals));
		    strings.add(part.substring(indexOfEquals + 1, part.length()));
		} else {
		    strings.add(part);
		}
	    }
	}
	return calculateChecksum(strings);
    }

    public ChecksumRewriter(HttpServletRequest httpServletRequest) {
	super(httpServletRequest);
	// TODO Auto-generated constructor stub
    }

    @Override
    public StringBuilder rewrite(StringBuilder source) {
	if(isRedirectRequest(httpServletRequest)) {
	    return source;
	}
	final StringBuilder response = new StringBuilder();

	int iOffset = 0;

	while (true) {

	    final int indexOfAopen = source.indexOf("<a ", iOffset);
	    final int indexOfFormOpen = source.indexOf("<form ", iOffset);
	    final int indexOfImgOpen = source.indexOf("<img ", iOffset);
	    final int indexOfAreaOpen = source.indexOf("<area ", iOffset);
	    if (indexOfAopen >= 0 && (indexOfFormOpen < 0 || indexOfAopen < indexOfFormOpen)
		    && (indexOfImgOpen < 0 || indexOfAopen < indexOfImgOpen)
		    && (indexOfAreaOpen < 0 || indexOfAopen < indexOfAreaOpen)) {
		final int indexOfAclose = source.indexOf(">", indexOfAopen);
		if (indexOfAclose >= 0) {
		    final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAopen, indexOfAclose);
		    if (indexOfHrefBodyStart >= 0) {
			final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			if (indexOfHrefBodyEnd >= 0) {

			    final int indexOfJavaScript = source.indexOf("javascript:", indexOfHrefBodyStart);
			    if (indexOfJavaScript < 0 || indexOfJavaScript > indexOfHrefBodyEnd) {

				final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
				boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart
					&& indexOfCardinal < indexOfHrefBodyEnd;
				if (hasCardinal) {
				    response.append(source, iOffset, indexOfCardinal);
				} else {
				    response.append(source, iOffset, indexOfHrefBodyEnd);
				}

				final String checksum = calculateChecksum(source, indexOfHrefBodyStart, indexOfHrefBodyEnd);
				final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
				if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
				    response.append('?');
				} else {
				    response.append("&amp;");
				}
				response.append(CHECKSUM_ATTRIBUTE_NAME);
				response.append("=");
				response.append(checksum);

				if (hasCardinal) {
				    response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
				}

				final int nextChar = indexOfAclose + 1;
				response.append(source, indexOfHrefBodyEnd, nextChar);
				// rewrite(response, source, nextChar);
				// return;
				iOffset = nextChar;
				continue;
			    }
			}
		    }
		}
	    } else if (indexOfFormOpen >= 0 && (indexOfImgOpen < 0 || indexOfFormOpen < indexOfImgOpen)
		    && (indexOfAreaOpen < 0 || indexOfFormOpen < indexOfAreaOpen)) {
		final int indexOfFormClose = source.indexOf(">", indexOfFormOpen);
		if (indexOfFormClose >= 0) {
		    final int indexOfFormActionBodyStart = findFormActionBodyStart(source, indexOfFormOpen, indexOfFormClose);
		    if (indexOfFormActionBodyStart >= 0) {
			final int indexOfFormActionBodyEnd = findFormActionBodyEnd(source, indexOfFormActionBodyStart);
			if (indexOfFormActionBodyEnd >= 0) {
			    final int nextChar = indexOfFormClose + 1;
			    response.append(source, iOffset, nextChar);
			    final String checksum = calculateChecksum(source, indexOfFormActionBodyStart,
				    indexOfFormActionBodyEnd);
			    response.append("<input type=\"hidden\" name=\"");
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("\" value=\"");
			    response.append(checksum);
			    response.append("\"/>");
			    // rewrite(response, source, nextChar);
			    // return;
			    iOffset = nextChar;
			    continue;
			}
		    }
		}
	    } else if (indexOfImgOpen >= 0 && (indexOfAreaOpen < 0 || indexOfImgOpen < indexOfAreaOpen)) {
		final int indexOfImgClose = source.indexOf(">", indexOfImgOpen);
		if (indexOfImgClose >= 0) {
		    final int indexOfSrcBodyStart = findSrcBodyStart(source, indexOfImgOpen, indexOfImgClose);
		    if (indexOfSrcBodyStart >= 0) {
			final int indexOfSrcBodyEnd = findSrcBodyEnd(source, indexOfSrcBodyStart);
			if (indexOfSrcBodyEnd >= 0) {
			    response.append(source, iOffset, indexOfSrcBodyEnd);

			    final String checksum = calculateChecksum(source, indexOfSrcBodyStart, indexOfSrcBodyEnd);
			    final int indexOfQmark = source.indexOf("?", indexOfSrcBodyStart);
			    if (indexOfQmark == -1 || indexOfQmark > indexOfSrcBodyEnd) {
				response.append('?');
			    } else {
				response.append("&amp;");
			    }
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("=");
			    response.append(checksum);

			    final int nextChar = indexOfImgClose + 1;
			    response.append(source, indexOfSrcBodyEnd, nextChar);
			    // rewrite(response, source, nextChar);
			    // return;
			    iOffset = nextChar;
			    continue;
			}
		    }
		}
	    } else if (indexOfAreaOpen >= 0) {
		final int indexOfAreaClose = source.indexOf(">", indexOfAreaOpen);
		if (indexOfAreaClose >= 0) {
		    final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAreaOpen, indexOfAreaClose);
		    if (indexOfHrefBodyStart >= 0) {
			final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			if (indexOfHrefBodyEnd >= 0) {
			    final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
			    boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart && indexOfCardinal < indexOfHrefBodyEnd;
			    if (hasCardinal) {
				response.append(source, iOffset, indexOfCardinal);
			    } else {
				response.append(source, iOffset, indexOfHrefBodyEnd);
			    }

			    final String checksum = calculateChecksum(source, indexOfHrefBodyStart, indexOfHrefBodyEnd);
			    final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
			    if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
				response.append('?');
			    } else {
				response.append("&amp;");
			    }
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("=");
			    response.append(checksum);

			    if (hasCardinal) {
				response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
			    }

			    final int nextChar = indexOfAreaClose + 1;
			    response.append(source, indexOfHrefBodyEnd, nextChar);
			    // rewrite(response, source, nextChar);
			    // return;
			    iOffset = nextChar;
			    continue;
			}
		    }
		}
	    }
	    response.append(source, iOffset, source.length());
	    break;
	}

	return response;
    }
    
    private boolean isRedirectRequest(final HttpServletRequest httpServletRequest) {
	final String uri = httpServletRequest.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH);
	return uri.indexOf("redirect.do") >= 0;
    }


}
