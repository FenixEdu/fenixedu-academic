/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.base.FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ChooseExecutionCourseAction
	extends FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		super.execute(mapping, form, request, response);

		//HttpSession session = request.getSession(false);

		DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		List infoCourseList = SessionUtils.getExecutionCourses(request);

		String courseInitials = (String) chooseCourseForm.get("courseInitials");

		if (courseInitials != null && !courseInitials.equals("")) {
			InfoExecutionCourse infoCourse = new InfoExecutionCourse();

			infoCourse.setInfoExecutionPeriod(infoExecutionPeriod);
			infoCourse.setSigla(courseInitials);

			int index = infoCourseList.indexOf(infoCourse);
			infoCourse = (InfoExecutionCourse) infoCourseList.get(index);

			request.setAttribute(
				SessionConstants.EXECUTION_COURSE,
				infoCourse);
			return mapping.findForward("forwardChoose");
		} else {
			request.removeAttribute(SessionConstants.EXECUTION_COURSE);
			//request.removeAttribute(SessionConstants.CLASS_VIEW);
			return mapping.findForward("showForm");
		}
	}

}