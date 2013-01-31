/*
 * Created on 2003/07/28
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.SelectRooms;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadClassByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadLessonByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadShiftByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ContextUtils {

	@Deprecated
	public static final void setExecutionPeriodContext(HttpServletRequest request) throws FenixServiceException {
		String executionPeriodOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID);
		if (executionPeriodOIDString == null) {
			executionPeriodOIDString = request.getParameter(PresentationConstants.EXECUTION_PERIOD_OID);
		}

		Integer executionPeriodOID = null;
		if (executionPeriodOIDString != null && !executionPeriodOIDString.equals("") && !executionPeriodOIDString.equals("null")) {
			executionPeriodOID = new Integer(executionPeriodOIDString);
		}

		InfoExecutionPeriod infoExecutionPeriod = null;
		if (executionPeriodOID != null) {
			infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodOID);
		} else {
			infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
		}
		if (infoExecutionPeriod != null) {
			// Place it in request
			request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
			request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
			if (infoExecutionPeriod.getInfoExecutionYear() != null) {
				request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
			}
		}
	}

	/**
	 * @param request
	 */
	public static void setExecutionDegreeContext(HttpServletRequest request) throws FenixServiceException {

		String executionDegreeOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID);

		if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
			executionDegreeOIDString = request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID);

			// No degree was chosen
			if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
				request.setAttribute(PresentationConstants.EXECUTION_DEGREE, null);
			}
		}

		Integer executionDegreeOID = null;
		if (executionDegreeOIDString != null) {
			try {
				executionDegreeOID = new Integer(executionDegreeOIDString);
			} catch (NumberFormatException ex) {
				return;
			}
		}

		InfoExecutionDegree infoExecutionDegree = null;

		if (executionDegreeOID != null) {
			infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeOID);

			if (infoExecutionDegree != null) {
				// Place it in request
				request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
				request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal().toString());
			}
		}
	}

	/**
	 * @param request
	 */
	public static void setCurricularYearContext(HttpServletRequest request) {
		String curricularYearOIDString = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID);
		if (curricularYearOIDString == null) {
			curricularYearOIDString = request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID);
		}

		Integer curricularYearOID = null;
		if (curricularYearOIDString != null && !curricularYearOIDString.equals("null")) {
			curricularYearOID = new Integer(curricularYearOIDString);
		}

		InfoCurricularYear infoCurricularYear = null;

		if (curricularYearOID != null) {
			// Read from database
			try {

				infoCurricularYear = ReadCurricularYearByOID.run(curricularYearOID);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			if (infoCurricularYear != null) {
				// Place it in request
				request.setAttribute(PresentationConstants.CURRICULAR_YEAR, infoCurricularYear);
				request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, infoCurricularYear.getIdInternal().toString());
			}

		}
	}

	/**
	 * @param request
	 */
	public static void setCurricularYearsContext(HttpServletRequest request) {

		String curricularYears_1 = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_1);
		String curricularYears_2 = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_2);
		String curricularYears_3 = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_3);
		String curricularYears_4 = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_4);
		String curricularYears_5 = (String) request.getAttribute(PresentationConstants.CURRICULAR_YEARS_5);
		if (curricularYears_1 == null) {
			curricularYears_1 = request.getParameter(PresentationConstants.CURRICULAR_YEARS_1);
		}
		if (curricularYears_2 == null) {
			curricularYears_2 = request.getParameter(PresentationConstants.CURRICULAR_YEARS_2);
		}
		if (curricularYears_3 == null) {
			curricularYears_3 = request.getParameter(PresentationConstants.CURRICULAR_YEARS_3);
		}
		if (curricularYears_4 == null) {
			curricularYears_4 = request.getParameter(PresentationConstants.CURRICULAR_YEARS_4);
		}
		if (curricularYears_5 == null) {
			curricularYears_5 = request.getParameter(PresentationConstants.CURRICULAR_YEARS_5);
		}

		List curricularYears = new ArrayList();

		if (curricularYears_1 != null) {
			try {
				curricularYears.add(new Integer(curricularYears_1));
			} catch (NumberFormatException ex) {
			}
		}
		if (curricularYears_2 != null) {
			try {
				curricularYears.add(new Integer(curricularYears_2));
			} catch (NumberFormatException ex) {
			}
		}
		if (curricularYears_3 != null) {
			try {
				curricularYears.add(new Integer(curricularYears_3));
			} catch (NumberFormatException ex) {
			}
		}
		if (curricularYears_4 != null) {
			try {
				curricularYears.add(new Integer(curricularYears_4));
			} catch (NumberFormatException ex) {
			}
		}
		if (curricularYears_5 != null) {
			try {
				curricularYears.add(new Integer(curricularYears_5));
			} catch (NumberFormatException ex) {
			}
		}

		request.setAttribute(PresentationConstants.CURRICULAR_YEARS_LIST, curricularYears);
	}

	/**
	 * @param request
	 */
	public static void setExecutionCourseContext(HttpServletRequest request) {
		String executionCourseOIDString = (String) request.getAttribute(PresentationConstants.EXECUTION_COURSE_OID);
		if (executionCourseOIDString == null) {
			executionCourseOIDString = request.getParameter(PresentationConstants.EXECUTION_COURSE_OID);
		}

		Integer executionCourseOID = null;
		if (executionCourseOIDString != null && !executionCourseOIDString.equals("") && !executionCourseOIDString.equals("null")) {
			executionCourseOID = new Integer(executionCourseOIDString);
		}

		InfoExecutionCourse infoExecutionCourse = null;

		if (executionCourseOID != null) {
			infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourseOID);

			if (infoExecutionCourse != null) {
				// Place it in request
				request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
			}
		}
	}

	/**
	 * @param request
	 */
	public static void setShiftContext(HttpServletRequest request) {
		String shiftOIDString = (String) request.getAttribute(PresentationConstants.SHIFT_OID);
		if (shiftOIDString == null) {
			shiftOIDString = request.getParameter(PresentationConstants.SHIFT_OID);
		}

		Integer shiftOID = null;
		if (shiftOIDString != null) {
			shiftOID = new Integer(shiftOIDString);
		}

		InfoShift infoShift = null;

		if (shiftOID != null) {
			infoShift = ReadShiftByOID.run(shiftOID);

			if (infoShift != null) {
				// Place it in request
				request.setAttribute(PresentationConstants.SHIFT, infoShift);
			}
		}
	}

	/**
	 * @param request
	 */
	public static void setClassContext(HttpServletRequest request) {
		String classOIDString = (String) request.getAttribute(PresentationConstants.CLASS_VIEW_OID);
		if (classOIDString == null) {
			classOIDString = request.getParameter(PresentationConstants.CLASS_VIEW_OID);
		}

		Integer classOID = null;
		if (classOIDString != null) {
			classOID = new Integer(classOIDString);
		}

		InfoClass infoClass = null;

		if (classOID != null) {
			// Read from database
			try {

				infoClass = ReadClassByOID.run(classOID);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(PresentationConstants.CLASS_VIEW, infoClass);
		}
	}

	/**
	 * @param request
	 */
	public static void setLessonContext(HttpServletRequest request) {
		String lessonOIDString = (String) request.getAttribute(PresentationConstants.LESSON_OID);
		if (lessonOIDString == null) {
			lessonOIDString = request.getParameter(PresentationConstants.LESSON_OID);
		}

		Integer lessonOID = null;
		if (lessonOIDString != null) {
			lessonOID = new Integer(lessonOIDString);
		}

		InfoLesson infoLesson = null;

		if (lessonOID != null) {
			infoLesson = ReadLessonByOID.run(lessonOID);

			// Place it in request
			request.setAttribute(PresentationConstants.LESSON, infoLesson);
		}
	}

	public static void setSelectedRoomsContext(HttpServletRequest request) throws FenixActionException {

		List selectedRooms = null;
		selectedRooms =
				(List) SelectRooms.run(new InfoRoomEditor(readRequestValue(request, "selectRoomCriteria_Name"), readRequestValue(
						request, "selectRoomCriteria_Building"), readIntegerRequestValue(request, "selectRoomCriteria_Floor"),
						readTypeRoomRequestValue(request, "selectRoomCriteria_Type"), readIntegerRequestValue(request,
								"selectRoomCriteria_CapacityNormal"), readIntegerRequestValue(request,
								"selectRoomCriteria_CapacityExame")));
		if (selectedRooms != null && !selectedRooms.isEmpty()) {
			Collections.sort(selectedRooms);
		}
		request.setAttribute(PresentationConstants.SELECTED_ROOMS, selectedRooms);

		setRoomSearchCriteriaContext(request);

	}

	/**
	 * @param request
	 */
	private static void setRoomSearchCriteriaContext(HttpServletRequest request) {

		request.setAttribute("selectRoomCriteria_Name", readRequestValue(request, "selectRoomCriteria_Name"));
		request.setAttribute("selectRoomCriteria_Building", readRequestValue(request, "selectRoomCriteria_Building"));
		request.setAttribute("selectRoomCriteria_Floor", readRequestValue(request, "selectRoomCriteria_Floor"));
		request.setAttribute("selectRoomCriteria_Type", readRequestValue(request, "selectRoomCriteria_Type"));
		request.setAttribute("selectRoomCriteria_CapacityNormal", readRequestValue(request, "selectRoomCriteria_CapacityNormal"));
		request.setAttribute("selectRoomCriteria_CapacityExame", readRequestValue(request, "selectRoomCriteria_CapacityExame"));

	}

	/**
	 * @param request
	 */
	public static void setSelectedRoomIndexContext(HttpServletRequest request) {
		Integer selectedRoomIndex = (Integer) request.getAttribute(PresentationConstants.SELECTED_ROOM_INDEX);

		// if (selectedRoomIndexString == null)
		// {
		String selectedRoomIndexString = request.getParameter(PresentationConstants.SELECTED_ROOM_INDEX);
		// }

		// Integer selectedRoomIndex = null;
		if (selectedRoomIndex != null) {
			// Place it in request
			request.setAttribute(PresentationConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);
		} else if (selectedRoomIndexString != null) {
			selectedRoomIndex = new Integer(selectedRoomIndexString);
			request.setAttribute(PresentationConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);
		}
	}

	@Deprecated
	public static void prepareChangeExecutionDegreeAndCurricularYear(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		/* Obtain a list of curricular years */
		List labelListOfCurricularYears = getLabelListOfCurricularYears();
		request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

		/* Obtain a list of degrees for the specified execution year */
		final ExecutionYear executionYear = infoExecutionPeriod.getExecutionPeriod().getExecutionYear();
		final Set<ExecutionDegree> executionDegrees = executionYear.getExecutionDegreesSet();

		final List<LabelValueBean> labelListOfExecutionDegrees = new ArrayList<LabelValueBean>();
		final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
		for (final ExecutionDegree executionDegree : executionDegrees) {
			infoExecutionDegrees.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));

			final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
			final Degree degree = degreeCurricularPlan.getDegree();
			final String degreeTypeString = bundle.getString(degree.getDegreeType().toString());
			final StringBuilder name = new StringBuilder();
			name.append(degreeTypeString);
			name.append(" ").append(applicationResources.getString("label.in")).append(" ");
			name.append(degree.getNameFor(executionDegree.getExecutionYear()).getContent());
			if (duplicateDegreeInList(degree, executionYear)) {
				name.append(" - ");
				name.append(degreeCurricularPlan.getName());
			}
			final LabelValueBean labelValueBean = new LabelValueBean(name.toString(), executionDegree.getIdInternal().toString());
			labelListOfExecutionDegrees.add(labelValueBean);
		}
		Collections.sort(labelListOfExecutionDegrees);
		request.setAttribute("licenciaturas", labelListOfExecutionDegrees);
		Collections.sort(infoExecutionDegrees, InfoExecutionDegree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME);
		request.setAttribute("executionDegrees", infoExecutionDegrees);

		final List<ExecutionSemester> executionSemesters = ExecutionSemester.readNotClosedExecutionPeriods();
		Collections.sort(executionSemesters);
		final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
		final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
		for (final ExecutionSemester executionSemester : executionSemesters) {
			infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
			final String name =
					StringAppender.append(executionSemester.getName(), " - ", executionSemester.getExecutionYear().getYear());
			final LabelValueBean labelValueBean = new LabelValueBean(name, executionSemester.getIdInternal().toString());
			executionPeriodLabelValueBeans.add(labelValueBean);
		}
		request.setAttribute(PresentationConstants.LIST_INFOEXECUTIONPERIOD, infoExecutionPeriods);
		request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodLabelValueBeans);
	}

	private static boolean duplicateDegreeInList(final Degree degree, final ExecutionYear executionYear) {
		boolean foundOne = false;
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
			for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
				if (executionYear == executionDegree.getExecutionYear()) {
					if (foundOne) {
						return true;
					} else {
						foundOne = true;
					}
				}
			}
		}
		return false;
	}

	// --------------------------------------------------------------------------
	// -----
	// Read from request utils
	// --------------------------------------------------------------------------
	// -----
	private static String readRequestValue(HttpServletRequest request, String name) {
		String obj = null;
		if (((String) request.getAttribute(name)) != null && !((String) request.getAttribute(name)).equals("")) {
			obj = (String) request.getAttribute(name);
		} else if (request.getParameter(name) != null && !request.getParameter(name).equals("")
				&& !request.getParameter(name).equals("null")) {
			obj = request.getParameter(name);
		}

		return obj;
	}

	private static Integer readIntegerRequestValue(HttpServletRequest request, String name) {
		String obj = readRequestValue(request, name);
		if (obj != null) {
			return new Integer(obj);
		}

		return null;
	}

	private static RoomClassification readTypeRoomRequestValue(HttpServletRequest request, String name) {
		Integer obj = readIntegerRequestValue(request, name);
		if (obj != null) {
			return RootDomainObject.getInstance().readRoomClassificationByOID(obj);
		}

		return null;
	}

	public static List getLabelListOfCurricularYears() {
		List labelListOfCurricularYears = new ArrayList();
		labelListOfCurricularYears.add(new LabelValueBean("escolher", ""));
		labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
		labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
		labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
		labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
		labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
		return labelListOfCurricularYears;
	}

	public static List getLabelListOfOptionalCurricularYears() {
		List labelListOfCurricularYears = new ArrayList();
		labelListOfCurricularYears.add(new LabelValueBean("todos", ""));
		labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
		labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
		labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
		labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
		labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
		return labelListOfCurricularYears;
	}

	public static List getLabelListOfExecutionDegrees(List executionDegreeList) {
		List labelListOfExecutionDegrees =
				(List) CollectionUtils.collect(executionDegreeList, new EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL());
		labelListOfExecutionDegrees.add(0, new LabelValueBean("escolher", ""));
		return labelListOfExecutionDegrees;
	}

	private static class EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL implements Transformer {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.commons.collections.Transformer#transform(java.lang.Object
		 * )
		 */
		@Override
		public Object transform(Object arg0) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;

			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " de " + name;

			return new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString());
		}
	}

	/***************************************************************************
	 * Novos metodos para os exames
	 */

	public static List createCurricularYearList() {
		List anosCurriculares = new ArrayList();

		anosCurriculares.add(new LabelValueBean("1 º", "1"));
		anosCurriculares.add(new LabelValueBean("2 º", "2"));
		anosCurriculares.add(new LabelValueBean("3 º", "3"));
		anosCurriculares.add(new LabelValueBean("4 º", "4"));
		anosCurriculares.add(new LabelValueBean("5 º", "5"));

		return anosCurriculares;
	}

	public static List createExecutionDegreeList(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
		IUserView userView = UserView.getUser();

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		/* Cria o form bean com as licenciaturas em execucao. */

		List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

		List licenciaturas = new ArrayList();

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		Iterator iterator = executionDegreeList.iterator();

		// sint index = 0;
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

			name +=
					duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
							+ infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

			licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
		}

		return licenciaturas;
	}

	/**
	 * Method existencesOfInfoDegree.
	 * 
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
	private static boolean duplicateInfoDegree(List<InfoExecutionDegree> executionDegreeList,
			InfoExecutionDegree infoExecutionDegree) {
		final InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		for (final InfoExecutionDegree otherInfoExecutionDegree : executionDegreeList) {
			final InfoDegreeCurricularPlan infoDegreeCurricularPlan = otherInfoExecutionDegree.getInfoDegreeCurricularPlan();
			final InfoDegree otherInfoDegree = infoDegreeCurricularPlan.getInfoDegree();
			if (infoDegree.equals(otherInfoDegree) && !infoExecutionDegree.equals(otherInfoExecutionDegree)) {
				return true;
			}
		}
		return false;
	}

	public static void setContextSelectionBean(HttpServletRequest request, Object renderedObject) {
		ContextSelectionBean context = null;
		if (renderedObject != null && renderedObject instanceof ContextSelectionBean) {
			RenderUtils.invalidateViewState();
			context = (ContextSelectionBean) renderedObject;
		} else if (request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN) != null) {
			context = (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);
		} else {
			AcademicInterval academicInterval = null;
			ExecutionDegree executionDegree = null;
			CurricularYear curricularYear = null;
			String courseName = null;
			if (request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL) != null) {
				String academicIntervalStr = (String) request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL);
				academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStr);
			} else if (request.getParameter(PresentationConstants.ACADEMIC_INTERVAL) != null) {
				String academicIntervalStr = request.getParameter(PresentationConstants.ACADEMIC_INTERVAL);
				if (academicIntervalStr != null && !academicIntervalStr.equals("null")) {
					academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(academicIntervalStr);
				}
			}
			if (academicInterval == null) {
				academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
			}
			if (request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID) != null) {
				executionDegree =
						RootDomainObject.getInstance().readExecutionDegreeByOID(
								Integer.parseInt((String) request.getAttribute(PresentationConstants.EXECUTION_DEGREE_OID)));
			} else if (request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID) != null) {
				executionDegree =
						RootDomainObject.getInstance().readExecutionDegreeByOID(
								Integer.parseInt(request.getParameter(PresentationConstants.EXECUTION_DEGREE_OID)));
			}
			if (request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID) != null
					&& !request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID).equals("null")) {
				curricularYear =
						RootDomainObject.getInstance().readCurricularYearByOID(
								Integer.parseInt((String) request.getAttribute(PresentationConstants.CURRICULAR_YEAR_OID)));
			} else if (request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID) != null
					&& !request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID).equals("null")) {
				curricularYear =
						RootDomainObject.getInstance().readCurricularYearByOID(
								Integer.parseInt(request.getParameter(PresentationConstants.CURRICULAR_YEAR_OID)));
			}
			if (request.getAttribute("execution_course_name") != null) {
				courseName = (String) request.getAttribute("execution_course_name");
			} else if (request.getParameter("execution_course_name") != null) {
				courseName = request.getParameter("execution_course_name");
			}

			context = new ContextSelectionBean(academicInterval, executionDegree, curricularYear);
			context.setCourseName(courseName);
		}
		request.setAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN, context);
		request.setAttribute(PresentationConstants.ACADEMIC_INTERVAL, context.getAcademicInterval()
				.getResumedRepresentationInStringFormat());

		if (context.getExecutionDegree() != null) {
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE, new InfoExecutionDegree(context.getExecutionDegree()));
		}

		request.setAttribute(PresentationConstants.CURRICULAR_YEAR, new InfoCurricularYear(context.getCurricularYear()));
	}

}