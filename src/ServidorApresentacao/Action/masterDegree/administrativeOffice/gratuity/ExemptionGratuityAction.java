/*
 * Created on 5/Jan/2004
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoEmployee;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPerson;
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

	public ActionForward prepareReadStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		//execution years
		List executionYears = null;
		Object[] args = {
		};
		try
		{
			executionYears =
				(List) ServiceManagerServiceFactory.executeService(
					null,
					"ReadNotClosedExecutionYears",
					args);
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

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("year"), true);
		Collections.sort(executionYears, comparator);

		List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
		request.setAttribute("executionYears", executionYearLabels);

		DynaActionForm studentForm = (DynaActionForm) actionForm;
		studentForm.set("studentNumber", null);
		
		return mapping.findForward("chooseStudent");
	}

	private List buildLabelValueBeanForJsp(List infoExecutionYears)
	{
		List executionYearLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionYears, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear =
					new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}

	public ActionForward readStudent(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//Read parameters
		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);

		String parameter = request.getParameter("studentNumber");
		Integer studentNumber = null;
		try
		{
			studentNumber = new Integer(parameter);
		}
		catch (NumberFormatException e)
		{
			errors.add("errors", new ActionError("error.tutor.numberAndRequired"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		request.setAttribute("studentNumber", studentNumber);

		List studentCurricularPlans = null;
		Object[] args = { studentNumber, TipoCurso.MESTRADO_OBJ };
		try
		{
			studentCurricularPlans =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentCurricularPlansByNumberAndDegreeTypeInMasterDegree",
					args);
		}
		catch (FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (studentCurricularPlans.size() == 1)
		{
			request.setAttribute(
				"studentCurricularPlanID",
				((InfoStudentCurricularPlan) studentCurricularPlans.get(0)).getIdInternal());
			return mapping.findForward("readExemptionGratuity");
		}
		
			request.setAttribute("studentCurricularPlans", studentCurricularPlans);
			return mapping.findForward("chooseStudentCurricularPlan");
		
	}

	public ActionForward readExemptionGratuity(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		request.setAttribute("percentageOfExemption", ExemptionGratuityType.percentageOfExemption());
		request.setAttribute("exemptionGratuityList", ExemptionGratuityType.getEnumList());

		//Read executionYear
		String executionYear = (String) request.getAttribute("executionYear");
		if (executionYear == null)
		{
			executionYear = request.getParameter("executionYear");
		}
		request.setAttribute("executionYear", executionYear);

		Integer studentCurricularPlanID = getFromRequest("studentCurricularPlanID", request);
		request.setAttribute("studentCurricularPlanID", studentCurricularPlanID);

		//read student curricular plan only for show in jsp
		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		Object[] args = { studentCurricularPlanID };
		try
		{
			infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentCurricularPlanInMasterDegree",
					args);
		}
		catch (FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

		//read gratuity values of the execution course
		InfoGratuityValues infoGratuityValues = null;
		Object args3[] =
			{ infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getIdInternal(), executionYear };
		try
		{
			infoGratuityValues =
				(InfoGratuityValues) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear",
					args3);
		}
		catch (FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add(
				"noGratuitySituation",
				new ActionError("error.impossible.insertExemptionGratuity"));
			errors.add(
				"noGratuityValues",
				new ActionError(
					"error.impossible.problemsWithDegree",
					infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getInfoDegree().getNome()));
			saveErrors(request, errors);
			return mapping.findForward("chooseStudent");
		}
		//if infoGratuityValues is null than it will be informed to the user
		//that this degree hasn't gratuity values defined
		if (infoGratuityValues == null)
		{
			request.setAttribute("noGratuityValues", "true");
			errors.add("noGratuityValues", new ActionError("error.impossible.noGratuityValues"));
			saveErrors(request, errors);
			return mapping.findForward("chooseStudent");
		}
		
			request.setAttribute("gratuityValuesID", infoGratuityValues.getIdInternal());
		

		//read gratuity situation of the student
		InfoGratuitySituation infoGratuitySituation = null;
		Object args2[] = { studentCurricularPlanID, infoGratuityValues.getIdInternal()};
		try
		{
			infoGratuitySituation =
				(InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadGratuitySituationByStudentCurricularPlanByGratuityValues",
					args2);
		}
		catch (FenixServiceException fenixServiceException)
		{
			fenixServiceException.printStackTrace();
			errors.add(
				"noGratuitySituation",
				new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		//if infoGratuitySituation is null than it will be created in next step
		if (infoGratuitySituation != null)
		{
			request.setAttribute("gratuitySituationID", infoGratuitySituation.getIdInternal());
		}

		DynaActionForm exemptionGrauityForm = (DynaActionForm) actionForm;
		fillForm(infoGratuitySituation, request, exemptionGrauityForm);

		return mapping.findForward("manageExemptionGratuity");
	}

	private void fillForm(
		InfoGratuitySituation infoGratuitySituation,
		HttpServletRequest request,
		DynaActionForm exemptionGrauityForm)
	{
		if (infoGratuitySituation != null)
		{
			Integer exemptionPercentage = infoGratuitySituation.getExemptionPercentage();
			if (exemptionPercentage != null)
			{
				if (ExemptionGratuityType.percentageOfExemption().contains(exemptionPercentage))
				{
					exemptionGrauityForm.set(
						"valueExemptionGratuity",
						String.valueOf(exemptionPercentage));
				}
				else if (exemptionPercentage.intValue() > 0)
				{
					exemptionGrauityForm.set("valueExemptionGratuity", "-1");
					exemptionGrauityForm.set(
						"otherValueExemptionGratuity",
						String.valueOf(exemptionPercentage));
				}
			}

			if (infoGratuitySituation.getExemptionType() != null)
			{
				exemptionGrauityForm.set(
					"justificationExemptionGratuity",
					String.valueOf(infoGratuitySituation.getExemptionType().getValue()));
			}
			exemptionGrauityForm.set(
				"otherJustificationExemptionGratuity",
				infoGratuitySituation.getExemptionDescription());
		}
	}

	public ActionForward insertExemptionGratuity(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm exemptionForm = (DynaActionForm) actionForm;
		InfoGratuitySituation infoGratuitySituation =
			fillInfoGratuityValues(userView, request, exemptionForm);

		Object[] args = { infoGratuitySituation };
		try
		{
			infoGratuitySituation =
				(InfoGratuitySituation) ServiceManagerServiceFactory.executeService(
					userView,
					"EditGratuitySituationById",
					args);

		}
		catch (FenixServiceException exception)
		{
			exception.printStackTrace();
			errors.add(
				"insertExemptionGratuity",
				new ActionError("error.impossible.insertExemptionGratuity"));
			saveErrors(request, errors);
			mapping.getInputForward();
		}
		request.setAttribute("exemptionGratuity", infoGratuitySituation);

		return mapping.findForward("confirmationExemptionGratuity");
	}

	private InfoGratuitySituation fillInfoGratuityValues(
		IUserView userView,
		HttpServletRequest request,
		DynaActionForm exemptionForm)
	{
		Integer valueExemptionGratuity =
			Integer.valueOf((String) exemptionForm.get("valueExemptionGratuity"));
		Integer justificationExemptionGratuity =
			Integer.valueOf((String) exemptionForm.get("justificationExemptionGratuity"));

		String otherValueExemptionGratuityString =
			(String) exemptionForm.get("otherValueExemptionGratuity");
		Integer otherValueExemptionGratuity = null;
		if (otherValueExemptionGratuityString != null && otherValueExemptionGratuityString.length() > 0)
		{
			otherValueExemptionGratuity =
				Integer.valueOf((String) exemptionForm.get("otherValueExemptionGratuity"));
		}
		String otherJustificationExemptionGratuity =
			(String) exemptionForm.get("otherJustificationExemptionGratuity");

		String studentCurricularPlanID = request.getParameter("studentCurricularPlanID");
		request.setAttribute("studentCurricularPlanID", studentCurricularPlanID);
		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);
		String gratuitySituationID = request.getParameter("gratuitySituationID");
		String gratuityValuesID = request.getParameter("gratuityValuesID");

		InfoGratuitySituation infoGratuitySituation = new InfoGratuitySituation();
		if (gratuitySituationID != null)
		{
			infoGratuitySituation.setIdInternal(Integer.valueOf(gratuitySituationID));
		}
		//value
		if (valueExemptionGratuity != null)
		{
			infoGratuitySituation.setExemptionPercentage(valueExemptionGratuity);
			if (otherValueExemptionGratuity != null && valueExemptionGratuity.equals(new Integer(-1)))
			{
				infoGratuitySituation.setExemptionPercentage(otherValueExemptionGratuity);
			}
		}
		//justification
		if (justificationExemptionGratuity != null)
		{
			infoGratuitySituation.setExemptionType(
				ExemptionGratuityType.getEnum(justificationExemptionGratuity.intValue()));
			if (justificationExemptionGratuity.equals(new Integer(ExemptionGratuityType.OTHER_TYPE)))
			{
				infoGratuitySituation.setExemptionDescription(otherJustificationExemptionGratuity);
			}
		}

		//Student Curricular Plan
		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		infoStudentCurricularPlan.setIdInternal(Integer.valueOf(studentCurricularPlanID));
		infoGratuitySituation.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

		//Gratuity Values
		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		infoGratuityValues.setIdInternal(Integer.valueOf(gratuityValuesID));
		infoGratuitySituation.setInfoGratuityValues(infoGratuityValues);

		//employee who made register
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setUsername(userView.getUtilizador());

		InfoEmployee infoEmployee = new InfoEmployee();
		infoEmployee.setPerson(infoPerson);

		infoGratuitySituation.setInfoEmployee(infoEmployee);

		return infoGratuitySituation;
	}

	public ActionForward removeExemptionGratuity(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentCurricularPlanID = request.getParameter("studentCurricularPlanID");
		request.setAttribute("studentCurricularPlanID", studentCurricularPlanID);
		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);

		String gratuitySituationIDString = request.getParameter("gratuitySituationID");
		Integer gratuitySituationID = null;
		try
		{
			gratuitySituationID = Integer.valueOf(gratuitySituationIDString);
		}
		catch (NumberFormatException exception)
		{
			errors.add(
				"removeExemptionGratuity",
				new ActionError("error.impossible.removeExemptionGratuity"));
			saveErrors(request, errors);
			mapping.getInputForward();
		}

		Object[] args = { gratuitySituationID };
		Boolean result = Boolean.FALSE;
		try
		{
			result =
				(Boolean) ServiceManagerServiceFactory.executeService(
					userView,
					"DeleteGratuitySituationById",
					args);
		}
		catch (FenixServiceException exception)
		{
			exception.printStackTrace();
			errors.add(
				"removeExemptionGratuity",
				new ActionError("error.impossible.removeExemptionGratuity"));
			saveErrors(request, errors);
			mapping.getInputForward();
		}
		request.setAttribute("removeExemptionGratuity", result.toString());

		return mapping.findForward("confirmationExemptionGratuity");
	}

	private Integer getFromRequest(String parameter, HttpServletRequest request)
	{
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString != null) //parameter
		{
			try
			{
				parameterCode = new Integer(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		else //request
			{
			if (request.getAttribute(parameter) instanceof String)
			{
				try
				{
					parameterCode = new Integer((String) request.getAttribute(parameter));
				}
				catch (Exception exception)
				{
					return null;
				}
			}
			else if (request.getAttribute(parameter) instanceof Integer)
			{
				parameterCode = (Integer) request.getAttribute(parameter);
			}
		}
		return parameterCode;
	}
}
