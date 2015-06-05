/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.QueueJobWithFile;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/downloadQueuedJob", module = "")
public class DownloadQueuedJob extends FenixDispatchAction {

    public ActionForward downloadFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws IOException {
        QueueJob queueJob = getDomainObject(request, "id");

        if ((queueJob instanceof QueueJobWithFile) && ((QueueJobWithFile) queueJob).getFile() != null) {
            httpServletResponse.setContentType(((QueueJobWithFile) queueJob).getContentType());
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + queueJob.getFilename());
            final OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(((QueueJobWithFile) queueJob).getFile().getContent());
            outputStream.close();
        }

        return null;
    }

}
