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

import DataBeans.ISiteComponent;
import DataBeans.InfoShift;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoSiteShifts;
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
public class GroupEnrolmentDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String groupPropertiesCodeString =
			request.getParameter("groupPropertiesCode");

		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

		ISiteComponent emptyShifts = null;
		Object[] args1 = { groupPropertiesCode,userView.getUtilizador() };
		System.out.println("PREPARE ENROLMENT ANTES DO SERVICO");
		try {
			emptyShifts =
				(ISiteComponent) ServiceUtils.executeService(
					userView,
					"ReadGroupPropertiesShifts",
					args1);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		System.out.println("JA ESTOU ISNCRITO "+emptyShifts);
		if(emptyShifts==null)
		{
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			// Create an ACTION_ERROR 
			error = new ActionError("errors.existing.groupStudentEnrolment");
			actionErrors.add("errors.existing.groupStudentEnrolment", error);
			saveErrors(request, actionErrors);
			request.setAttribute("groupPropertiesCode",groupPropertiesCode);
			return mapping.findForward("viewProjectStudentGroups");
		}
		
		List shifts = ((InfoSiteShifts)emptyShifts).getShifts();
		ArrayList shiftsList = new ArrayList();
		InfoShift oldInfoShift = new InfoShift();
		if (shifts.size() != 0) 
		{
			shiftsList.add(new LabelValueBean("(escolher)", ""));
			InfoShiftWithAssociatedInfoClassesAndInfoLessons infoShift;
			Iterator iter = shifts.iterator();
			String label, value;
			while (iter.hasNext()) {
				infoShift =
					(InfoShiftWithAssociatedInfoClassesAndInfoLessons) iter
						.next();
				value = infoShift.getInfoShift().getIdInternal().toString();
				label = infoShift.getInfoShift().getNome();
				shiftsList.add(new LabelValueBean(label, value));
			}
		request.setAttribute("shiftsList", shiftsList);
		System.out.println("SHIFT LIST SIZE"+shiftsList.size());
		}
		//else
		//TODO groupProperties sem turno 

		List studentsNotEnroled = null;
		Object[] args2 = { groupPropertiesCode, userView.getUtilizador()};
		try {
				studentsNotEnroled =
					(List) ServiceUtils.executeService(
						userView,
						"ReadStudentsWithoutGroup",
						args2);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

		request.setAttribute("infoStudents", studentsNotEnroled);
		request.setAttribute("groupPropertiesCode",groupPropertiesCode);
		System.out.println("INFOSTUDENTS SIZE"+studentsNotEnroled.size());
		return mapping.findForward("sucess");

		}

		public ActionForward enrolment(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
			
				System.out.println("ENTRA NA ACCAO ENROLMENT");
			HttpSession session = request.getSession(false);
			DynaActionForm enrolmentForm = (DynaActionForm) form;
			
			IUserView userView =(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			String groupPropertiesCodeString =request.getParameter("groupPropertiesCode");

			Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
			
			String shiftCodeString =(String)enrolmentForm.get("shift");
			if (shiftCodeString.equals("")) 
			{
				return mapping.findForward("viewProjectStudentGroups");
			}
					
			Integer shiftCode = new Integer(shiftCodeString);

			List studentCodes =Arrays.asList((Integer[]) enrolmentForm.get("studentsNotEnroled"));

			
			Object[] args = { groupPropertiesCode,shiftCode, studentCodes,userView.getUtilizador()};
			Integer result;
			try {

				result =(Integer) ServiceUtils.executeService(userView,"GroupEnrolment",args);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			System.out.println("NA ACCAO ENROLMENT DEPOIS DO SERVICO RESULT"+result.toString());
			
			switch(result.intValue())
			{
				case -1:	
					ActionErrors actionErrors1 = new ActionErrors();
					ActionError error1 = null;
					// Create an ACTION_ERROR 
					error1 = new ActionError("errors.existing.groupStudentEnrolment");
				    actionErrors1.add("errors.existing.groupStudentEnrolment",error1);
					saveErrors(request, actionErrors1);
					return mapping.findForward("viewProjectStudentGroups");
					
				case -2:
					ActionErrors actionErrors2 = new ActionErrors();
					ActionError error2 = null;
					// Create an ACTION_ERROR 
					error2 = new ActionError("errors.fullShift.groupStudentEnrolment");
					actionErrors2.add("errors.fullShift.groupStudentEnrolment",error2);
					saveErrors(request, actionErrors2);		
					return mapping.findForward("sucess");
			}
			
			request.setAttribute("groupPropertiesCode",groupPropertiesCode);
			return mapping.findForward("viewProjectStudentGroups");

		}

	}