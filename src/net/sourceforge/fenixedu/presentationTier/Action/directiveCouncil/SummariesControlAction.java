/*
 * Created on Jan 16, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.ExecutionCourseSummaryElement;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.SummariesControlElementDTO;
import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement.SummaryControlCategory;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class SummariesControlAction extends FenixDispatchAction {

    private BigDecimal EMPTY = BigDecimal.ZERO;

    public ActionForward prepareSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DepartmentSummaryElement departmentSummaryElement = new DepartmentSummaryElement(null, null);
	request.setAttribute("executionSemesters", departmentSummaryElement);
	return mapping.findForward("success");
    }

    public ActionForward listSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DepartmentSummaryElement departmentSummaryElement = (DepartmentSummaryElement) getRenderedObject();
	String executionSemesterID = null;
	if (departmentSummaryElement != null) {
	    executionSemesterID = departmentSummaryElement.getExecutionSemester().getExternalId();
	} else {
	    executionSemesterID = (String) getFromRequest(request, "executionSemesterID");
	    ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionSemesterID);
	    departmentSummaryElement = new DepartmentSummaryElement(null, executionSemester);
	}
	request.setAttribute("executionSemesters", departmentSummaryElement);
	setAllDepartmentsSummaryResume(request, executionSemesterID);

	return mapping.findForward("success");
    }

    public ActionForward listDepartmentSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DepartmentSummaryElement departmentSummaryElement = (DepartmentSummaryElement) getRenderedObject();
	SummaryControlCategory summaryControlCategory = departmentSummaryElement.getSummaryControlCategory();
	departmentSummaryElement = getDepartmentSummaryResume(departmentSummaryElement.getExecutionSemester(),
		departmentSummaryElement.getDepartment());
	departmentSummaryElement.setSummaryControlCategory(summaryControlCategory);

	request.setAttribute("departmentResume", departmentSummaryElement);
	List<DepartmentSummaryElement> departmentList = new ArrayList<DepartmentSummaryElement>();
	departmentList.add(departmentSummaryElement);
	request.setAttribute("departmentResumeList", departmentList);
	RenderUtils.invalidateViewState();

	return mapping.findForward("success");
    }

    public ActionForward departmentSummariesResume(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String departmentID = (String) request.getParameter("departmentID");
	String executionPeriodID = (String) request.getParameter("executionSemesterID");
	String categoryControl = (String) getFromRequest(request, "categoryControl");

	SummaryControlCategory summaryControlCategory = null;
	if (!StringUtils.isEmpty(categoryControl)) {
	    summaryControlCategory = SummaryControlCategory.valueOf(categoryControl);
	}

	final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodID);
	final Department department = AbstractDomainObject.fromExternalId(departmentID);
	DepartmentSummaryElement departmentSummaryResume = getDepartmentSummaryResume(executionSemester, department);
	departmentSummaryResume.setSummaryControlCategory(summaryControlCategory);
	request.setAttribute("departmentResume", departmentSummaryResume);

	List<DepartmentSummaryElement> departmentList = new ArrayList<DepartmentSummaryElement>();
	departmentList.add(departmentSummaryResume);
	request.setAttribute("departmentResumeList", departmentList);

	return mapping.findForward("success");
    }

    public ActionForward executionCourseSummariesControl(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	String departmentID = (String) getFromRequest(request, "departmentID");
	String categoryControl = (String) getFromRequest(request, "categoryControl");
	String executionCourseId = (String) getFromRequest(request, "executionCourseID");
	ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);

	List<SummariesControlElementDTO> executionCoursesResume = getExecutionCourseResume(executionCourse.getExecutionPeriod(),
		executionCourse.getProfessorships());

	request.setAttribute("departmentID", departmentID);
	request.setAttribute("categoryControl", categoryControl);
	request.setAttribute("executionCourse", executionCourse);
	request.setAttribute("executionCoursesResume", executionCoursesResume);
	return mapping.findForward("success");
    }

    public ActionForward teacherSummariesControl(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String departmentID = (String) getFromRequest(request, "departmentID");
	String categoryControl = (String) getFromRequest(request, "categoryControl");
	String executionSemesterId = (String) getFromRequest(request, "executionSemesterID");

	String personId = (String) getFromRequest(request, "personID");
	Person person = AbstractDomainObject.fromExternalId(personId);

	List<Pair<ExecutionSemester, List<SummariesControlElementDTO>>> last4SemestersSummaryControl = new ArrayList<Pair<ExecutionSemester, List<SummariesControlElementDTO>>>();
	ExecutionSemester executionSemesterToPresent = ExecutionSemester.readActualExecutionSemester();

	List<SummariesControlElementDTO> executionCoursesResume = getExecutionCourseResume(executionSemesterToPresent, person
		.getProfessorshipsByExecutionSemester(executionSemesterToPresent));
	last4SemestersSummaryControl.add(new Pair<ExecutionSemester, List<SummariesControlElementDTO>>(
		executionSemesterToPresent, executionCoursesResume));
	for (int iter = 0; iter < 3; iter++) {
	    executionSemesterToPresent = executionSemesterToPresent.getPreviousExecutionPeriod();
	    executionCoursesResume = getExecutionCourseResume(executionSemesterToPresent, person
		    .getProfessorshipsByExecutionSemester(executionSemesterToPresent));
	    last4SemestersSummaryControl.add(new Pair<ExecutionSemester, List<SummariesControlElementDTO>>(
		    executionSemesterToPresent, executionCoursesResume));
	}

	request.setAttribute("last4SemestersSummaryControl", last4SemestersSummaryControl);
	request.setAttribute("person", person);
	request.setAttribute("departmentID", departmentID);
	request.setAttribute("categoryControl", categoryControl);
	request.setAttribute("executionSemesterID", executionSemesterId);

	return mapping.findForward("success");
    }

    private List<SummariesControlElementDTO> getListing(HttpServletRequest request, String departmentID, String executionPeriodID)
	    throws FenixFilterException, FenixServiceException {

	final Department department = rootDomainObject.readDepartmentByOID(Integer.valueOf(departmentID));
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(Integer
		.valueOf(executionPeriodID));

	List<Teacher> allDepartmentTeachers = (department != null && executionSemester != null) ? department.getAllTeachers(
		executionSemester.getBeginDateYearMonthDay(), executionSemester.getEndDateYearMonthDay())
		: new ArrayList<Teacher>();

	List<SummariesControlElementDTO> allListElements = new ArrayList<SummariesControlElementDTO>();

	for (Teacher teacher : allDepartmentTeachers) {
	    
	    for (Professorship professorship : teacher.getProfessorships()) {

		BigDecimal lessonHours = EMPTY, shiftHours = EMPTY, courseDifference = EMPTY;
		BigDecimal shiftDifference = EMPTY, courseHours = EMPTY;

		if (professorship.belongsToExecutionPeriod(executionSemester)
			&& !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
		    for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
			DegreeTeachingService degreeTeachingService = professorship.getDegreeTeachingServiceByShift(shift);
			if (degreeTeachingService != null) {
			    // GET LESSON HOURS
			    lessonHours = readDeclaredLessonHours(degreeTeachingService.getPercentage(), shift, lessonHours);

			    // GET SHIFT SUMMARIES HOURS
			    shiftHours = readSummaryHours(professorship, shift, shiftHours);
			}
			// GET COURSE SUMMARY HOURS
			courseHours = readSummaryHours(professorship, shift, courseHours);
		    }

		    shiftHours = shiftHours.setScale(2, RoundingMode.HALF_UP);
		    lessonHours = lessonHours.setScale(2, RoundingMode.HALF_UP);
		    courseHours = courseHours.setScale(2, RoundingMode.HALF_UP);

		    shiftDifference = getDifference(lessonHours, shiftHours);
		    courseDifference = getDifference(lessonHours, courseHours);

		    Category category = teacher.getCategory();
		    String categoryName = (category != null) ? category.getCode() : "";
		    String siglas = getSiglas(professorship);

		    SummariesControlElementDTO listElementDTO = new SummariesControlElementDTO(teacher.getPerson().getName(),
			    professorship.getExecutionCourse().getNome(), teacher.getTeacherNumber(), categoryName, lessonHours,
			    shiftHours, courseHours, shiftDifference, courseDifference, siglas);

		    allListElements.add(listElementDTO);
		}
	    }
	}

	Collections.sort(allListElements, new BeanComparator("teacherNumber"));
	request.setAttribute("listElements", allListElements);

	return allListElements;
    }

    private List<SummariesControlElementDTO> getExecutionCourseResume(final ExecutionSemester executionSemester,
	    List<Professorship> professorships) {
	List<SummariesControlElementDTO> allListElements = new ArrayList<SummariesControlElementDTO>();
	for (Professorship professorship : professorships) {

	    BigDecimal lessonHours = EMPTY, shiftHours = EMPTY, courseDifference = EMPTY;
	    BigDecimal shiftDifference = EMPTY, courseHours = EMPTY;
	    if (professorship.belongsToExecutionPeriod(executionSemester)
		    && !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {

		for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {

		    DegreeTeachingService degreeTeachingService = professorship.getDegreeTeachingServiceByShift(shift);
		    if (degreeTeachingService != null) {
			// GET LESSON HOURS
			lessonHours = readDeclaredLessonHours(degreeTeachingService.getPercentage(), shift, lessonHours);

			// GET SHIFT SUMMARIES HOURS
			shiftHours = readSummaryHours(professorship, shift, shiftHours);
		    }
		    // GET COURSE SUMMARY HOURS
		    courseHours = readSummaryHours(professorship, shift, courseHours);
		}

		shiftHours = shiftHours.setScale(2, RoundingMode.HALF_UP);
		lessonHours = lessonHours.setScale(2, RoundingMode.HALF_UP);
		courseHours = courseHours.setScale(2, RoundingMode.HALF_UP);

		shiftDifference = getDifference(lessonHours, shiftHours);
		courseDifference = getDifference(lessonHours, courseHours);

		Category category = professorship.getTeacher().getCategory();
		String categoryName = (category != null) ? category.getCode() : "";
		String siglas = getSiglas(professorship);

		SummariesControlElementDTO listElementDTO = new SummariesControlElementDTO(professorship.getTeacher().getPerson()
			.getName(), professorship.getExecutionCourse().getNome(), professorship.getTeacher().getTeacherNumber(),
			categoryName, lessonHours, shiftHours, courseHours, shiftDifference, courseDifference, siglas);

		allListElements.add(listElementDTO);
	    }
	}
	return allListElements;
    }

    private void setAllDepartmentsSummaryResume(HttpServletRequest request, String executionPeriodOID)
	    throws FenixFilterException, FenixServiceException {

	final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodOID);

	List<DepartmentSummaryElement> allDepartmentsSummariesResume = new ArrayList<DepartmentSummaryElement>();
	for (Department department : rootDomainObject.getDepartments()) {
	    DepartmentSummaryElement departmentSummariesElement = getDepartmentSummaryResume(executionSemester, department);
	    allDepartmentsSummariesResume.add(departmentSummariesElement);
	}
	Collections.sort(allDepartmentsSummariesResume, new BeanComparator("department.realName"));
	request.setAttribute("summariesResumeMap", allDepartmentsSummariesResume);
    }

    private DepartmentSummaryElement getDepartmentSummaryResume(final ExecutionSemester executionSemester,
	    final Department department) {
	DepartmentSummaryElement departmentSummariesElement = new DepartmentSummaryElement(department, executionSemester);
	Set<ExecutionCourse> allDepartmentExecutionCourses = getDepartmentExecutionCourses(department, executionSemester);
	if (allDepartmentExecutionCourses != null) {
	    for (ExecutionCourse executionCourse : allDepartmentExecutionCourses) {
		int instanceLessonsTotal[] = { 0, 0 };
		for (Shift shift : executionCourse.getAssociatedShifts()) {
		    getInstanceLessonsTotalsByShift(shift, instanceLessonsTotal);
		}
		BigDecimal result = BigDecimal.valueOf(0);
		BigDecimal numberOfLessonInstances = BigDecimal.valueOf(instanceLessonsTotal[0]);
		BigDecimal numberOfLessonInstancesWithSummary = BigDecimal.valueOf(0);
		if (instanceLessonsTotal[0] == 0) {
		    continue;
		}
		if (instanceLessonsTotal[1] != 0) {
		    numberOfLessonInstancesWithSummary = BigDecimal.valueOf(instanceLessonsTotal[1]);
		    result = numberOfLessonInstancesWithSummary.divide(numberOfLessonInstances, 3, BigDecimal.ROUND_CEILING)
			    .multiply(BigDecimal.valueOf(100));
		}
		SummaryControlCategory resumeClassification = getResumeClassification(result);
		Map<SummaryControlCategory, List<ExecutionCourseSummaryElement>> departmentResumeMap = departmentSummariesElement
			.getExecutionCoursesResume();
		List<ExecutionCourseSummaryElement> executionCoursesSummary = null;
		if (departmentResumeMap == null) {
		    departmentResumeMap = new HashMap<SummaryControlCategory, List<ExecutionCourseSummaryElement>>();
		    executionCoursesSummary = new ArrayList<ExecutionCourseSummaryElement>();
		    ExecutionCourseSummaryElement executionCourseSummaryElement = new ExecutionCourseSummaryElement(
			    executionCourse, numberOfLessonInstances, numberOfLessonInstancesWithSummary, result);
		    executionCoursesSummary.add(executionCourseSummaryElement);
		    departmentResumeMap.put(resumeClassification, executionCoursesSummary);
		    departmentSummariesElement.setExecutionCoursesResume(departmentResumeMap);
		} else {
		    executionCoursesSummary = departmentResumeMap.get(resumeClassification);
		    if (executionCoursesSummary == null) {
			executionCoursesSummary = new ArrayList<ExecutionCourseSummaryElement>();
			ExecutionCourseSummaryElement executionCourseSummaryElement = new ExecutionCourseSummaryElement(
				executionCourse, numberOfLessonInstances, numberOfLessonInstancesWithSummary, result);
			executionCoursesSummary.add(executionCourseSummaryElement);
			departmentResumeMap.put(resumeClassification, executionCoursesSummary);
		    } else {
			ExecutionCourseSummaryElement executionCourseSummaryElement = new ExecutionCourseSummaryElement(
				executionCourse, numberOfLessonInstances, numberOfLessonInstancesWithSummary, result);
			executionCoursesSummary.add(executionCourseSummaryElement);
		    }

		}
	    }
	}
	return departmentSummariesElement;
    }

    private Set<ExecutionCourse> getDepartmentExecutionCourses(Department department, ExecutionSemester executionSemester) {
	Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
	List<Teacher> allDepartmentTeachers = department.getAllTeachers(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());

	for (Teacher teacher : allDepartmentTeachers) {
	    for (Professorship professorship : teacher.getProfessorships()) {
		if (professorship.belongsToExecutionPeriod(executionSemester)
			&& !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
		    executionCourses.add(professorship.getExecutionCourse());
		}
	    }
	}
	return executionCourses;
    }

    private SummaryControlCategory getResumeClassification(BigDecimal result) {
	if (result.compareTo(BigDecimal.valueOf(20)) < 0) {
	    return SummaryControlCategory.BETWEEN_0_20;
	}
	if (result.compareTo(BigDecimal.valueOf(40)) < 0) {
	    return SummaryControlCategory.BETWEEN_20_40;
	}
	if (result.compareTo(BigDecimal.valueOf(60)) < 0) {
	    return SummaryControlCategory.BETWEEN_40_60;
	}
	if (result.compareTo(BigDecimal.valueOf(80)) < 0) {
	    return SummaryControlCategory.BETWEEN_60_80;
	}
	return SummaryControlCategory.BETWEEN_80_100;
    }

    private BigDecimal readDeclaredLessonHours(Double percentage, Shift shift, BigDecimal lessonHours) {
	BigDecimal shiftLessonHoursSum = EMPTY;
	for (Lesson lesson : shift.getAssociatedLessons()) {
	    shiftLessonHoursSum = shiftLessonHoursSum.add(lesson.getUnitHours().multiply(
		    BigDecimal.valueOf(lesson.getAllLessonDates().size())));
	}
	return lessonHours.add(BigDecimal.valueOf((percentage / 100)).multiply(shiftLessonHoursSum));
    }

    /**
     * Receives a shift and an array of int, with two positions filled in at
     * index 0 is the total number of lessonsInstance at index 1 is the total
     * number of lessonsInstance with a summary For the given shift this values
     * are checked and added in the correspondent position of the array
     * 
     * @param shift
     * @param instanceLessonsTotals
     */
    private void getInstanceLessonsTotalsByShift(Shift shift, int[] instanceLessonsTotals) {
	LocalDate today = new LocalDate();
	LocalDate oneWeekBeforeToday = today.minusDays(8);
	int numberOfInstanceLessons = 0;
	int numberOfInstanceLessonsWithSummary = 0;
	for (Lesson lesson : shift.getAssociatedLessons()) {
	    List<LessonInstance> allLessonInstanceDatesUntil = lesson.getAllLessonInstancesUntil(oneWeekBeforeToday);
	    numberOfInstanceLessons += allLessonInstanceDatesUntil.size();
	    for (LessonInstance lessonInstance : allLessonInstanceDatesUntil) {
		if (lessonInstance.getSummary() != null) {
		    numberOfInstanceLessonsWithSummary++;
		}
	    }
	}
	instanceLessonsTotals[0] = instanceLessonsTotals[0] + numberOfInstanceLessons;
	instanceLessonsTotals[1] = instanceLessonsTotals[1] + numberOfInstanceLessonsWithSummary;
    }

    private BigDecimal readSummaryHours(Professorship professorship, Shift shift, BigDecimal summaryHours) {
	for (Summary summary : shift.getAssociatedSummaries()) {
	    if (summary.getProfessorship() != null && summary.getProfessorship().equals(professorship)) {
		BigDecimal lessonHours = EMPTY;
		if (summary.getLesson() != null) {
		    lessonHours = summary.getLesson().getUnitHours();
		} else if (!shift.getAssociatedLessons().isEmpty()) {
		    lessonHours = shift.getAssociatedLessons().get(0).getUnitHours();
		}
		summaryHours = summaryHours.add(lessonHours);
	    }
	}
	return summaryHours;
    }

    private BigDecimal getDifference(BigDecimal lessonHours, BigDecimal summaryHours) {
	Double difference;
	difference = (1 - ((lessonHours.doubleValue() - summaryHours.doubleValue()) / lessonHours.doubleValue())) * 100;
	if (difference.isNaN() || difference.isInfinite()) {
	    difference = 0.0;
	}
	return BigDecimal.valueOf(difference).setScale(2, RoundingMode.HALF_UP);
    }

    private String getSiglas(Professorship professorship) {
	ExecutionCourse executionCourse = professorship.getExecutionCourse();
	int numberOfCurricularCourse = executionCourse.getAssociatedCurricularCourses().size();

	List<String> siglas = new ArrayList<String>();
	StringBuilder buffer = new StringBuilder();

	for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
	    String sigla = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
	    if (!siglas.contains(sigla)) {
		if (numberOfCurricularCourse < executionCourse.getAssociatedCurricularCourses().size()) {
		    buffer.append(",");
		}
		buffer.append(sigla);
		siglas.add(sigla);
	    }
	    numberOfCurricularCourse--;
	}
	return buffer.toString();
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	String departmentID = (String) dynaActionForm.get("department");
	String executionPeriodID = (String) dynaActionForm.get("executionPeriod");

	List<SummariesControlElementDTO> list = getListing(request, departmentID, executionPeriodID);
	try {
	    String filename = "ControloSumarios:" + getFileName(Calendar.getInstance().getTime());
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

	    ServletOutputStream writer = response.getOutputStream();
	    exportToXls(list, writer);

	    writer.flush();
	    response.flushBuffer();

	} catch (IOException e) {
	    throw new FenixServiceException();
	}
	return null;
    }

    public ActionForward exportToCSV(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
	String departmentID = (String) dynaActionForm.get("department");
	String executionPeriodID = (String) dynaActionForm.get("executionPeriod");

	List<SummariesControlElementDTO> list = getListing(request, departmentID, executionPeriodID);

	try {
	    String filename = "ControloSumarios:" + getFileName(Calendar.getInstance().getTime());
	    response.setContentType("text/plain");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename + ".csv");

	    ServletOutputStream writer = response.getOutputStream();
	    exportToCSV(list, writer);

	    writer.flush();
	    response.flushBuffer();

	} catch (IOException e) {
	    throw new FenixServiceException();
	}
	return null;
    }

    private void exportToXls(final List<SummariesControlElementDTO> allListElements, OutputStream outputStream)
	    throws IOException {
	final List<Object> headers = getHeaders();
	final Spreadsheet spreadsheet = new Spreadsheet("Controlo de Sumários", headers);
	fillSpreadSheet(allListElements, spreadsheet);
	spreadsheet.exportToXLSSheet(outputStream);
    }

    private void exportToCSV(final List<SummariesControlElementDTO> allListElements, OutputStream outputStream)
	    throws IOException {
	final Spreadsheet spreadsheet = new Spreadsheet("Controlo de Sumários");
	fillSpreadSheet(allListElements, spreadsheet);
	spreadsheet.exportToCSV(outputStream, ";");
    }

    private void fillSpreadSheet(final List<SummariesControlElementDTO> allListElements, final Spreadsheet spreadsheet) {
	for (final SummariesControlElementDTO summariesControlElementDTO : allListElements) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(summariesControlElementDTO.getTeacherName());
	    row.setCell(summariesControlElementDTO.getTeacherNumber().toString());
	    row.setCell(summariesControlElementDTO.getCategoryName());
	    row.setCell(summariesControlElementDTO.getExecutionCourseName());
	    row.setCell(summariesControlElementDTO.getSiglas());
	    row.setCell(summariesControlElementDTO.getLessonHours().toString());
	    row.setCell(summariesControlElementDTO.getSummaryHours().toString());
	    row.setCell(summariesControlElementDTO.getShiftDifference() == null ? "" : summariesControlElementDTO
		    .getShiftDifference().toString());
	    row.setCell(summariesControlElementDTO.getCourseSummaryHours().toString());
	    row.setCell(summariesControlElementDTO.getCourseDifference() == null ? "" : summariesControlElementDTO
		    .getCourseDifference().toString());
	}
    }

    private List<Object> getHeaders() {
	final List<Object> headers = new ArrayList<Object>();
	headers.add("Nome");
	headers.add("Número");
	headers.add("Categoria");
	headers.add("Disciplina");
	headers.add("Licenciatura(s)");
	headers.add("Horas Declaradas");
	headers.add("Sumários nos Turnos");
	headers.add("Percentagem nos Turnos");
	headers.add("Sumários na Disciplina");
	headers.add("Percentagem na Disciplina");
	return headers;
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(List<InfoExecutionPeriod> allExecutionPeriods) {
	List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
	for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
	    LabelValueBean labelValueBean = new LabelValueBean();
	    labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
		    + infoExecutionPeriod.getSemester() + "º Semestre");
	    labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
	    executionPeriods.add(labelValueBean);
	}
	Collections.sort(executionPeriods, new BeanComparator("label"));
	return executionPeriods;
    }

    private List<LabelValueBean> getAllDepartments(Collection<Department> allDepartments) {
	List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
	for (Department department : allDepartments) {
	    LabelValueBean labelValueBean = new LabelValueBean();
	    labelValueBean.setValue(department.getIdInternal().toString());
	    labelValueBean.setLabel(department.getRealName());
	    departments.add(labelValueBean);
	}
	Collections.sort(departments, new BeanComparator("label"));
	return departments;
    }

    protected void readAndSaveAllDepartments(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	Collection<Department> allDepartments = rootDomainObject.getDepartments();
	List<LabelValueBean> departments = getAllDepartments(allDepartments);
	request.setAttribute("allDepartments", allDepartments);
	request.setAttribute("departments", departments);
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException, FenixServiceException {

	List<InfoExecutionPeriod> allExecutionPeriods = new ArrayList<InfoExecutionPeriod>();

	allExecutionPeriods = ReadNotClosedExecutionPeriods.run();

	List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(allExecutionPeriods);
	request.setAttribute("executionPeriods", executionPeriods);
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	int month = calendar.get(Calendar.MONTH) + 1;
	int year = calendar.get(Calendar.YEAR);
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	int minutes = calendar.get(Calendar.MINUTE);
	return (day + "-" + month + "-" + year + "_" + hour + ":" + minutes);
    }
}