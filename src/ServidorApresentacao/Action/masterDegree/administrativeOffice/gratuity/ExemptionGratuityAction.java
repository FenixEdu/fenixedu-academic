/*
 * Created on 5/Jan/2004
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.ExemptionGratuityType;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class ExemptionGratuityAction extends DispatchAction
{

	public ActionForward chooseStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session  = request.getSession();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		//execution years
		List executionYears = null;
		Object[] args = {
		};
		try
		{
			executionYears = (List) ServiceManagerServiceFactory.executeService(null, "ReadExecutionYears", args);

		}
		catch (FenixServiceException e)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		} 
		if (executionYears == null || executionYears.size() <= 0)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		request.setAttribute("executionYears", executionYears);

		//specialitions
		/*ArrayList specializations = Specialization.toArrayListWithEssentials();
		if (specializations == null || specializations.size() <= 0)
		{
			errors.add("noSpecializations", new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		request.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);*/

		return mapping.findForward("chooseStudent");
	}

	public ActionForward readStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session  = request.getSession();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read parameters
		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);
		
		String parameter = (String) request.getParameter("studentNumber");
		Integer studentNumber = null;
		if (parameter != null)
		{
			studentNumber = new Integer(parameter);
		}
		request.setAttribute("studentNumber", studentNumber);
			
		List studentCurricularPlans = null;
		Object[] args = { studentNumber, TipoCurso.MESTRADO_OBJ };
		try
		{
			studentCurricularPlans = (List) ServiceManagerServiceFactory.executeService(null, "ReadStudentCurricularPlansByNumberAndDegreeType", args);
		}
		catch(FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		} 
		
		if(studentCurricularPlans.size() == 1){
			request.setAttribute("studentCurricularPlanID", ((InfoStudentCurricularPlan)studentCurricularPlans.get(0)).getIdInternal());
			return mapping.findForward("readExemptionGratuity");
		} else {
			request.setAttribute("studentCurricularPlans", studentCurricularPlans);
			return mapping.findForward("chooseStudentCurricularPlan");
		}		
	}
	
	public ActionForward readExemptionGratuity(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session  = request.getSession();
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		Integer studentCurricularPlanID = (Integer) request.getAttribute("studentCurricularPlanID");

		InfoStudentCurricularPlan studentCurricularPlan = null;
		Object[] args = { studentCurricularPlanID };
		try
		{
			studentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(null, "ReadStudentCurricularPlan", args);
		}
		catch(FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		} 
		
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);
		request.setAttribute("percentageOfExemption", ExemptionGratuityType.percentageOfExemption());
		request.setAttribute("exemptionGratuityList", ExemptionGratuityType.getEnumList());
		
		return mapping.findForward("manageExemptionGratuity");
	}	
	
	public ActionForward insertExemptionGratuity(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
	
		return mapping.findForward("manageExemptionGratuity");
	}
}
