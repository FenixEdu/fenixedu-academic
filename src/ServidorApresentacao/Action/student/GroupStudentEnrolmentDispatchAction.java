/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

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
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
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
		
		String shiftCodeString = request.getParameter("shiftCode");
		request.setAttribute("shiftCode", shiftCodeString);

		Object[] args1 = { null, null, studentGroupCode, userView.getUtilizador(), new Integer(1)};
		
		try {
			ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);

		} catch (NotAuthorizedException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.noStudentInAttendsSet");
			actionErrors.add("errors.noStudentInAttendsSet", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("insucess");
		
		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			// Create an ACTION_ERROR 
			error2 = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors2.add("errors.existing.groupStudentEnrolment", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("viewStudentGroupInformation");

		} catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.invalid.insert.groupStudentEnrolment");
			actionErrors1.add("errors.invalid.insert.groupStudentEnrolment", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("viewShiftsAndGroups");

		} catch (FenixServiceException e) {
			ActionErrors actionErrors3 = new ActionErrors();
			ActionError error3 = null;
			// Create an ACTION_ERROR 
			error3 = new ActionError("error.noGroup");
			actionErrors3.add("error.noGroup", error3);
			saveErrors(request, actionErrors3);
			return mapping.findForward("viewShiftsAndGroups");

		}

		Object[] args2 = { studentGroupCode };
		ISiteComponent viewStudentGroup = null;
		try {
			viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView, "ReadStudentGroupInformation", args2);

		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors3 = new ActionErrors();
			ActionError error3 = null;
			// Create an ACTION_ERROR 
			error3 = new ActionError("error.noGroup");
			actionErrors3.add("error.noGroup", error3);
			saveErrors(request, actionErrors3);
			return mapping.findForward("viewShiftsAndGroups");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

		request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

		return mapping.findForward("sucess");

	}

	public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentGroupCodeString = request.getParameter("studentGroupCode");

		Integer studentGroupCode = new Integer(studentGroupCodeString);

		Object[] args1 = { studentGroupCode, userView.getUtilizador()};
		
		try {

			ServiceUtils.executeService(userView, "GroupStudentEnrolment", args1);

		}  catch (NotAuthorizedException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.noStudentInAttendsSet");
			actionErrors.add("errors.noStudentInAttendsSet", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("insucess");
		
		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors.add("errors.existing.groupStudentEnrolment", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewStudentGroupInformation");

		} catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.invalid.insert.groupStudentEnrolment");
			actionErrors1.add("errors.invalid.insert.groupStudentEnrolment", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("viewShiftsAndGroups");
			
		} catch (FenixServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");

		}
		return mapping.findForward("viewStudentGroupInformation");

	}

}