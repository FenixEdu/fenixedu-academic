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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ChooseExecutionCourseAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;
		
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);


		List infoCourseList = SessionUtils.getExecutionCourses(request);
	
		String courseInitials = (String) chooseCourseForm.get("courseInitials");

		if (courseInitials != null && !courseInitials.equals("")) {
			InfoExecutionCourse infoCourse = new InfoExecutionCourse();
			
			infoCourse.setInfoExecutionPeriod(infoExecutionPeriod);
			infoCourse.setSigla(courseInitials);
			
			int index = infoCourseList.indexOf(infoCourse);
			infoCourse = (InfoExecutionCourse) infoCourseList.get(index);

			session.setAttribute(
				SessionConstants.EXECUTION_COURSE_KEY,
				infoCourse);
			return mapping.findForward("forwardChoose");
		} else {
			return mapping.findForward("showForm");
		}
	}

}
