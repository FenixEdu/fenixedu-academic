/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.student
 * 
 * Created on 20/Dez/2003
 *
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoEnrolmentServiceResult;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ShiftEnrolmentAction extends Action {
	public static String INFO_STUDENT_KEY = "infoStudent";
	
	
	public static String SHIFT_FULL_ERROR = "errors.shift.full";
	public static String SHIFT_NON_EXISTING_ERROR = "errors.shift.nonExisting";
	public static String STUDENT_NON_EXISTING_ERROR = "errors.student.nonExisting";
	public static String STUDENT_NOT_ENROLED_IN_EXECUTION_COURSE_ERROR = "errors.student.notEnroledInExecutionCourse";	
	public static String UNKNOWN_ERROR = "errors.unknown";	
	
	public static String PARAMETER_SHIFT_NAME = "shiftName";
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaValidatorForm enrolForm = (DynaValidatorForm) form;
		String shiftName = (String) enrolForm.get(PARAMETER_SHIFT_NAME);

		HttpSession session = request.getSession(false);

		InfoStudent infoStudent =
			(InfoStudent) session.getAttribute(INFO_STUDENT_KEY);

		InfoShift infoShift =
			new InfoShift(shiftName, null, null, null);
		
		Object args[] = { infoStudent, infoShift };
		InfoEnrolmentServiceResult result =
			(InfoEnrolmentServiceResult) ServiceUtils.executeService(
				SessionUtils.getUserView(request),
				"StudentShiftEnrolment",
				args);
		ActionErrors actionErrors = getActionErrors(result, infoStudent, infoShift);

		if (actionErrors.isEmpty()){
			return mapping.findForward("sucess");
		}
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		
	}
	/**
	 * Method getActionErrors.
	 * @param result
	 * @return ActionErrors
	 */
	private ActionErrors getActionErrors(InfoEnrolmentServiceResult result, InfoStudent infoStudent, InfoShift infoShift) {
		ActionErrors actionErrors = new ActionErrors();
		switch (result.getMessageType()) {
			case InfoEnrolmentServiceResult.ENROLMENT_SUCESS :
				break;
			case InfoEnrolmentServiceResult.SHIFT_FULL :
				actionErrors.add(
					SHIFT_FULL_ERROR,
					new ActionError(SHIFT_FULL_ERROR, infoShift.getNome()));
				break;
			case InfoEnrolmentServiceResult.NON_EXISTING_SHIFT :
			actionErrors.add(
				SHIFT_NON_EXISTING_ERROR,
				new ActionError(SHIFT_NON_EXISTING_ERROR, infoShift.getNome()));
				break;
			case InfoEnrolmentServiceResult.NON_EXISTING_STUDENT :
			actionErrors.add(
				STUDENT_NON_EXISTING_ERROR,
				new ActionError(STUDENT_NON_EXISTING_ERROR, infoStudent.getNumber()));
				break;
			case InfoEnrolmentServiceResult.NOT_ENROLED_INTO_EXECUTION_COURSE :
				actionErrors.add(
					STUDENT_NOT_ENROLED_IN_EXECUTION_COURSE_ERROR,
					new ActionError(STUDENT_NOT_ENROLED_IN_EXECUTION_COURSE_ERROR, infoShift.getNome()));
				break;
			default :
				actionErrors.add(UNKNOWN_ERROR,new ActionError(UNKNOWN_ERROR));
				break;
		}
		return actionErrors;
	}

}
