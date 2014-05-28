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
package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobWithFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/downloadQueuedJob", module = "")
public class DownloadQueuedJob extends FenixDispatchAction {

    public ActionForward downloadFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws IOException {
        QueueJob queueJob = getDomainObject(request, "id");

        if ((queueJob instanceof QueueJobWithFile) && ((QueueJobWithFile) queueJob).getFile() != null) {
            httpServletResponse.setContentType(((QueueJobWithFile) queueJob).getContentType());
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + queueJob.getFilename());
            final OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(((QueueJobWithFile) queueJob).getFile().getContents());
            outputStream.close();
        }

        return null;
    }

}
