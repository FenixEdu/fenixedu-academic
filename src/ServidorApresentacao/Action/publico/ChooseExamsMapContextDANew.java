/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDANew extends FenixContextDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(true);
		if (session != null) {

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);
					
			Integer degreeId = getFromRequest("degreeID", request);
			request.setAttribute("degreeID", degreeId);

			Integer executionDegreeId = getFromRequest("executionDegreeID", request);
			request.setAttribute("executionDegreeID", executionDegreeId);

			Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
			request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

			Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
			request.setAttribute("inEnglish", inEnglish);		
					
			

			/* Criar o bean de semestres */
			ArrayList semestres = new ArrayList();
			semestres.add(new LabelValueBean("escolher", ""));
			semestres.add(new LabelValueBean("1 º", "1"));
			semestres.add(new LabelValueBean("2 º", "2"));
			request.setAttribute("semesterList", semestres);

			List curricularYearsList = new ArrayList();
			curricularYearsList.add("1");
			curricularYearsList.add("2");
			curricularYearsList.add("3");
			curricularYearsList.add("4");
			curricularYearsList.add("5");
			request.setAttribute("curricularYearList", curricularYearsList);

			/* Cria o form bean com as licenciaturas em execucao.*/
			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List executionDegreeList =
				(List) ServiceUtils.executeService(
					null,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			Collections.sort(
				executionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());

			ArrayList licenciaturas = new ArrayList();

			


			licenciaturas.add(new LabelValueBean("escolher", ""));

			Iterator iterator = executionDegreeList.iterator();

			int index = 0;
			while (iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) iterator.next();
				String name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getNome();

				name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getTipoCurso()
						.toString()
						+ " de "
						+ name;

				name
					+= duplicateInfoDegree(
						executionDegreeList,
						infoExecutionDegree)
					? "-"
						+ infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getName()
					: "";

				licenciaturas.add(
					new LabelValueBean(name, String.valueOf(index++)));
			}

			request.setAttribute("degreeList", licenciaturas);
		

			return mapping.findForward("chooseExamsMapContext");
		} 
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm chooseExamContextoForm = (DynaActionForm) form;		

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			ActionErrors errors = new ActionErrors();
			
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);

			String[] selectedCurricularYears =
				(String[]) chooseExamContextoForm.get(
					"selectedCurricularYears");

			Boolean selectAllCurricularYears =
				(Boolean) chooseExamContextoForm.get(
					"selectAllCurricularYears");

			if ((selectAllCurricularYears != null)
				&& selectAllCurricularYears.booleanValue()) {
				String[] allCurricularYears = { "1", "2", "3", "4", "5" };
				selectedCurricularYears = allCurricularYears;
			}

			List curricularYears =
				new ArrayList(selectedCurricularYears.length);
			for (int i = 0; i < selectedCurricularYears.length; i++)
				curricularYears.add(new Integer(selectedCurricularYears[i]));

			request.setAttribute("curricularYearList", curricularYears);
			Integer indexValue =  getFromRequest("index",request);
		//	int index = indexValue.intValue();
			request.setAttribute("index",indexValue);
			
			Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
			request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
			
			Integer degreeId = getFromRequest("degreeID", request);
			request.setAttribute("degreeID", degreeId);

 
			/*int index =
				Integer.parseInt((String) chooseExamContextoForm.get("index"));
				request.setAttribute("index", chooseExamContextoForm.get("index"));*/

			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List infoExecutionDegreeList =
				(List) ServiceUtils.executeService(
					null,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			Collections.sort(
				infoExecutionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());
				
			    InfoExecutionDegree infoExecutionDegree1 = new InfoExecutionDegree();
				Iterator iterator = infoExecutionDegreeList.iterator();
				while (iterator.hasNext())
				{
					 infoExecutionDegree1=
						(InfoExecutionDegree) iterator.next();
					if (infoExecutionDegree1.getInfoDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId))
					{
				//		request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree1);
						break;
					}
				}
				
				
	/*		InfoExecutionDegree infoExecutionDegree =
							(InfoExecutionDegree) infoExecutionDegreeList.get(index);*/
			InfoExecutionDegree infoExecutionDegree = infoExecutionDegree1;
				
		
		//**************************************/
		
			Object[] args = { degreeId };

			List infoDegreeCurricularPlanList = null;
			try
			{
				infoDegreeCurricularPlanList =
					(List) ServiceManagerServiceFactory.executeService(
						null,
						"ReadPublicDegreeCurricularPlansByDegree",
						args);
			}
			catch (FenixServiceException e)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
			//order the list by state and next by begin date
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("state.degreeState"));
			comparatorChain.addComparator(new BeanComparator("initialDate"), true);

			Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

		
			if (degreeCurricularPlanId != null)
			{
				Iterator iterator1 = infoDegreeCurricularPlanList.iterator();
				while (iterator1.hasNext())
				{
					InfoDegreeCurricularPlan infoDegreeCurricularPlanElem =
						(InfoDegreeCurricularPlan) iterator1.next();
					if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId))
					{
						request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
						break;
					}
				}
			}
	
				
        //***************************************/
			request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
			request.setAttribute(
				SessionConstants.EXECUTION_PERIOD_OID,
				infoExecutionPeriod.getIdInternal().toString());

			if (infoExecutionDegree != null) {

				//added by rspl
					request.setAttribute(
						SessionConstants.EXECUTION_DEGREE,
						infoExecutionDegree);
				//-----------------
				request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal().toString() );
				RequestUtils.setExecutionDegreeToRequest(
					request,
					infoExecutionDegree);
			/*	request.setAttribute(
				"infoDegreeCurricularPlan",
										 (InfoDegreeCurricularPlan)infoExecutionDegree.getInfoDegreeCurricularPlan());*/
	
			} else {
				
				return mapping.findForward("Licenciatura execucao inexistente");
			}

		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

		return mapping.findForward("showExamsMap");

	}

	/**
	 * Method existencesOfInfoDegree.
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree =
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 =
				(InfoExecutionDegree) iterator.next();
			if (infoDegree
				.equals(
					infoExecutionDegree2
						.getInfoDegreeCurricularPlan()
						.getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}
	
	private Integer getFromRequest(String parameter, HttpServletRequest request)
	{
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
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
		return parameterCode;
	}
	private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request)
	{
		Boolean parameterBoolean = null;

		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterBoolean = new Boolean(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		return parameterBoolean;
	}
}
