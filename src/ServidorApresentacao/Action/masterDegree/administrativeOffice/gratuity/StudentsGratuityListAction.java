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

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.GratuitySituationType;
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
		throws Exception
	{

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
			throw new FenixServiceException();
		}

		if (executionYears != null && !executionYears.isEmpty())
		{
			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("year"), true);
			Collections.sort(executionYears, comparator);

			List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
			request.setAttribute("executionYears", executionYearLabels);
		}

		return mapping.findForward("chooseStudentsGratuityList");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward prepareChooseDegree(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

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
			errors.add(
				"impossibleOperation",
				new ActionError("error.masterDegree.gatuyiuty.impossible.operation"));
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
			}
			return mapping.findForward("choose");
		}

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList);

		request.setAttribute(SessionConstants.DEGREES, executionDegreeLabels);
		request.setAttribute("specializations", Specialization.toArrayListWithoutDefault());
		request.setAttribute("situations", GratuitySituationType.getEnumList());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward studentsGratuityList(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		//read data from form
		DynaActionForm studentGratuityListForm = (DynaActionForm) actionForm;
		String executionYear = (String) studentGratuityListForm.get("executionYear");
		String specialization = (String) studentGratuityListForm.get("specialization");
		String situation = (String) studentGratuityListForm.get("situation");
		String degree = (String) studentGratuityListForm.get("degree");
		Integer executionDegreeId = null;
		try {
			executionDegreeId = findExecutionDegreeId(degree);
		} catch(NumberFormatException exception){
			errors.add("noList", new ActionError("error.masterDegree.gatuyiuty.impossible.studentsGratuityList"));
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
			}
			return mapping.getInputForward();
		}
		//put data in request
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("specialization", specialization);
		request.setAttribute("degree", degree);
		request.setAttribute("situation", situation);

		System.out.println("====================================================");
		System.out.println("studentGratuityListForm.executionYear = " + executionYear);
		System.out.println("studentGratuityListForm.specialization = " + specialization);
		System.out.println("studentGratuityListForm.degree = " + degree);
		System.out.println("studentGratuityListForm.situation = " + situation);
		System.out.println("====================================================");


		Object[] args = { executionDegreeId, executionYear, specialization, situation };
		List infoGratuitySituationList = null;
		try
		{
			infoGratuitySituationList =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadGratuitySituationListByExecutionDegreeAndSpecialization",
					args);
		}
		catch (FenixServiceException exception)
		{
			errors.add("noList", new ActionError(exception.getLocalizedMessage()));
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
			}
			return mapping.getInputForward();
		}
		request.setAttribute("infoGratuitySituationList", infoGratuitySituationList);

		return mapping.findForward("studentsGratuityList");
	}

	/**
	 * @param degree
	 *            that is a string like '<degree's type>em <degree's name>#
	 *            <execution degree's id internal>'
	 * @return Integer whith execution degree id internal
	 */
	private Integer findExecutionDegreeId(String degree)
	{
		String idInString = degree.substring(degree.indexOf("#") + 1, degree.length());
		System.out.println("idInString= " + idInString);
		Integer idInternal = null;
		try
		{
			idInternal = Integer.valueOf(idInString);
		}
		catch (NumberFormatException numberFormatException)
		{
			numberFormatException.printStackTrace();
			throw new NumberFormatException();
		}
		return idInternal;
	}
}
