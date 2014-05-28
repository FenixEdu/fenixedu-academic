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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * The <tt>ZipArchive</tt> creates a Zip with all the resources added to the
 * archive.
 * 
 * @author cfgi
 */
public class ZipArchive extends Archive {

    public static String ARCHIVE_CONTENT_TYPE = "application/zip";

    private ZipOutputStream zipStream;

    public ZipArchive(HttpServletResponse response, String name) throws IOException {
        super(response, name);

        response.setContentType(ARCHIVE_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + name + ".zip\"");
    }

    /**
     * Creates a new entry in the Zip and returns the zip stream to write to.
     */
    @Override
    public OutputStream getStream(Resource resource) throws IOException {
        if (this.zipStream == null) {
            ServletOutputStream stream = getResponse().getOutputStream();
            this.zipStream = new ZipOutputStream(stream);
        }

        this.zipStream.putNextEntry(new ZipEntry(resource.getName()));
        return new CloseEntryStream(this.zipStream);
    }

    /**
     * Closes the Zip stream.
     */
    @Override
    public void finish() throws IOException {
        if (this.zipStream != null) {
            this.zipStream.close();
        }
    }

    /**
     * Intermediary stream that, when closed, closes the zip entry instead of
     * the zip stream itself.
     * 
     * @author cfgi
     */
    private static class CloseEntryStream extends OutputStream {

        private ZipOutputStream stream;

        public CloseEntryStream(ZipOutputStream stream) {
            super();

            this.stream = stream;
        }

        @Override
        public void write(int b) throws IOException {
            this.stream.write(b);
        }

        @Override
        public void flush() throws IOException {
            this.stream.flush();
        }

        @Override
        public void close() throws IOException {
            flush();
            this.stream.closeEntry();
        }

    }

}
