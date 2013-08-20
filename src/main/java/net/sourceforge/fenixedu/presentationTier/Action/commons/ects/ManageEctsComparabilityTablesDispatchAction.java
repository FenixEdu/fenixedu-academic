package net.sourceforge.fenixedu.presentationTier.Action.commons.ects;

import java.io.IOException;
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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsComparabilityPercentages;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsComparabilityTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCompetenceCourseConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCycleGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex;
import net.sourceforge.fenixedu.domain.degreeStructure.IEctsConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.NullEctsConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/manageEctsComparabilityTables", module = "gep")
@Forwards({ @Forward(name = "index", path = "/gep/ects/comparabilityTableIndex.jsp", tileProperties = @Tile(
        title = "private.gep.gepportal.ectstables")) })
public class ManageEctsComparabilityTablesDispatchAction extends FenixDispatchAction {
    private static final String SEPARATOR = "\\t";

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("resources.GEPResources", Language.getLocale());

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
                SheetData<?> builder = exportTemplate(request, filter);
                response.setContentType("text/csv");
                response.setHeader("Content-disposition", "attachment; filename=template.tsv");
                new SpreadsheetBuilder().addSheet("template", builder)
                        .build(WorkbookExportFormat.TSV, response.getOutputStream());
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
            try {
                importTables(filter.getExecutionInterval(), filter.getType(), filter.getLevel(), filter.getContent());
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            } catch (IOException e) {
                addActionMessage(request, "error.ects.table.unableToReadTablesFile");
            }
            processStatus(request, filter);
        } finally {
            filter.clearFileContent();
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("filter", filter);
        return mapping.findForward("index");
    }

    private EctsTableFilter readFilter(HttpServletRequest request) {
        EctsTableFilter filter = getRenderedObject("filter");
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

    private SheetData<?> exportTemplate(HttpServletRequest request, EctsTableFilter filter) {
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
            throw new DomainException("error.ects.table.unableToReadTablesFile");
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
        case SCHOOL:
            return processEnrolmentByInstitutionStatus(filter);
        default:
            return Collections.emptySet();
        }
    }

    private SheetData<?> exportEnrolmentTemplate(EctsTableFilter filter) {
        switch (filter.getLevel()) {
        case COMPETENCE_COURSE:
            return exportEnrolmentByCompetenceCourseTemplate(filter);
        case DEGREE:
            return exportEnrolmentByDegreeTemplate(filter);
        case CURRICULAR_YEAR:
            return exportEnrolmentByCurricularYearTemplate(filter);
        case SCHOOL:
            return exportEnrolmentByInstitutionTemplate(filter);
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
        case SCHOOL:
            importEnrolmentByInstitutionTables(executionInterval, file);
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

    private SheetData<IEctsConversionTable> exportGraduationTemplate(EctsTableFilter filter) {
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
                    && competenceCourse.hasActiveScopesInExecutionYear(year)) {
                EctsCompetenceCourseConversionTable table =
                        EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getEnrolmentTableBy(competenceCourse);
                if (table != null) {
                    tables.add(table);
                } else {
                    tables.add(new NullEctsConversionTable(competenceCourse));
                }
            }
        }
        return tables;
    }

    private SheetData<IEctsConversionTable> exportEnrolmentByCompetenceCourseTemplate(EctsTableFilter filter) {
        final ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
        final ExecutionSemester querySemester = year.getFirstExecutionPeriod();
        SheetData<IEctsConversionTable> builder =
                new SheetData<IEctsConversionTable>(processEnrolmentByCompetenceCourseStatus(filter)) {
                    @Override
                    protected void makeLine(IEctsConversionTable table) {
                        CompetenceCourse competence = (CompetenceCourse) table.getTargetEntity();
                        addCell(BUNDLE.getString("label.externalId"), competence.getExternalId());
                        addCell(BUNDLE.getString("label.departmentUnit.name"), competence.getDepartmentUnit().getName());
                        addCell(BUNDLE.getString("label.competenceCourse.name"), competence.getName(querySemester));
                        addCell(BUNDLE.getString("label.acronym"), competence.getAcronym(querySemester));
                        addCell(BUNDLE.getString("label.externalId"), competence.getExternalId());
                        Set<String> ids = new HashSet<String>();
                        for (CurricularCourse course : competence.getAssociatedCurricularCoursesSet()) {
                            List<ExecutionCourse> executions = course.getExecutionCoursesByExecutionYear(year);
                            for (ExecutionCourse executionCourse : executions) {
                                if (!ids.contains(executionCourse.getExternalId().toString())) {
                                    ids.add(executionCourse.getExternalId().toString());
                                }
                            }
                        }
                        addCell(BUNDLE.getString("label.competenceCourse.executionCodes"), StringUtils.join(ids, ", "));
                        EctsComparabilityTable ects = table.getEctsTable();
                        for (int i = 10; i <= 20; i++) {
                            addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                        }
                    }
                };
        return builder;
    }

    @Service
    private void importEnrolmentByCompetenceCourseTables(AcademicInterval executionInterval, String file) {
        ExecutionSemester querySemester = ExecutionYear.readByAcademicInterval(executionInterval).getFirstExecutionPeriod();
        for (String line : file.split("\n")) {
            if (!line.startsWith(BUNDLE.getString("label.externalId"))) {
                String[] parts = fillArray(line.split(SEPARATOR), 17);
                CompetenceCourse competence = AbstractDomainObject.fromExternalId(parts[0]);
                // if
                // (!competence.getDepartmentUnit().getName().equals(parts[1]))
                // {
                // throw new
                // DomainException("error.ects.invalidLine.nonMatchingCourse",
                // parts[0], parts[1], competence
                // .getDepartmentUnit().getName());
                // }
                // if (!competence.getName(querySemester).equals(parts[2])) {
                // throw new
                // DomainException("error.ects.invalidLine.nonMatchingCourse",
                // parts[0], parts[2],
                // competence.getName(querySemester));
                // }
                EctsCompetenceCourseConversionTable.createConversionTable(competence, executionInterval,
                        Arrays.copyOfRange(parts, 6, 17));
            }
        }
    }

    private Set<IEctsConversionTable> processEnrolmentByDegreeStatus(EctsTableFilter filter) {
        ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
        Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
        for (Degree degree : rootDomainObject.getDegreesSet()) {
            if (degree.getDegreeCurricularPlansExecutionYears().contains(year)
                    && (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)
                            || degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)
                            || degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) || degree
                            .getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA))) {
                for (int i = 1; i <= degree.getDegreeType().getYears(); i++) {
                    EctsDegreeByCurricularYearConversionTable table =
                            EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getEnrolmentTableBy(degree,
                                    CurricularYear.readByYear(i));
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

    private SheetData<IEctsConversionTable> exportEnrolmentByDegreeTemplate(EctsTableFilter filter) {
        SheetData<IEctsConversionTable> builder = new SheetData<IEctsConversionTable>(processEnrolmentByDegreeStatus(filter)) {
            @Override
            protected void makeLine(IEctsConversionTable table) {
                Degree degree = (Degree) table.getTargetEntity();
                addCell(BUNDLE.getString("label.externalId"), degree.getExternalId());
                addCell(BUNDLE.getString("label.degreeType"), degree.getDegreeType().getLocalizedName());
                addCell(BUNDLE.getString("label.name"), degree.getName());
                addCell(BUNDLE.getString("label.curricularYear"), table.getCurricularYear().getYear());
                EctsComparabilityTable ects = table.getEctsTable();
                for (int i = 10; i <= 20; i++) {
                    addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                }
            }
        };
        return builder;
    }

    @Service
    private void importEnrolmentByDegreeTables(AcademicInterval executionInterval, String file) {
        for (String line : file.split("\n")) {
            if (!line.startsWith(BUNDLE.getString("label.externalId"))) {
                String[] parts = fillArray(line.split(SEPARATOR), 15);
                Degree degree = AbstractDomainObject.fromExternalId(parts[0]);
                if (!degree.getDegreeType().getLocalizedName().equals(parts[1])) {
                    throw new DomainException("error.ects.invalidLine.nonMatchingCourse", parts[0], parts[1], degree
                            .getDegreeType().getLocalizedName());
                }
                if (!degree.getName().equals(parts[2])) {
                    throw new DomainException("error.ects.invalidLine.nonMatchingCourse", parts[0], parts[2], degree.getName());
                }
                CurricularYear year = CurricularYear.readByYear(Integer.parseInt(parts[3]));
                EctsDegreeByCurricularYearConversionTable.createConversionTable(degree, executionInterval, year,
                        Arrays.copyOfRange(parts, 4, 15));
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
            default:
                years = Collections.emptyList();
            }
            for (Integer year : years) {
                EctsInstitutionByCurricularYearConversionTable table =
                        EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getEnrolmentTableBy(ist,
                                CurricularYear.readByYear(year), cycle);
                if (table != null) {
                    tables.add(table);
                } else {
                    tables.add(new NullEctsConversionTable(ist, cycle, CurricularYear.readByYear(year)));
                }
            }
        }
        return tables;
    }

    private SheetData<IEctsConversionTable> exportEnrolmentByCurricularYearTemplate(EctsTableFilter filter) {
        SheetData<IEctsConversionTable> builder =
                new SheetData<IEctsConversionTable>(processEnrolmentByCurricularYearStatus(filter)) {
                    @Override
                    protected void makeLine(IEctsConversionTable table) {
                        // FIXME: should not depend on ordinal(), use a resource bundle
                        // or something.
                        addCell(BUNDLE.getString("label.cycle"), table.getCycle().ordinal() + 1);
                        addCell(BUNDLE.getString("label.curricularYear"), table.getCurricularYear().getYear());
                        EctsComparabilityTable ects = table.getEctsTable();
                        for (int i = 10; i <= 20; i++) {
                            addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                        }
                    }
                };
        return builder;
    }

    @Service
    private void importEnrolmentByCurricularYearTables(AcademicInterval executionInterval, String file) {
        for (String line : file.split("\n")) {
            if (!line.startsWith(BUNDLE.getString("label.cycle"))) {
                String[] parts = fillArray(line.split(SEPARATOR), 13);
                final Unit ist = UnitUtils.readInstitutionUnit();
                CycleType cycle;
                try {
                    cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[0]) - 1];
                } catch (NumberFormatException e) {
                    cycle = null;
                }
                CurricularYear year = CurricularYear.readByYear(Integer.parseInt(parts[1]));
                EctsInstitutionByCurricularYearConversionTable.createConversionTable(ist, executionInterval, cycle, year,
                        Arrays.copyOfRange(parts, 2, 13));
            }
        }
    }

    private Set<IEctsConversionTable> processEnrolmentByInstitutionStatus(EctsTableFilter filter) {
        final Unit ist = UnitUtils.readInstitutionUnit();
        Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
        EctsConversionTable table = EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getEnrolmentTableBy(ist);
        if (table != null) {
            tables.add(table);
        } else {
            tables.add(new NullEctsConversionTable(ist));
        }
        return tables;
    }

    private SheetData<IEctsConversionTable> exportEnrolmentByInstitutionTemplate(EctsTableFilter filter) {
        SheetData<IEctsConversionTable> builder =
                new SheetData<IEctsConversionTable>(processEnrolmentByInstitutionStatus(filter)) {

                    @Override
                    protected void makeLine(IEctsConversionTable table) {
                        EctsComparabilityTable ects = table.getEctsTable();
                        for (int i = 10; i <= 20; i++) {
                            addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                        }
                    }

                };
        return builder;
    }

    @Service
    private void importEnrolmentByInstitutionTables(AcademicInterval executionInterval, String file) {
        for (String line : file.split("\n")) {
            if (!line.startsWith("10")) {
                String[] table = fillArray(line.split(SEPARATOR), 11);
                final Unit ist = UnitUtils.readInstitutionUnit();
                EctsInstitutionConversionTable.createConversionTable(ist, executionInterval, table);
            }
        }
    }

    private Set<IEctsConversionTable> processGraduationByDegreeStatus(EctsTableFilter filter) {
        ExecutionYear year = (ExecutionYear) ExecutionYear.getExecutionInterval(filter.getExecutionInterval());
        Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
        for (Degree degree : rootDomainObject.getDegreesSet()) {
            if (degree.getDegreeCurricularPlansExecutionYears().contains(year)
                    && (degree.getDegreeType().equals(DegreeType.BOLONHA_DEGREE)
                            || degree.getDegreeType().equals(DegreeType.BOLONHA_MASTER_DEGREE)
                            || degree.getDegreeType().equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) || degree
                            .getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA))) {
                for (CycleType cycle : degree.getDegreeType().getCycleTypes()) {
                    EctsDegreeGraduationGradeConversionTable table =
                            EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getGraduationTableBy(degree, cycle);
                    if (table != null) {
                        tables.add(table);
                    } else {
                        if (degree.getDegreeType().isComposite()) {
                            tables.add(new NullEctsConversionTable(degree, cycle));
                        } else {
                            tables.add(new NullEctsConversionTable(degree));
                        }
                    }
                }
            }
        }
        return tables;
    }

    private SheetData<IEctsConversionTable> exportGraduationByDegreeTemplate(EctsTableFilter filter) {
        SheetData<IEctsConversionTable> builder = new SheetData<IEctsConversionTable>(processGraduationByDegreeStatus(filter)) {
            @Override
            protected void makeLine(IEctsConversionTable table) {
                Degree degree = (Degree) table.getTargetEntity();
                addCell(BUNDLE.getString("label.externalId"), degree.getExternalId());
                addCell(BUNDLE.getString("label.degreeType"), degree.getDegreeType().getLocalizedName());
                addCell(BUNDLE.getString("label.name"), degree.getName());
                addCell(BUNDLE.getString("label.cycle"), table.getCycle() != null ? table.getCycle().ordinal() + 1 : null);
                EctsComparabilityTable ects = table.getEctsTable();
                for (int i = 10; i <= 20; i++) {
                    addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                }
                EctsComparabilityPercentages percentages = table.getPercentages();
                for (int i = 10; i <= 20; i++) {
                    addCell(i + "", percentages.getPercentage(i) != -1 ? percentages.getPercentage(i) : null);
                }
            }
        };
        return builder;
    }

    @Service
    private void importGraduationByDegreeTables(AcademicInterval executionInterval, String file) {
        for (String line : file.split("\n")) {
            if (!line.startsWith(BUNDLE.getString("label.externalId"))) {
                String[] parts = fillArray(line.split(SEPARATOR), 26);
                Degree degree = AbstractDomainObject.fromExternalId(parts[0]);
                if (!degree.getDegreeType().getLocalizedName().equals(parts[1])) {
                    throw new DomainException("error.ects.invalidLine.nonMatchingCourse", parts[0], parts[1], degree
                            .getDegreeType().getLocalizedName());
                }
                if (!degree.getName().equals(parts[2])) {
                    throw new DomainException("error.ects.invalidLine.nonMatchingCourse", parts[0], parts[2], degree.getName());
                }
                CycleType cycle;
                try {
                    cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[3]) - 1];
                } catch (NumberFormatException e) {
                    cycle = null;
                }
                EctsDegreeGraduationGradeConversionTable.createConversionTable(degree, executionInterval, cycle,
                        Arrays.copyOfRange(parts, 4, 15), Arrays.copyOfRange(parts, 15, 26));
            }
        }
    }

    private Set<IEctsConversionTable> processGraduationByCycleStatus(EctsTableFilter filter) {
        final Unit ist = UnitUtils.readInstitutionUnit();
        Set<IEctsConversionTable> tables = new HashSet<IEctsConversionTable>();
        for (CycleType cycle : CycleType.getSortedValues()) {
            EctsGraduationGradeConversionTable table =
                    EctsTableIndex.readOrCreateByYear(filter.getExecutionInterval()).getGraduationTableBy(cycle);
            if (table != null) {
                tables.add(table);
            } else {
                tables.add(new NullEctsConversionTable(ist, cycle));
            }
        }
        return tables;
    }

    private SheetData<IEctsConversionTable> exportGraduationByCycleTemplate(EctsTableFilter filter) {
        SheetData<IEctsConversionTable> builder = new SheetData<IEctsConversionTable>(processGraduationByCycleStatus(filter)) {
            @Override
            protected void makeLine(IEctsConversionTable table) {
                // FIXME: should not depend on ordinal(), use a resource bundle
                // or something.
                addCell(BUNDLE.getString("label.cycle"), table.getCycle().ordinal() + 1);
                EctsComparabilityTable ects = table.getEctsTable();
                for (int i = 10; i <= 20; i++) {
                    addCell(i + "", !ects.convert(i).equals(GradeScale.NA) ? ects.convert(i) : null);
                }
                EctsComparabilityPercentages percentages = table.getPercentages();
                for (int i = 10; i <= 20; i++) {
                    addCell(i + "", percentages.getPercentage(i) != -1 ? percentages.getPercentage(i) : null);
                }
            }
        };
        return builder;
    }

    @Service
    private void importGraduationByCycleTables(AcademicInterval executionInterval, String file) {
        for (String line : file.split("\n")) {
            if (!line.startsWith(BUNDLE.getString("label.cycle"))) {
                String[] parts = fillArray(line.split(SEPARATOR), 23);
                CycleType cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[0]) - 1];
                final Unit ist = UnitUtils.readInstitutionUnit();
                EctsCycleGraduationGradeConversionTable.createConversionTable(ist, executionInterval, cycle,
                        Arrays.copyOfRange(parts, 1, 12), Arrays.copyOfRange(parts, 12, 23));
            }
        }
    }

    private String[] fillArray(String[] array, int length) {
        String[] filled = new String[length];
        for (int i = 0; i < array.length; i++) {
            filled[i] = array[i];
        }
        return filled;
    }
}