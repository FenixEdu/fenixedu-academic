package ServidorApresentacao.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CurriculumDispatchAction extends DispatchAction {

	public ActionForward getCurriculum(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		GestorServicos serviceManager = GestorServicos.manager();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		

		Integer studentCurricularPlanID = Integer.valueOf(request.getParameter("studentCPID"));

		List result = null;
		
		
		try {
			Object args[] = { userView, studentCurricularPlanID };
			result = (ArrayList) serviceManager.executar(userView, "ReadStudentCurriculum", args);
		} catch (NotAuthorizedException e) {
			return mapping.findForward("NotAuthorized");
		}

		BeanComparator curricularYear = new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year");
		BeanComparator semester = new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.semester");
		BeanComparator courseName = new BeanComparator("infoCurricularCourseScope.infoCurricularCourse.name");
		BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(curricularYear);
		chainComparator.addComparator(semester);
		chainComparator.addComparator(courseName);
		chainComparator.addComparator(executionYear);
		
		Collections.sort(result, chainComparator);


		
		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		try {
			Object args[] = { studentCurricularPlanID };
			infoStudentCurricularPlan = (InfoStudentCurricularPlan) serviceManager.executar(userView, "ReadStudentCurricularPlan", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}

		request.setAttribute(SessionConstants.CURRICULUM, result);
		request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
		

		return mapping.findForward("ShowStudentCurriculum");
	}

	public ActionForward getStudentCP(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		GestorServicos serviceManager = GestorServicos.manager();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		

		List result = null;
		try {
			Object args[] = { userView };
			result = (ArrayList) serviceManager.executar(userView, "ReadStudentCurricularPlans", args);
		} catch (NonExistingServiceException e) {
			request.setAttribute("studentCPs", new ArrayList());
			return mapping.findForward("ShowStudentCurricularPlans");
		}
		
		request.setAttribute("studentCPs", result);

		return mapping.findForward("ShowStudentCurricularPlans");
	}



}