/*
 * Created on 27/Ago/2003
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
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *
 */
public class GroupStudentEnrolmentDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentGroupCodeString = request.getParameter("studentGroupCode");

		Integer studentGroupCode = new Integer(studentGroupCodeString);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		Integer result;
		Object[] args1 = { studentGroupCode, userView.getUtilizador()};
		try {
			result = (Integer) ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (result.intValue() == 1) {

			Object[] args2 = { studentGroupCode };
			ISiteComponent viewStudentGroup = null;
			try {
				viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView, "ReadStudentGroupInformation", args2);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

			List infoSiteStudentInformationList = new ArrayList();
			if (infoSiteStudentGroup != null) {
				infoSiteStudentInformationList = infoSiteStudentGroup.getInfoSiteStudentInformationList();
			}

			request.setAttribute("infoSiteStudentInformationList", infoSiteStudentInformationList);

			return mapping.findForward("sucess");
		} else {

			switch (result.intValue()) {
				case -1 :
					ActionErrors actionErrors1 = new ActionErrors();
					ActionError error1 = null;
					// Create an ACTION_ERROR 
					error1 = new ActionError("errors.existing.groupStudentEnrolment");
					actionErrors1.add("errors.existing.groupStudentEnrolment", error1);
					saveErrors(request, actionErrors1);
					return mapping.findForward("viewProjectStudentGroups");

				case -2 :
					ActionErrors actionErrors2 = new ActionErrors();
					ActionError error2 = null;
					// Create an ACTION_ERROR 
					error2 = new ActionError("errors.invalid.insert.groupStudentEnrolment");
					actionErrors2.add("errors.invalid.insert.groupStudentEnrolment", error2);
					saveErrors(request, actionErrors2);
					return mapping.findForward("viewProjectStudentGroups");

				case -3 :
					ActionErrors actionErrors3 = new ActionErrors();
					ActionError error3 = null;
					// Create an ACTION_ERROR 
					error3 = new ActionError("error.noGroup");
					actionErrors3.add("error.noGroup", error3);
					saveErrors(request, actionErrors3);
					return mapping.findForward("viewProjectStudentGroups");

			}
			//request.setAttribute("groupPropertiesCode",groupPropertiesCode);
			return mapping.findForward("viewProjectStudentGroups");
		}
	}

	public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		String studentGroupCodeString = request.getParameter("studentGroupCode");

		Integer studentGroupCode = new Integer(studentGroupCodeString);

		Object[] args1 = { studentGroupCode, userView.getUtilizador()};
		Boolean result = new Boolean(false);
		try {

			result = (Boolean) ServiceUtils.executeService(userView, "GroupStudentEnrolment", args1);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (!result.booleanValue()) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			// Create an ACTION_ERROR 
			error = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors.add("errors.existing.groupStudentEnrolment", error);
			saveErrors(request, actionErrors);

		}

		return mapping.findForward("viewProjectStudentGroups");

	}

}