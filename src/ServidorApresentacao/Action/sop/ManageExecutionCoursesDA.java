package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao
	.Action
	.sop
	.base
	.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageExecutionCoursesDA
	extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward prepareSearch(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		// TODO : find a way to refactor this code so it is shared with context selection.
		//        this implies changing the form value from index to executionPeriodOID etc.
		//        The same should be done with the selection of the execution course and 
		//        of the curricular year.

		IUserView userView =
			(IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		InfoExecutionPeriod selectedExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods =
			(ArrayList) gestor.executar(
				userView,
				"ReadExecutionPeriods",
				argsReadExecutionPeriods);

		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(
			new BeanComparator("infoExecutionYear.year"));
		chainComparator.addComparator(new BeanComparator("semester"));
		Collections.sort(executionPeriods, chainComparator);

		if (selectedExecutionPeriod != null) {
			DynaActionForm searchExecutionCourse = (DynaActionForm) form;
			searchExecutionCourse.set(
				"executionPeriodOID",
				selectedExecutionPeriod.getIdInternal().toString());
		}

		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(
					infoExecutionPeriod.getName()
						+ " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
					infoExecutionPeriod.getIdInternal().toString()));
		}

		request.setAttribute(
			SessionConstants.LABELLIST_EXECUTIONPERIOD,
			executionPeriodsLabelValueList);

		////////////////////////////////////////////////////////////////////////////

		/* Obtain a list of curricular years */
		List labelListOfCurricularYears =
			ContextUtils.getLabelListOfCurricularYears();
		request.setAttribute(
			SessionConstants.LABELLIST_CURRICULAR_YEARS,
			labelListOfCurricularYears);

		/* Obtain a list of degrees for the specified execution year */
		Object argsLerLicenciaturas[] =
			{ selectedExecutionPeriod.getInfoExecutionYear()};
		List executionDegreeList = null;
		try {
			executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			/* Sort the list of degrees */
			Collections.sort(
				executionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());
		} catch (FenixServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Generate a label list for the above list of degrees */
		List labelListOfExecutionDegrees =
			ContextUtils.getLabelListOfExecutionDegrees(executionDegreeList);
		request.setAttribute(
			SessionConstants.LIST_INFOEXECUTIONDEGREE,
			labelListOfExecutionDegrees);

		return mapping.findForward("ShowSearchForm");
	}

	public ActionForward search(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView =
			(IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		DynaActionForm searchExecutionCourse = (DynaActionForm) form;
		// Mandatory Selection
		Integer executionPeriodOID =
			new Integer(
				(String) searchExecutionCourse.get("executionPeriodOID"));
		// Mandatory Selection
		Integer executionDegreeOID =
			new Integer(
				(String) searchExecutionCourse.get("executionDegreeOID"));
		// Optional Selection
		Integer curricularYearOID = null;
		if (searchExecutionCourse.get("curricularYearOID") != null
			&& !searchExecutionCourse.get("curricularYearOID").equals("")
			&& !searchExecutionCourse.get("curricularYearOID").equals("null")) {
			curricularYearOID =
				new Integer(
					(String) searchExecutionCourse.get("curricularYearOID"));
		}
		// Optional Selection
		String executionCourseName =
			(String) searchExecutionCourse.get("executionCourseName");

		// Set Context
		System.out.println("executionPeriodOID= " + executionPeriodOID);
		request.setAttribute(
			SessionConstants.EXECUTION_PERIOD_OID,
			executionPeriodOID.toString());
		ContextUtils.setExecutionPeriodContext(request);
		System.out.println("executionDegreeOID= " + executionDegreeOID);
		request.setAttribute(
			SessionConstants.EXECUTION_DEGREE_OID,
			executionDegreeOID.toString());
		ContextUtils.setExecutionDegreeContext(request);
		if (curricularYearOID != null) {
			System.out.println("curricularYearOID= " + curricularYearOID);
			request.setAttribute(
				SessionConstants.CURRICULAR_YEAR_OID,
				curricularYearOID.toString());
			ContextUtils.setCurricularYearContext(request);
		}

		// Call some service that queries the list of execution course.
		List infoExecutionCourses = null;
		Object args[] =
			{
				request.getAttribute(SessionConstants.EXECUTION_PERIOD),
				request.getAttribute(SessionConstants.EXECUTION_DEGREE),
				request.getAttribute(SessionConstants.CURRICULAR_YEAR),
				executionCourseName };
		infoExecutionCourses =
			(List) gestor.executar(userView, "SearchExecutionCourses", args);

		// if query result is a list then go to a page where they are listed
		if (infoExecutionCourses == null
			|| infoExecutionCourses.isEmpty()
			|| infoExecutionCourses.size() > 1) {
			if (infoExecutionCourses != null) {
				Collections.sort(
					infoExecutionCourses,
					new BeanComparator("sigla"));
				request.setAttribute(
					SessionConstants.LIST_INFOEXECUTIONCOURSE,
					infoExecutionCourses);
			}
			return prepareSearch(mapping, form, request, response);
			// if query result is a sigle item then go directly to the execution course page
		} else {
			request.setAttribute(
				SessionConstants.EXECUTION_COURSE,
				infoExecutionCourses.get(0));
			return mapping.findForward("ManageExecutionCourse");
		}

	}

	public ActionForward changeExecutionPeriod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView =
			(IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		DynaActionForm searchExecutionCourse = (DynaActionForm) form;
		Integer executionPeriodOID =
			new Integer(
				(String) searchExecutionCourse.get("executionPeriodOID"));

		request.setAttribute(
			SessionConstants.EXECUTION_PERIOD_OID,
			executionPeriodOID.toString());

		ContextUtils.setExecutionPeriodContext(request);

		return prepareSearch(mapping, form, request, response);
	}

}