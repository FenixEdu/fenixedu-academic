package ServidorApresentacao.Action.student.enrollment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.degreeAdministrativeOffice.CurricularCoursesEnrollmentDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/*
 * 
 * @author Fernanda Quitério 7/Fev/2004
 *  
 */
public class StudentCurricularCoursesEnrollmentDispatchAction extends DispatchAction
{

	public ActionForward prepareEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
		IUserView userView = SessionUtils.getUserView(request);

		System.out.println("-->entrando no form de inscricao no estudante");
		InfoStudent infoStudent = new InfoStudent();
		Object[] args = { userView.getUtilizador()};
		try
		{
			infoStudent =
				(InfoStudent) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentByUsername",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		enrollmentForm.set("studentNumber", infoStudent.getNumber().toString());

		System.out.println("-->estudante: " + infoStudent.getNumber());

		return mapping.findForward("curricularCoursesEnrollment");
	}
}