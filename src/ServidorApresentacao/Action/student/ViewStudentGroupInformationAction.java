/*
 * Created on 26/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentGroup;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *
 */
public class ViewStudentGroupInformationAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentGroupCodeString = request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);

		ISiteComponent viewStudentGroup;
		Object[] args = { studentGroupCode };
		try {
			viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView, "ReadStudentGroupInformation", args);

		} catch (FenixServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewStudentGroups");
		}
		InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

		List infoSiteStudentInformationList = new ArrayList();
		if (infoSiteStudentGroup != null) {
			infoSiteStudentInformationList = infoSiteStudentGroup.getInfoSiteStudentInformationList();
		}

		request.setAttribute("infoSiteStudentInformationList", infoSiteStudentInformationList);

		return mapping.findForward("sucess");
	}
}
