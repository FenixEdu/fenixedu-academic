/*
 * Created on 20/Jan/2004
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Specialization;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *
 */
public class StudentsGratuityListAction extends DispatchAction
{


	public ActionForward chooseExecutionYear(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
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
			throw new FenixActionException();
		}

		if (executionYears != null && !executionYears.isEmpty())
		{
			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("year"), true);
			Collections.sort(executionYears, comparator);

			List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
			request.setAttribute("executionYears", executionYearLabels);
		}
		
		return mapping.findForward("chooseStudentList");
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
	
	

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward prepareChooseDegree(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		System.out.println("-->aqui");
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm studentListForm = (DynaActionForm) actionForm;
		String executionYear = (String) studentListForm.get("executionYear");

		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		Object args[] = { infoExecutionYear };
		List executionDegreeList = null;
		try
		{
			executionDegreeList =
			(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList);

		request.setAttribute(SessionConstants.DEGREES, executionDegreeLabels);
		request.setAttribute("specializations", Specialization.toArrayList());
		request.setAttribute("showNextSelects", "true");
		
		return chooseExecutionYear(mapping, actionForm, request, response);
	}
	
	private List buildExecutionDegreeLabelValueBean(List executionDegreeList)
	{
		List executionDegreeLabels = new ArrayList();
		Iterator iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name =
			infoExecutionDegree
			.getInfoDegreeCurricularPlan()
			.getInfoDegree()
			.getTipoCurso()
			.toString()
			+ " em "
			+ name;

			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
			? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
			: "";

			executionDegreeLabels.add(
					new LabelValueBean(name, name + "#" + infoExecutionDegree.getIdInternal().toString()));
		}
		return executionDegreeLabels;
	}

	private boolean duplicateInfoDegree(
			List executionDegreeList,
			InfoExecutionDegree infoExecutionDegree)
	{
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
					&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}

}
