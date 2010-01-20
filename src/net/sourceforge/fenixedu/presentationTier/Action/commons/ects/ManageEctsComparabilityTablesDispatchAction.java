package net.sourceforge.fenixedu.presentationTier.Action.commons.ects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCompetenceCourseConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCycleGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.IEctsConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.NullEctsConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsConversionTable.DuplicateEctsConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.excel.SimplifiedSpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(path = "/manageEctsComparabilityTables", module = "gep")
@Forwards( { @Forward(name = "index", path = "/gep/ects/comparabilityTableIndex.jsp") })
public class ManageEctsComparabilityTablesDispatchAction extends FenixDispatchAction {
    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EctsTableFilter filter = readFilter(request);
	request.setAttribute("filter", filter);
	return mapping.findForward("index");
    }

    public ActionForward filterPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EctsTableFilter filter = readFilter(request);
	filter.setLevel(null);
	request.setAttribute("filter", filter);
	return mapping.findForward("index");
    }

    public ActionForward viewStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EctsTableFilter filter = readFilter(request);
	processStatus(request, filter);
	request.setAttribute("filter", filter);
	return mapping.findForward("index");
    }

    public ActionForward exportTemplate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EctsTableFilter filter = readFilter(request);
	try {
	    try {
		SimplifiedSpreadsheetBuilder<?> builder = exportTemplate(request, filter);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=template.xls");
		builder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
		return null;
	    } finally {
		response.flushBuffer();
	    }
	} catch (IOException e) {
	    addActionMessage(request, "error.ects.comparabilityTables.ioException");
	    processStatus(request, filter);
	    request.setAttribute("filter", filter);
	    return mapping.findForward("index");
	}
    }

    public ActionForward importTables(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EctsTableFilter filter = readFilter(request);
	try {
	    importTables(filter.getExecutionInterval(), filter.getType(), filter.getLevel(), FileUtils.readFile(filter
		    .getInputStream()));
	} catch (IOException e) {
	    addActionMessage(request, "error.ects.table.unableToReadTablesFile");
	}
	processStatus(request, filter);
	request.setAttribute("filter", filter);
	return mapping.findForward("index");
    }

    private EctsTableFilter readFilter(HttpServletRequest request) {
	EctsTableFilter filter = (EctsTableFilter) getRenderedObject("filter");
	RenderUtils.invalidateViewState();
	if (filter == null) {
	    filter = new EctsTableFilter();
	    if (request.getParameter("interval") != null) {
		filter.setExecutionInterval(AcademicInterval.getAcademicIntervalFromResumedString(request
			.getParameter("interval")));
	    }
	    if (request.getParameter("type") != null) {
		filter.setType(EctsTableType.valueOf(request.getParameter("type")));
	    }
	    if (request.getParameter("level") != null) {
		filter.setLevel(EctsTableLevel.valueOf(request.getParameter("level")));
	    }
	}
	return filter;
    }

    private void processStatus(HttpServletRequest request, EctsTableFilter filter) {
	switch (filter.getType()) {
	case ENROLMENT:
	    request.setAttribute("status", processEnrolmentStatus(filter));
	    break;
	case GRADUATION:
	    request.setAttribute("status", processGraduationStatus(filter));
	    break;
	}
    }

    private SimplifiedSpreadsheetBuilder<?> exportTemplate(HttpServletRequest request, EctsTableFilter filter) {
	switch (filter.getType()) {
	case ENROLMENT:
	    return exportEnrolmentTemplate(filter);
	case GRADUATION:
	    return exportGraduationTemplate(filter);
	default:
	    throw new Error();
	}
    }

    private void importTables(AcademicInterval executionInterval, EctsTableType type, EctsTableLevel level, String file) {
	try {
	    switch (type) {
	    case ENROLMENT:
		importEnrolmentTables(executionInterval, level, file);
		break;
	    case GRADUATION:
		importGraduationTables(executionInterval, level, file);
		break;
	    }
	} catch (IOException e) {
	    throw new DomainException("error.ects.importfailed");
	}
    }

    private Set<IEctsConversionTable> processEnrolmentStatus(EctsTableFilter filter) {
	switch (filter.getLevel()) {
	case COMPETENCE_COURSE:
	    return processEnrolmentByCompetenceCourseStatus(filter);
	case DEGREE:
	    return processEnrolmentByDegreeStatus(filter);
	case CURRICULAR_YEAR:
	    return processEnrolmentByCurricularYearStatus(filter);
	default:
	    return Collections.emptySet();
	}
    }

    private SimplifiedSpreadsheetBuilder<?> exportEnrolmentTemplate(EctsTableFilter filter) {
	switch (filter.getLevel()) {
	case COMPETENCE_COURSE:
	    return exportEnrolmentByCompetenceCourseTemplate(filter);
	case DEGREE:
	    return exportEnrolmentByDegreeTemplate(filter);
	case CURRICULAR_YEAR:
	    return exportEnrolmentByCurricularYearTemplate(filter);
	default:
	    throw new Error();
	}
    }

    private void importEnrolmentTables(AcademicInterval executionInterval, EctsTableLevel level, String file) {
	switch (level) {
	case COMPETENCE_COURSE:
	    importEnrolmentByCompetenceCourseTables(executionInterval, file);
	    break;
	case DEGREE:
	    importEnrolmentByDegreeTables(executionInterval, file);
	    break;
	case CURRICULAR_YEAR:
	    importEnrolmentByCurricularYearTables(executionInterval, file);
	    break;
	}
    }

    private Set<IEctsConversionTable> processGraduationStatus(EctsTableFilter filter) {
	switch (filter.getLevel()) {
	case DEGREE:
	    return processGraduationByDegreeStatus(filter);
	case CYCLE:
	    return processGraduationByCycleStatus(filter);
	default:
	    return Collections.emptySet();
	}
    }

    private SimplifiedSpreadsheetBuilder<IEctsConversionTable> exportGraduationTemplate(EctsTableFilter filter) {
	switch (filter.getLevel()) {
	case DEGREE:
	    return exportGraduationByDegreeTemplate(filter);
	case CYCLE:
	    return exportGraduationByCycleTemplate(filter);
	default:
	    throw new Error();
	}
    }

    private void importGraduationTables(AcademicInterval executionInterval, EctsTableLevel level, String file) throws IOException {
	switch (level) {
	case DEGREE:
	    importGraduationByDegreeTables(executionInterval, file);
	    break;
	case CYCLE:
	    importGraduationByCycleTables(executionInterval, file);
	    break;
	}
    }

    private Set<IEctsConversionTable> processEnrolmentByCompetenceCourseStatus(EctsTableFilter filter) {
	ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (CompetenceCourse competenceCourse : rootDomainObject.getCompetenceCoursesSet()) {
	    if ((competenceCourse.getCurricularStage() == CurricularStage.PUBLISHED || competenceCourse.getCurricularStage() == CurricularStage.APPROVED)
		    && competenceCourse.hasActiveScopesInExecutionYear(year)
		    && competenceCourse.getActiveEnrollments(year).size() > 0) {
		if (competenceCourse.getEctsCourseConversionTable(filter.getExecutionInterval()) != null) {
		    tables.add(competenceCourse.getEctsCourseConversionTable(filter.getExecutionInterval()));
		} else {
		    tables.add(new NullEctsConversionTable(competenceCourse));
		}
	    }
	}
	return tables;
    }

    private SimplifiedSpreadsheetBuilder<CompetenceCourse> exportEnrolmentByCompetenceCourseTemplate(EctsTableFilter filter) {
	final ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
	List<CompetenceCourse> competences = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse competenceCourse : rootDomainObject.getCompetenceCoursesSet()) {
	    if ((competenceCourse.getCurricularStage() == CurricularStage.PUBLISHED || competenceCourse.getCurricularStage() == CurricularStage.APPROVED)
		    && competenceCourse.hasActiveScopesInExecutionYear(year)
		    && competenceCourse.getActiveEnrollments(year).size() > 0) {
		competences.add(competenceCourse);
	    }
	}
	ComparatorChain chain = new ComparatorChain();
	chain.addComparator(new BeanComparator("departmentUnit.name"));
	chain.addComparator(new BeanComparator("name"));
	Collections.sort(competences, chain);
	SimplifiedSpreadsheetBuilder<CompetenceCourse> builder = new SimplifiedSpreadsheetBuilder<CompetenceCourse>(competences) {
	    private final ResourceBundle bundle = ResourceBundle.getBundle("resources.GEPResources");

	    @Override
	    protected void makeLine(CompetenceCourse competence) {
		addColumn(bundle.getString("label.externalId"), competence.getExternalId());
		addColumn(bundle.getString("label.departmentUnit.name"), competence.getDepartmentUnit().getName());
		addColumn(bundle.getString("label.competenceCourse.name"), competence.getName());
		addColumn(bundle.getString("label.acronym"), competence.getAcronym());
		addColumn(bundle.getString("label.idInternal"), competence.getIdInternal());
		Set<String> ids = new HashSet<String>();
		for (CurricularCourse course : competence.getAssociatedCurricularCoursesSet()) {
		    List<ExecutionCourse> executions = course.getExecutionCoursesByExecutionYear(year);
		    for (ExecutionCourse executionCourse : executions) {
			if (!ids.contains(executionCourse.getIdInternal().toString())) {
			    ids.add(executionCourse.getIdInternal().toString());
			}
		    }
		}
		addColumn(bundle.getString("label.competenceCourse.executionCodes"), StringUtils.join(ids, ", "));
		addColumn("10", null);
		addColumn("11", null);
		addColumn("12", null);
		addColumn("13", null);
		addColumn("14", null);
		addColumn("15", null);
		addColumn("16", null);
		addColumn("17", null);
		addColumn("18", null);
		addColumn("19", null);
		addColumn("20", null);
	    }
	};
	return builder;
    }

    private void importEnrolmentByCompetenceCourseTables(AcademicInterval executionInterval, String file) {
	for (String line : file.split("\n")) {
	    String[] parts = line.split("\\s*;\\s*");
	    CompetenceCourse competence = DomainObject.fromExternalId(parts[0]);
	    if (!competence.getDepartmentUnit().getName().equals(parts[1])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[1]);
	    }
	    if (!competence.getName().equals(parts[2])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[2]);
	    }
	    try {
		EctsCompetenceCourseConversionTable.createConversionTable(competence, executionInterval, Arrays.copyOfRange(
			parts, 6, 17));
	    } catch (DuplicateEctsConversionTable e) {
	    }
	}
    }

    private Set<IEctsConversionTable> processEnrolmentByDegreeStatus(EctsTableFilter filter) {
	ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeCurricularPlansExecutionYears().contains(year)
		    && (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)
			    || degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE) || degree.getDegreeType().equals(
			    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE))) {
		for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
		    EctsDegreeByCurricularYearConversionTable table = degree.getEctsCourseConversionTable(filter
			    .getExecutionInterval(), CurricularYear.readByYear(i));
		    if (table != null) {
			tables.add(table);
		    } else {
			tables.add(new NullEctsConversionTable(degree, CurricularYear.readByYear(i)));
		    }
		}
	    }
	}
	return tables;
    }

    private SimplifiedSpreadsheetBuilder<IEctsConversionTable> exportEnrolmentByDegreeTemplate(EctsTableFilter filter) {
	ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeCurricularPlansExecutionYears().contains(year)
		    && (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)
			    || degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE) || degree.getDegreeType().equals(
			    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE))) {
		for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
		    EctsDegreeByCurricularYearConversionTable table = degree.getEctsCourseConversionTable(filter
			    .getExecutionInterval(), CurricularYear.readByYear(i));
		    if (table != null) {
			tables.add(table);
		    } else {
			tables.add(new NullEctsConversionTable(degree, CurricularYear.readByYear(i)));
		    }
		}
	    }
	}
	// Collections.sort(degrees,
	// Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	SimplifiedSpreadsheetBuilder<IEctsConversionTable> builder = new SimplifiedSpreadsheetBuilder<IEctsConversionTable>(
		tables) {
	    private final ResourceBundle bundle = ResourceBundle.getBundle("resources.GEPResources");

	    @Override
	    protected void makeLine(IEctsConversionTable table) {
		Degree degree = (Degree) table.getTargetEntity();
		addColumn(bundle.getString("label.externalId"), degree.getExternalId());
		addColumn(bundle.getString("label.degreeType"), degree.getDegreeType().getLocalizedName());
		addColumn(bundle.getString("label.name"), degree.getName());
		addColumn(bundle.getString("label.curricularYear"), table.getCurricularYear());
		addColumn("10", null);
		addColumn("11", null);
		addColumn("12", null);
		addColumn("13", null);
		addColumn("14", null);
		addColumn("15", null);
		addColumn("16", null);
		addColumn("17", null);
		addColumn("18", null);
		addColumn("19", null);
		addColumn("20", null);
	    }
	};
	return builder;
    }

    private void importEnrolmentByDegreeTables(AcademicInterval executionInterval, String file) {
	for (String line : file.split("\n")) {
	    String[] parts = line.split("\\s*;\\s*");
	    Degree degree = DomainObject.fromExternalId(parts[0]);
	    if (!degree.getDegreeType().getLocalizedName().equals(parts[1])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[1]);
	    }
	    if (!degree.getName().equals(parts[2])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[2]);
	    }
	    CurricularYear year = CurricularYear.readByYear(Integer.parseInt(parts[3]));
	    try {
		EctsDegreeByCurricularYearConversionTable.createConversionTable(degree, executionInterval, year, Arrays
			.copyOfRange(parts, 4, 15));
	    } catch (DuplicateEctsConversionTable e) {
	    }
	}
    }

    private Set<IEctsConversionTable> processEnrolmentByCurricularYearStatus(EctsTableFilter filter) {
	final Unit ist = UnitUtils.readInstitutionUnit();
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (CycleType cycle : CycleType.getSortedValues()) {
	    List<Integer> years = null;
	    switch (cycle) {
	    case FIRST_CYCLE:
		years = Arrays.asList(1, 2, 3);
		break;
	    case SECOND_CYCLE:
		years = Arrays.asList(1, 2, 4, 5);
		break;
	    case THIRD_CYCLE:
		years = Arrays.asList(1, 2);
		break;
	    }
	    for (Integer year : years) {
		EctsInstitutionByCurricularYearConversionTable table = ist.getEctsCourseConversionTable(filter
			.getExecutionInterval(), cycle, CurricularYear.readByYear(year));
		if (table != null) {
		    tables.add(table);
		} else {
		    tables.add(new NullEctsConversionTable(ist, cycle, CurricularYear.readByYear(year)));
		}
	    }
	}
	return tables;
    }

    private SimplifiedSpreadsheetBuilder<IEctsConversionTable> exportEnrolmentByCurricularYearTemplate(EctsTableFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }

    private void importEnrolmentByCurricularYearTables(AcademicInterval executionInterval, String file) {
	for (String line : file.split("\n")) {
	    String[] parts = line.split("\\s*;\\s*");
	    final Unit ist = UnitUtils.readInstitutionUnit();
	    CycleType cycle;
	    try {
		cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[0]) - 1];
	    } catch (NumberFormatException e) {
		cycle = null;
	    }
	    CurricularYear year = CurricularYear.readByYear(Integer.parseInt(parts[1]));
	    try {
		EctsInstitutionByCurricularYearConversionTable.createConversionTable(ist, executionInterval, cycle, year, Arrays
			.copyOfRange(parts, 2, 13));
	    } catch (DuplicateEctsConversionTable e) {
	    }
	}
    }

    private Set<IEctsConversionTable> processGraduationByDegreeStatus(EctsTableFilter filter) {
	ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeCurricularPlansExecutionYears().contains(year)) {
		for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
		    EctsDegreeGraduationGradeConversionTable table = degree.getEctsGraduationGradeConversionTable(filter
			    .getExecutionInterval());
		    if (table != null) {
			tables.add(table);
		    } else {
			tables.add(new NullEctsConversionTable(degree));
		    }
		}
	    }
	}
	return tables;
    }

    private SimplifiedSpreadsheetBuilder<IEctsConversionTable> exportGraduationByDegreeTemplate(EctsTableFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }

    private void importGraduationByDegreeTables(AcademicInterval executionInterval, String file) {
	for (String line : file.split("\n")) {
	    String[] parts = line.split("\\s*;\\s*");
	    Degree degree = DomainObject.fromExternalId(parts[0]);
	    if (!degree.getDegreeType().getLocalizedName().equals(parts[1])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[1]);
	    }
	    if (!degree.getName().equals(parts[2])) {
		throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[2]);
	    }
	    CycleType cycle;
	    try {
		cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[3]) - 1];
	    } catch (NumberFormatException e) {
		cycle = null;
	    }
	    try {
		EctsDegreeGraduationGradeConversionTable.createConversionTable(degree, executionInterval, cycle, Arrays
			.copyOfRange(parts, 4, 15), Arrays.copyOfRange(parts, 15, 26));
	    } catch (DuplicateEctsConversionTable e) {
	    }
	}
    }

    private Set<IEctsConversionTable> processGraduationByCycleStatus(EctsTableFilter filter) {
	final Unit ist = UnitUtils.readInstitutionUnit();
	Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
	for (CycleType cycle : CycleType.getSortedValues()) {
	    EctsCycleGraduationGradeConversionTable table = ist.getEctsGraduationGradeConversionTable(filter
		    .getExecutionInterval(), cycle);
	    if (table != null) {
		tables.add(table);
	    } else {
		tables.add(new NullEctsConversionTable(ist, cycle));
	    }
	}
	return tables;
    }

    private SimplifiedSpreadsheetBuilder<IEctsConversionTable> exportGraduationByCycleTemplate(EctsTableFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }

    private void importGraduationByCycleTables(AcademicInterval executionInterval, String file) {
	for (String line : file.split("\n")) {
	    String[] parts = line.split("\\s*;\\s*");
	    CycleType cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[0]) - 1];
	    final Unit ist = UnitUtils.readInstitutionUnit();
	    try {
		EctsCycleGraduationGradeConversionTable table = EctsCycleGraduationGradeConversionTable.createConversionTable(
			ist, executionInterval, cycle, Arrays.copyOfRange(parts, 1, 12), Arrays.copyOfRange(parts, 12, 23));
	    } catch (DuplicateEctsConversionTable e) {
	    }
	}
    }
}