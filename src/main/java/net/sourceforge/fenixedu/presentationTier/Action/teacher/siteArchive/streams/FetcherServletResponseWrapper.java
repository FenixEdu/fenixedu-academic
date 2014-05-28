/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Fetcher;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;

/**
 * This response wrapper is used by the fetcher to prevent any content generated
 * by the resource to be written to the wrapped servlet response thus commiting
 * the response and making it useless.
 * 
 * <p>
 * The {@link #getOutputStream() ServletOutputStream} and {@link #getWriter()
 * PrinterWriter} returned by the response will write to the provided stream ratter than to the wrapped response. If the response
 * is of type <tt>"text/html"</tt> then an intermediary
 * {@link net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.streams.TransformUrlsStream} is used to ensure that
 * the content is transfomed using the target resource's rules.
 * 
 * @author cfgi
 */
public class FetcherServletResponseWrapper extends HttpServletResponseWrapper {

    private Fetcher fetcher;
    private Resource resource;
    private OutputStream stream;

    private String contentType;

    private PrintWriter writer;
    private ServletOutputStream parseStream;

    /**
     * Wraps the given response and uses the given stream as the destiny of all
     * output instead. If the resource rules are applyed to the content, all the
     * generate resources will be queue in the given fetcher.
     * 
     * @param response
     *            the wrapped response
     * @param fetcher
     *            were all the extra resources will be queued
     * @param resource
     *            the target resource
     * @param stream
     *            the destiny of all generated content
     */
    public FetcherServletResponseWrapper(HttpServletResponse response, Fetcher fetcher, Resource resource, OutputStream stream) {
        super(response);

        this.fetcher = fetcher;
        this.resource = resource;
        this.stream = stream;
    }

    public Fetcher getFetcher() {
        return this.fetcher;
    }

    public Resource getResource() {
        return this.resource;
    }

    public OutputStream getStream() {
        return this.stream;
    }

    protected OutputStream getDecoratedStream() {
        return new TransformUrlsStream(getFetcher(), getResource(), getStream(), getCharacterEncoding());
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    private boolean isHtmlResponse() {
        String contentType = getContentType();

        return contentType != null && contentType.contains("text/html");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.parseStream == null) {
            OutputStream stream = isHtmlResponse() ? getDecoratedStream() : getStream();
            this.parseStream = new DirectServletOutputStream(stream);
        }

        return this.parseStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.writer == null) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getOutputStream(), getCharacterEncoding());
            this.writer = new PrintWriter(outputStreamWriter);
        }

        return this.writer;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
    }

    public void flush() throws IOException {
        if (this.parseStream != null) {
            this.parseStream.flush();
        }
    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void setContentLength(int len) {
        // ignore
    }

    @Override
    public void setBufferSize(int size) {
        // ignore
    }

    @Override
    public void flushBuffer() throws IOException {
        flush();
    }

    @Override
    public void resetBuffer() {
        // ignore
    }

    @Override
    public void reset() {
        // ignore
    }

}
