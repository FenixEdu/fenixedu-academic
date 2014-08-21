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
package net.sourceforge.fenixedu.presentationTier.servlets;

import net.sourceforge.fenixedu.domain.File;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class FileDownloadServlet extends org.fenixedu.bennu.io.servlets.FileDownloadServlet {

    private static final long serialVersionUID = 6954413451468325605L;

    static final String SERVLET_PATH = "/downloadFile/";

    public final static File getFileFromURL(String url) {
        try {
            // Remove trailing path, and split the tokens
            String[] parts = url.substring(url.indexOf(SERVLET_PATH)).replace(SERVLET_PATH, "").split("\\/");
            if (parts.length == 0) {
                return null;
            }
            DomainObject object = FenixFramework.getDomainObject(parts[0]);
            if (object instanceof File && FenixFramework.isDomainObjectValid(object)) {
                return (File) object;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
