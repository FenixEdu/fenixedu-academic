package ServidorApresentacao.Action.degreeAdministrativeOffice.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Mar 5, 2004
 */

public class OptionalCoursesEnrollmentManagerDispatchAction extends DispatchAction
{
	public ActionForward chooseStudentAndExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		String degreeType = request.getParameter("degreeType");

		if (degreeType == null)
		{
			degreeType = (String) request.getAttribute("degreeType");
			if (degreeType == null)
			{
				DynaActionForm dynaActionForm = (DynaActionForm) form;
				degreeType = (String) dynaActionForm.get("degreeType");
			}
		}

		request.setAttribute("degreeType", degreeType);

		List executionYears = null;

		try
		{
			executionYears = (List) ServiceManagerServiceFactory.executeService(null, "ReadNotClosedExecutionYears", null);
		} catch (FenixServiceException e)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
			saveErrors(request, errors);
			return mapping.findForward("globalError");
		}

		if (executionYears == null || executionYears.size() <= 0)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
			saveErrors(request, errors);
			return mapping.findForward("globalError");
		}

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("year"), true);
		Collections.sort(executionYears, comparator);

		List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
		request.setAttribute("executionYears", executionYearLabels);

		return mapping.findForward("chooseStudentAndExecutionYear");
	}

	private List buildLabelValueBeanForJsp(List infoExecutionYears)
	{
		List executionYearLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionYears, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}
	
}