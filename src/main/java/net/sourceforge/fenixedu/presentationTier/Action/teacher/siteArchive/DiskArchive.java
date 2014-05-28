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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * A <tt>DiskArchive</tt> writes all resources to disk under the root directoy
 * given in it's creation. Nothing is written to the response so it's usefull
 * afterwards.
 * 
 * @author cfgi
 */
public class DiskArchive extends Archive {

    private File directory;

    /**
     * @param root
     *            the root directory under wich all resources will be saved
     */
    public DiskArchive(File root, HttpServletResponse response) throws IOException {
        super(response, null);

        this.directory = root;
    }

    public static File getTemporaryDirectory() throws IOException {
        File file = File.createTempFile("archive", null);
        file.delete();
        file.mkdir();

        return file;
    }

    protected File getRoot() {
        return this.directory;
    }

    /**
     * Obtains a new file stream. The stream is created based on the resource
     * name and the root directoy of the archive.
     */
    @Override
    public OutputStream getStream(Resource resource) throws IOException {
        File file = new File(this.directory, resource.getName());

        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        return new FileOutputStream(file);
    }

    @Override
    public String toString() {
        return String.format("Archive[%s]", this.directory);
    }

    /**
     * Does nothing.
     */
    @Override
    public void finish() throws IOException {
        // do nothing
    }
}
