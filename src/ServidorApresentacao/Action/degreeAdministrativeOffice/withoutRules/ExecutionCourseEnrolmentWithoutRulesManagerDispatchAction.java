/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorApresentacao.Action.degreeAdministrativeOffice.withoutRules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class ExecutionCourseEnrolmentWithoutRulesManagerDispatchAction extends DispatchAction
{

	public ActionForward readExecutionCourseEnrollments(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm studentAndYearForm = (DynaActionForm) form;
		Integer studentNumber = Integer.valueOf((String) studentAndYearForm.get("studentNumber"));
		System.out.println("-->studentNumber: " + studentNumber);
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) studentAndYearForm.get("executionYear");
		System.out.println("-->executionYear: " + executionYear);

		String degreeTypeCode = (String) studentAndYearForm.get("degreeType");
		System.out.println("-->degreeType: " + degreeTypeCode);
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}

		
		Object[] args = { infoStudent, degreeType };
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		try
		{
			infoStudentEnrolmentContext = (InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(userView, "ReadEnrollmentsWithStateEnrolledByStudent", args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			
			errors.add("", new ActionError(e.getMessage()));
			saveErrors(request, errors);
			
			mapping.getInput();
		}
		
		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
		
		return mapping.findForward("curricularCourseEnrollmentList");
	}

}
