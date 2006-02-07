/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.fileManager;

import java.io.DataOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.RetrieveItemFile;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head presentationTier.Action.scientificCouncil
 * 
 */
public class FileRetrievingAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		final String fileName = request.getParameter("fileName");
		final String itemCodeString = request.getParameter("itemCode");
		final Integer itemCode = Integer.valueOf(itemCodeString);

		final IUserView userView = SessionUtils.getUserView(request);

		final String slidename = (String) ServiceUtils.executeService(userView, "ReadItemSlideName", new Object[] { itemCode });
		final FileSuportObject file = RetrieveItemFile.run(slidename, fileName);

		response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
		response.setContentType(file.getContentType());

		final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(file.getContent());
		dos.close();

		return null;
	}

}