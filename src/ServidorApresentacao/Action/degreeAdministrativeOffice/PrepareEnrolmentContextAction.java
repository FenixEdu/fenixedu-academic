package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareEnrolmentContextAction extends Action {
	
	private final String[] forwards = { "showAvailableCurricularCourses", "home" };

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[1]);
		}

		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

		InfoStudent infoStudent = (InfoStudent) session.getAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		if(infoStudent == null) {
			// It's probably a refresh to the page so those session attributes were already removed
			// but a infoEnrolmentContext was already added to the session.
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			infoStudent = infoEnrolmentContext.getInfoStudent();
		}

		List infoExecutionDegreesList = (List) session.getAttribute(SessionConstants.DEGREES);
		if(infoExecutionDegreesList == null) {
			// It's probably a refresh to the page so those session attributes were already removed
			// but a infoEnrolmentContext was already added to the session.
			try {
				InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
				Object args[] = { infoExecutionPeriod.getInfoExecutionYear(), infoEnrolmentContext.getChosenOptionalInfoDegree().getTipoCurso() };
				infoExecutionDegreesList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
		}

		Integer infoExecutionDegreeIndex = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("infoExecutionDegreeName"));
		Integer semester = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("semester"));
		Integer year = new Integer((String) getDegreeAndCurricularSemesterAndCurricularYearForm.get("year"));
		
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreesList.get(infoExecutionDegreeIndex.intValue());

		Object args[] = { infoStudent, infoExecutionPeriod, infoExecutionDegree, semester, year };

		InfoEnrolmentContext infoEnrolmentContext = null;
		try {
			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "PrepareEnrolmentContext", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		session.removeAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		session.removeAttribute(SessionConstants.DEGREES);

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		session.setAttribute(SessionConstants.ENROLMENT_STUDENT_NUMBER_KEY, infoStudent.getNumber());
		session.setAttribute(SessionConstants.ENROLMENT_SEMESTER_KEY, semester);
		session.setAttribute(SessionConstants.ENROLMENT_YEAR_KEY, year);
		session.setAttribute(SessionConstants.ENROLMENT_DEGREE_NAME_KEY, infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
		session.setAttribute(SessionConstants.ENROLMENT_CAN_BE_REMOVED_KEY, new Integer(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().size()));

		return mapping.findForward(forwards[0]);
	}

	private boolean isSessionAttributesValid(HttpServletRequest request) {
		boolean result = true;
		HttpSession session = request.getSession();
		
		InfoStudent infoStudent = (InfoStudent) session.getAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		List infoExecutionDegreesList = (List) session.getAttribute(SessionConstants.DEGREES);
		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		
		if( (infoStudent == null) || (infoExecutionDegreesList == null) ) {
			if(infoEnrolmentContext == null) {
				result = false;
			}
		}

		return result;
	}
}