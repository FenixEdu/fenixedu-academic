package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/downloadQueuedJob", module = "")
public class DownloadQueuedJob extends FenixDispatchAction {

    public ActionForward downloadFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    final HttpServletResponse httpServletResponse) throws IOException {

	int oid = Integer.valueOf(request.getParameter("id"));
	for (QueueJob queueJob : rootDomainObject.getQueueJob()) {
	    if (queueJob.getIdInternal() == oid) {
		httpServletResponse.setContentType(((QueueJobFile) queueJob).getContentType());
		httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + queueJob.getFilename());
		final OutputStream outputStream = httpServletResponse.getOutputStream();
		outputStream.write(((QueueJobFile) queueJob).getContent().getBytes());
		outputStream.close();
	    }
	}

	return null;
    }

}
