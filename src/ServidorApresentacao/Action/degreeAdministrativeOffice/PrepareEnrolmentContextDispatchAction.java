package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.degreeAdministrativeOffice.InfoCurricularCourseEnromentWithoutRules;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareEnrolmentContextDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "showAvailableCurricularCourses", "home", "error" };

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[1]);
		}

		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		InfoCurricularCourseEnromentWithoutRules infoCurricularCourseEnromentWithoutRules = (InfoCurricularCourseEnromentWithoutRules) session.getAttribute(SessionConstants.ENROLMENT_WITHOUT_RULES_INFO_KEY);
		List infoExecutionDegreesList = infoCurricularCourseEnromentWithoutRules.getInfoExecutionDegreesList();

		Integer infoExecutionDegreeIndex = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("infoExecutionDegree"));
		Integer semester = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("semester"));
		Integer year = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("year"));
		
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreesList.get(infoExecutionDegreeIndex.intValue());
		
		infoCurricularCourseEnromentWithoutRules.setChosenInfoExecutionDegree(infoExecutionDegree);
		infoCurricularCourseEnromentWithoutRules.setChosenSemester(semester);
		infoCurricularCourseEnromentWithoutRules.setChosenYear(year);

		session.setAttribute(SessionConstants.ENROLMENT_WITHOUT_RULES_INFO_KEY, infoCurricularCourseEnromentWithoutRules);

		return mapping.findForward(forwards[0]);
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[2]);
	}

	private boolean isSessionAttributesValid(HttpServletRequest request) {
		boolean result = true;
		HttpSession session = request.getSession();
		InfoCurricularCourseEnromentWithoutRules infoCurricularCourseEnromentWithoutRules = (InfoCurricularCourseEnromentWithoutRules) session.getAttribute(SessionConstants.ENROLMENT_WITHOUT_RULES_INFO_KEY);
		if(infoCurricularCourseEnromentWithoutRules == null) {
			result = false;
		}
		return result;
	}
}