/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
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

		Object[] args1 = { groupPropertiesCode, null, userView.getUtilizador(), new Integer(2)};
		try {
			ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);

		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			// Create an ACTION_ERROR 
			error2 = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors2.add("errors.existing.groupStudentEnrolment", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("viewProjectShifts");

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);

		}

		List shifts = null;
		Object[] args2 = { groupPropertiesCode, null };
		try {
			shifts = (List) ServiceUtils.executeService(userView, "ReadGroupPropertiesShifts", args2);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (shifts.size() == 0) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("errors.fullShift");
			actionErrors.add("errors.fullShift", error);
			saveErrors(request, actionErrors);

			return mapping.findForward("viewProjectShifts");

		} else {
			ArrayList shiftsList = new ArrayList();
			InfoShift oldInfoShift = new InfoShift();

			shiftsList.add(new LabelValueBean("(escolher)", ""));
			InfoShift infoShift;
			Iterator iter = shifts.iterator();
			String label, value;
			while (iter.hasNext()) {
				infoShift = (InfoShift) iter.next();
				value = infoShift.getIdInternal().toString();
				label = infoShift.getNome();
				shiftsList.add(new LabelValueBean(label, value));
			}

			request.setAttribute("shiftsList", shiftsList);

			List studentsNotEnroled = null;
			Object[] args3 = { groupPropertiesCode, userView.getUtilizador()};
			try {
				studentsNotEnroled = (List) ServiceUtils.executeService(userView, "ReadStudentsWithoutGroup", args3);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			if (studentsNotEnroled != null) {
				request.setAttribute("infoStudents", studentsNotEnroled);
			}
			request.setAttribute("groupPropertiesCode", groupPropertiesCode);

			return mapping.findForward("sucess");

		}
	}
	public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		DynaActionForm enrolmentForm = (DynaActionForm) form;

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		String shiftCodeString = (String) enrolmentForm.get("shift");

		if (shiftCodeString.equals("")) {
			ActionErrors actionErrors1 = new ActionErrors();
			ActionError error1 = null;
			// Create an ACTION_ERROR 
			error1 = new ActionError("errors.invalid.shift.groupEnrolment");
			actionErrors1.add("errors.invalid.shift.groupEnrolment", error1);
			saveErrors(request, actionErrors1);
			return prepareEnrolment(mapping, form, request, response);
		}

		Integer shiftCode = new Integer(shiftCodeString);

		List studentCodes = new ArrayList();
		studentCodes = Arrays.asList((Integer[]) enrolmentForm.get("studentsNotEnroled"));

		Object[] args = { groupPropertiesCode, shiftCode, studentCodes, userView.getUtilizador()};
		try {

			ServiceUtils.executeService(userView, "GroupEnrolment", args);

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
			return mapping.findForward("viewProjectShifts");

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

		return mapping.findForward("viewProjectShifts");

	}

}