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
	
	private final String[] forwards = { "showAvailableCurricularCourses" };

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getDegreeAndCurricularSemesterAndCurricularYearForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		List infoExecutionDegreesList = (List) session.getAttribute(SessionConstants.DEGREES);
		InfoStudent infoStudent = (InfoStudent) session.getAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);

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

//		FIXME DAVID-RICARDO: Devido a remover estes atributos, se for feito um refresh á página e o código desta action for executado sem executar o código da action que coloca estes atributos na sessão, vai dar uma excepção (java.lang.NullPointerException) na linha 47.
//		session.removeAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
//		session.removeAttribute(SessionConstants.DEGREE_LIST);
//		session.removeAttribute(SessionConstants.DEGREES);

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

		return mapping.findForward(forwards[0]);
	}
}