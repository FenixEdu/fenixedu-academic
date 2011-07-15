package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/files", scope = "session")
public class FileDownload extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String oid = request.getParameter("oid");
	File file = rootDomainObject.readFileByOID(Integer.parseInt(oid));
	if (!file.isPrivate() || file.isPersonAllowedToAccess(AccessControl.getPerson())) {
	    try {
		response.setContentType(file.getMimeType());
		final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		response.addHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
		response.setContentLength(file.getSize());
		dos.write(file.getContents());
		dos.close();
	    } catch (IOException e) {
	    }
	}
	return null;
    }
}
