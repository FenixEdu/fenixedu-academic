package net.sourceforge.fenixedu.presentationTier.servlets.filters.requestWrappers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.sourceforge.fenixedu.presentationTier.servlets.filters.RequestRewriteFilter.RequestRewriter;

public class ResponseWrapper extends HttpServletResponseWrapper {

    protected final HttpServletResponse httpServletResponse;

    protected BufferedFacadServletOutputStream bufferedFacadServletOutputStream = null;

    protected BufferedFacadPrintWriter bufferedFacadPrintWriter = null;

    public ResponseWrapper(final HttpServletResponse httpServletResponse)
    		throws IOException {
	super(httpServletResponse);
	this.httpServletResponse = httpServletResponse;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
	if (bufferedFacadServletOutputStream == null) {
	    bufferedFacadServletOutputStream = new BufferedFacadServletOutputStream(httpServletResponse.getOutputStream());
	}
	return bufferedFacadServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
	if (bufferedFacadPrintWriter == null) {
	    bufferedFacadPrintWriter = new BufferedFacadPrintWriter(httpServletResponse.getWriter());
	}
	return bufferedFacadPrintWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
	if (bufferedFacadServletOutputStream != null) {
	    bufferedFacadServletOutputStream.flush();
	}
	if (bufferedFacadPrintWriter != null) {
	    bufferedFacadPrintWriter.flush();
	}
    }

    public void writeRealResponse(final RequestRewriter... requestRewriters) throws IOException {
	if (bufferedFacadServletOutputStream != null) {
	    bufferedFacadServletOutputStream.writeRealResponse(requestRewriters);
	}
	if (bufferedFacadPrintWriter != null) {
	    bufferedFacadPrintWriter.writeRealResponse(requestRewriters);
	}
    }

}
