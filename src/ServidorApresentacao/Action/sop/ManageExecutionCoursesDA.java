package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseOccupancy;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import DataBeans.InfoShiftGroupStatistics;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.utils.ContextUtils;
import Util.TipoAula;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageExecutionCoursesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// TODO : find a way to refactor this code so it is shared with context selection.
		//        this implies changing the form value from index to executionPeriodOID etc.
		//        The same should be done with the selection of the execution course and 
		//        of the curricular year.

		IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

		Object argsReadExecutionPeriods[] = {};
		ArrayList executionPeriods = (ArrayList) gestor.executar(userView, "ReadNotClosedExecutionPeriods", argsReadExecutionPeriods);

		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
		chainComparator.addComparator(new BeanComparator("semester"));
		Collections.sort(executionPeriods, chainComparator);

		if (selectedExecutionPeriod != null) {
			DynaActionForm searchExecutionCourse = (DynaActionForm) form;
			searchExecutionCourse.set("executionPeriodOID", selectedExecutionPeriod.getIdInternal().toString());
		}

		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(infoExecutionPeriod.getName() + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getIdInternal().toString()));
		}

		request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

		////////////////////////////////////////////////////////////////////////////

		/* Obtain a list of curricular years */
		List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
		request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

		/* Obtain a list of degrees for the specified execution year */
		Object argsLerLicenciaturas[] = { selectedExecutionPeriod.getInfoExecutionYear()};
		List executionDegreeList = null;
		try {
			executionDegreeList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

			/* Sort the list of degrees */
			Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		} catch (FenixServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Generate a label list for the above list of degrees */
		List labelListOfExecutionDegrees = ContextUtils.getLabelListOfExecutionDegrees(executionDegreeList);
		request.setAttribute(SessionConstants.LIST_INFOEXECUTIONDEGREE, labelListOfExecutionDegrees);

		return mapping.findForward("ShowSearchForm");
	}

	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		DynaActionForm searchExecutionCourse = (DynaActionForm) form;
		// Mandatory Selection
		Integer executionPeriodOID = new Integer((String) searchExecutionCourse.get("executionPeriodOID"));
		// Optional Selection
		Integer executionDegreeOID = null;
		String executionDegreeOIDString = (String) searchExecutionCourse.get("executionDegreeOID");
		
		if ((executionDegreeOIDString != null) && (!executionDegreeOIDString.equals("null"))) {
			if (executionDegreeOIDString.length() > 0) {
				executionDegreeOID = new Integer(executionDegreeOIDString);
			}
		}
		
		// Optional Selection
		Integer curricularYearOID = null;
		if (searchExecutionCourse.get("curricularYearOID") != null && !searchExecutionCourse.get("curricularYearOID").equals("") && !searchExecutionCourse.get("curricularYearOID").equals("null")) {
			curricularYearOID = new Integer((String) searchExecutionCourse.get("curricularYearOID"));
		}
		// Optional Selection
		String executionCourseName = (String) searchExecutionCourse.get("executionCourseName");

		// Set Context
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
		ContextUtils.setExecutionPeriodContext(request);

		if (executionDegreeOID != null) {
			request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeOID.toString());
		}
		ContextUtils.setExecutionDegreeContext(request);
		if (curricularYearOID != null) {
			request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYearOID.toString());
			ContextUtils.setCurricularYearContext(request);
		}
		request.setAttribute("execution_course_name", executionCourseName);

		// Call some service that queries the list of execution course.
		List infoExecutionCourses = null;
		
		
		Object args[] =
			{
				request.getAttribute(SessionConstants.EXECUTION_PERIOD),
				request.getAttribute(SessionConstants.EXECUTION_DEGREE),
				request.getAttribute(SessionConstants.CURRICULAR_YEAR),
				executionCourseName };
		infoExecutionCourses = (List) gestor.executar(userView, "SearchExecutionCourses", args);

		// if query result is a list then go to a page where they are listed
		//		if (infoExecutionCourses == null
		//			|| infoExecutionCourses.isEmpty()
		//			|| infoExecutionCourses.size() > 1) {

		if (infoExecutionCourses != null) {

			sortList(request, infoExecutionCourses);

			request.setAttribute(SessionConstants.LIST_INFOEXECUTIONCOURSE, infoExecutionCourses);
			
			
		}
		return prepareSearch(mapping, form, request, response);
	// if query result is a sigle item then go directly to the execution course page
		//		} else {
		//			request.setAttribute(
		//				SessionConstants.EXECUTION_COURSE,
		//				infoExecutionCourses.get(0));
		//			return mapping.findForward("ManageExecutionCourse");
		//		}
		}

	/**
	 * @param request
	 * @param infoExecutionCourses
	 */
	private void sortList(HttpServletRequest request, List infoExecutionCourses) {
		String sortParameter = request.getParameter("sortBy"); 
		if ((sortParameter != null) && (sortParameter.length() != 0)) {
			if (sortParameter.equals("occupancy")) {
				Collections.sort(infoExecutionCourses, new ReverseComparator(new BeanComparator(sortParameter)));	
			} else {
				Collections.sort(infoExecutionCourses, new BeanComparator(sortParameter));
			}
		} else {
			Collections.sort(infoExecutionCourses, new ReverseComparator(new BeanComparator("occupancy"))); 
		}
	}

	public ActionForward changeExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm searchExecutionCourse = (DynaActionForm) form;
		Integer executionPeriodOID = new Integer((String) searchExecutionCourse.get("executionPeriodOID"));
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
		ContextUtils.setExecutionPeriodContext(request);
		return prepareSearch(mapping, form, request, response);
	}


	public ActionForward showOccupancyLevels(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();


		Object args[] = {new Integer(request.getParameter("executionCourseOID"))};

		InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = (InfoExecutionCourseOccupancy) gestor.executar(userView, "ReadShiftsByExecutionCourseID", args);

		arranjeShifts(infoExecutionCourseOccupancy);
		

//		Collections.sort(infoExecutionCourseOccupancy.getInfoShifts(), new ReverseComparator(new BeanComparator("percentage")));
				
		request.setAttribute("infoExecutionCourseOccupancy", infoExecutionCourseOccupancy);
		return mapping.findForward("showOccupancy");

	}

	/**
	 * @param infoExecutionCourseOccupancy
	 */
	private void arranjeShifts(InfoExecutionCourseOccupancy infoExecutionCourseOccupancy) {

		// Note : This must be synched with TipoAula.java
		
		List theoreticalShifts = new ArrayList();
		List theoPraticalShifts = new ArrayList();
		List praticalShifts = new ArrayList();
		List labShifts = new ArrayList();
		List reserveShifts = new ArrayList();
		List doubtsShifts = new ArrayList();
		
		infoExecutionCourseOccupancy.setShiftsInGroups(new ArrayList());

		Iterator iterator = infoExecutionCourseOccupancy.getInfoShifts().iterator();
		while(iterator.hasNext()) {
			InfoShift infoShift = (InfoShift) iterator.next();
			if (infoShift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
				theoreticalShifts.add(infoShift);
			} else if (infoShift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
				praticalShifts.add(infoShift);
			}else if (infoShift.getTipo().equals(new TipoAula(TipoAula.DUVIDAS))) {
				doubtsShifts.add(infoShift);
			}else if (infoShift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
				labShifts.add(infoShift);
			}else if (infoShift.getTipo().equals(new TipoAula(TipoAula.RESERVA))) {
				reserveShifts.add(infoShift);
			}else if (infoShift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
				theoPraticalShifts.add(infoShift);
			}
		}
		infoExecutionCourseOccupancy.setInfoShifts(null);
		InfoShiftGroupStatistics infoShiftGroupStatistics = new InfoShiftGroupStatistics();
		if (!theoreticalShifts.isEmpty()) {
			infoShiftGroupStatistics.setShiftsInGroup(theoreticalShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}
		
		
		
		if (!theoPraticalShifts.isEmpty()) {
			infoShiftGroupStatistics = new InfoShiftGroupStatistics();
			infoShiftGroupStatistics.setShiftsInGroup(theoPraticalShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}

		if (!labShifts.isEmpty()) {
			infoShiftGroupStatistics = new InfoShiftGroupStatistics();
			infoShiftGroupStatistics.setShiftsInGroup(labShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}

		if (!praticalShifts.isEmpty()) {
			infoShiftGroupStatistics = new InfoShiftGroupStatistics();
			infoShiftGroupStatistics.setShiftsInGroup(praticalShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}

		if (!reserveShifts.isEmpty()) {
			infoShiftGroupStatistics = new InfoShiftGroupStatistics();
			infoShiftGroupStatistics.setShiftsInGroup(reserveShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}
		

		if (!doubtsShifts.isEmpty()) {
			infoShiftGroupStatistics = new InfoShiftGroupStatistics();
			infoShiftGroupStatistics.setShiftsInGroup(doubtsShifts);
			infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
		}

	}


	public ActionForward showLoads(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();


		Object args[] = {new Integer(request.getParameter("executionCourseOID"))};

		InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) gestor.executar(userView, "ReadExecutionCourseByOID", args);

		List scopes = (List) gestor.executar(userView, "ReadCurricularCourseScopesByExecutionCourseID", args);

		request.setAttribute("infoExecutionCourse", infoExecutionCourse);
		request.setAttribute("curricularCourses", scopes);
		
		return mapping.findForward("showLoads");

	}



}