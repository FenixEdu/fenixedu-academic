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

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

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

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		String studentCurricularPlanID = request.getParameter("studentCPID");
		if(studentCurricularPlanID == null) {
			studentCurricularPlanID = (String) request.getAttribute("studentCPID");
		}
		
		List result = null;
		try {
			Object args[] = { null, Integer.valueOf(studentCurricularPlanID) };
			result = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadStudentCurriculum", args);
		} catch (NotAuthorizedException e) {
			return mapping.findForward("NotAuthorized");
		}

//		BeanComparator curricularYear = new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year");
//		BeanComparator semester = new BeanComparator("infoCurricularCourseScope.infoCurricularSemester.semester");
		BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
		BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
		ComparatorChain chainComparator = new ComparatorChain();
//		chainComparator.addComparator(curricularYear);
//		chainComparator.addComparator(semester);
		chainComparator.addComparator(courseName);
		chainComparator.addComparator(executionYear);
		
		Collections.sort(result, chainComparator);
		
		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		try {
			Object args[] = { Integer.valueOf(studentCurricularPlanID) };
			infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(userView, "ReadStudentCurricularPlan", args);
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
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		String studentNumber = request.getParameter("studentNumber");
		if(studentNumber == null) {
			studentNumber = (String) request.getAttribute("studentNumber");
		}
		if(studentNumber == null) {
			InfoStudent infoStudent = null;
			try {
				Object args[] = { userView };
				infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView, "ReadStudentByUsername", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			studentNumber = infoStudent.getNumber().toString();
		}
		
		List result = null;
		try {
			Object args[] = { Integer.valueOf(studentNumber), TipoCurso.LICENCIATURA_OBJ };
			result = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadStudentCurricularPlans", args);
		} catch (NonExistingServiceException e) {
			result = new ArrayList();
		}
		
		request.setAttribute("studentCPs", result);

		return mapping.findForward("ShowStudentCurricularPlans");
	}



}