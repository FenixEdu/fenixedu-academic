/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoSiteStudentsWithoutGroup;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidStudentNumberServiceException;
import ServidorAplicacao.Servico.exceptions.NoChangeMadeServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *
 */
public class GroupEnrolmentDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		String shiftCodeString = request.getParameter("shiftCode");

		Integer shiftCode = null;
		if(shiftCodeString!=null){
		shiftCode = new Integer(shiftCodeString);
		}
		
		Object[] args1 = { groupPropertiesCode, shiftCode, null, userView.getUtilizador(), new Integer(2)};
		try {

			ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);

		}catch (NotAuthorizedException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors2.add("errors.noStudentInAttendsSet", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("insucess");
		}catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.impossible.nrOfGroups.groupEnrolment");
			actionErrors2.add("errors.impossible.nrOfGroups.groupEnrolment", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("viewShiftsAndGroups");
		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors2.add("errors.existing.groupStudentEnrolment", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("viewShiftsAndGroups");

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);

		}

		InfoSiteStudentsWithoutGroup studentsNotEnroled = null;
		Object[] args3 = { groupPropertiesCode, userView.getUtilizador()};
		try {
			studentsNotEnroled =
				(InfoSiteStudentsWithoutGroup) ServiceUtils.executeService(userView, "ReadStudentsWithoutGroup", args3);

		} catch (FenixServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("error.existingGroup");
			actionErrors1.add("error.existingGroup", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);

		}

		List infoStudentList = studentsNotEnroled.getInfoStudentList();
		if (infoStudentList != null) {
			Collections.sort(infoStudentList, new BeanComparator("number"));
			request.setAttribute("infoStudents", infoStudentList);
		}
		request.setAttribute("groupNumber", studentsNotEnroled.getGroupNumber());
		request.setAttribute("groupPropertiesCode", groupPropertiesCode);
		request.setAttribute("shiftCode", shiftCode);

		return mapping.findForward("sucess");

	}
	public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm enrolmentForm = (DynaActionForm) form;

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		String groupNumberString = request.getParameter("groupNumber");
		Integer groupNumber = new Integer(groupNumberString);
		Integer shiftCode = null;	
		String shiftCodeString = request.getParameter("shiftCode");
		if(shiftCodeString!=null){
		shiftCode = new Integer(shiftCodeString);
		}

		List studentCodes = new ArrayList();
		studentCodes = Arrays.asList((Integer[]) enrolmentForm.get("studentsNotEnroled"));

		Object[] args = { groupPropertiesCode, shiftCode, groupNumber, studentCodes, userView.getUtilizador()};
		try {

			ServiceUtils.executeService(userView, "GroupEnrolment", args);
		
		}catch (NonExistingServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("error.noProject");
			actionErrors1.add("error.noProject", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("viewStudentGroupInformation");
		}catch (NoChangeMadeServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors1.add("errors.noStudentInAttendsSet", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("insucess");
		} catch (InvalidStudentNumberServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.noStudentsInAttendsSet");
			actionErrors1.add("errors.noStudentsInAttendsSet", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);
		} catch (InvalidArgumentsServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.impossible.nrOfGroups.groupEnrolment");
			actionErrors1.add("errors.impossible.nrOfGroups.groupEnrolment", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);
		} catch (NonValidChangeServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.impossible.minimumCapacity.groupEnrolment");
			actionErrors1.add("errors.impossible.minimumCapacity.groupEnrolment", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);
		} catch (NotAuthorizedException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.impossible.maximumCapacity.groupEnrolment");
			actionErrors1.add("errors.impossible.maximumCapacity.groupEnrolment", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);

		} catch (ExistingServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.existing.elementsEnrolment");
			actionErrors1.add("errors.existing.elementsEnrolment", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);

		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors1.add("errors.existing.groupStudentEnrolment", error1);
			saveErrors(request, actionErrors1);
			return mapping.findForward("viewShiftsAndGroups");
			
		} catch (FenixServiceException e) {
		   ActionErrors actionErrors1 = new ActionErrors();
		   ActionError error1 = null;
		   // Create an ACTION_ERROR 
		   error1 = new ActionError("error.existingGroup");
		   actionErrors1.add("error.existingGroup", error1);
		   saveErrors(request, actionErrors1);
		   return prepareEnrolment(mapping, form, request, response);

		}

		request.setAttribute("groupPropertiesCode", groupPropertiesCode);
		request.setAttribute("shiftCode", shiftCode);

		return mapping.findForward("viewShiftsAndGroups");

	}

}