package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class ChecksumRewriter extends GenericChecksumRewriter {

    public ChecksumRewriter(HttpServletRequest httpServletRequest) {
	super(httpServletRequest);
    }

    public final static String NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX = NO_CHECKSUM_PREFIX
	    + ContentInjectionRewriter.HAS_CONTEXT_PREFIX;

    private static final int LENGTH_OF_NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX = NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX.length();

    protected boolean isPrefixed(final StringBuilder source, final int indexOfTagOpen) {
	return (indexOfTagOpen >= LENGTH_OF_NO_CHECKSUM_PREFIX && match(source, indexOfTagOpen - LENGTH_OF_NO_CHECKSUM_PREFIX,
		indexOfTagOpen, NO_CHECKSUM_PREFIX))
		|| (indexOfTagOpen >= LENGTH_OF_NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX && match(source, indexOfTagOpen
			- LENGTH_OF_NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX, indexOfTagOpen, NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX));
    }

}
