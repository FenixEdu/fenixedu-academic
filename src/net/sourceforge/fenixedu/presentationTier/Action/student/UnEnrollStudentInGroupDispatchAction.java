/*
 * Created on 27/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author asnr and scpo
 * 
 */
public class UnEnrollStudentInGroupDispatchAction extends FenixDispatchAction {

	public ActionForward prepareRemove(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException, FenixFilterException {

		HttpSession session = request.getSession(false);
		IUserView userView = getUserView(request);

		String studentGroupCodeString = request.getParameter("studentGroupCode");

		Integer studentGroupCode = new Integer(studentGroupCodeString);
		
		String shiftCodeString = request.getParameter("shiftCode");
		request.setAttribute("shiftCode", shiftCodeString);

		Object[] args1 = { null,null, studentGroupCode, userView.getUtilizador(), new Integer(3)};
		try {
			ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);

		}catch (NotAuthorizedException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.noStudentInAttendsSetToDelete");
			actionErrors.add("errors.noStudentInAttendsSetToDelete", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("insucess");
		
		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			// Create an ACTION_ERROR 
			error = new ActionError("errors.removeEnrolment.studentNotEnroled");
			actionErrors.add("errors.removeEnrolment.studentNotEnroled", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewStudentGroupInformation");

		} catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			// Create an ACTION_ERROR 
			error = new ActionError("errors.removeEnrolment.minimumNumberOfElements");
			actionErrors.add("errors.removeEnrolment.minimumNumberOfElements", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewStudentGroupInformation");

		} catch (FenixServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");
		}

		ISiteComponent viewStudentGroup;
		Object[] args = { studentGroupCode };

		try {
			viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView, "ReadStudentGroupInformation", args);

		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");
		}catch (FenixServiceException e){
			throw new FenixActionException(e);
		}
		
		InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

		request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

		return mapping.findForward("sucess");
	}
	
	

	public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException, FenixFilterException {

		HttpSession session = request.getSession(false);
		IUserView userView = getUserView(request);
		String userName = userView.getUtilizador();

		String studentGroupCodeString = request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);

		Object[] args1 = { userName, studentGroupCode };
		Boolean shiftWithGroups;
		try {
			shiftWithGroups = (Boolean) ServiceUtils.executeService(userView, "UnEnrollStudentInGroup", args1);

		}catch (NotAuthorizedException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.noStudentInAttendsSetToDelete");
			actionErrors.add("errors.noStudentInAttendsSetToDelete", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("insucess");

		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");

		}catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.removeEnrolment.studentNotEnroled");
			actionErrors.add("errors.removeEnrolment.studentNotEnroled", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewStudentGroupInformation");

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if(!shiftWithGroups.booleanValue())
			return mapping.findForward("viewShiftsAndGroups");
		
		return mapping.findForward("viewStudentGroupInformation");

	}

}
