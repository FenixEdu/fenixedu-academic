package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoSiteEnrolmentEvaluation;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 01/07/2003
 * 
 */
public class SubmitMarksAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		String executionYear = (String) request.getParameter("executionYear");
		String degree = request.getParameter("degree");
		String curricularCourse = request.getParameter("curricularCourse");
		Integer curricularCourseCode = new Integer(request.getParameter("curricularCourseCode"));

		// Get students List			
		Object args[] = { curricularCourseCode };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
		try {
			infoSiteEnrolmentEvaluation =
				(InfoSiteEnrolmentEvaluation) serviceManager.executar(userView, "ReadStudentsAndMarksByCurricularCourse", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}

		Collections.sort(
			infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(),
			new BeanComparator("infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

		//		fill in teacher number in case it exists
		DynaValidatorForm submitMarksForm = (DynaValidatorForm) form;
		submitMarksForm.set("teacherNumber", infoSiteEnrolmentEvaluation.getInfoTeacher().getTeacherNumber());

		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("curricularCourseCode", curricularCourseCode);

		request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

		return mapping.findForward("MarksSubmission");

	}
}