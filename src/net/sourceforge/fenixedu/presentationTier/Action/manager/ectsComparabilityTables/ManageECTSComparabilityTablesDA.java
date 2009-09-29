package net.sourceforge.fenixedu.presentationTier.Action.manager.ectsComparabilityTables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsComparabilityTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCompetenceCourseConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsCycleGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsInstitutionByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsConversionTable.DuplicateEctsConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageECTSComparabilityTables", module = "manager")
@Forwards( { @Forward(name = "index", path = "/manager/ectsComparabilityTables/index.jsp"),
	@Forward(name = "tableInputStatus", path = "/manager/ectsComparabilityTables/tableInputStatus.jsp") })
public class ManageECTSComparabilityTablesDA extends FenixDispatchAction {
    public static class ECTSComparabilityTableStatistics implements Serializable {
	private static final long serialVersionUID = -6768974069136005454L;

	private final ECTSTableInputBean input;

	private long competenceCoursesWithTable;

	private long competenceCoursesWithoutTable;

	public void setCompetenceCoursesWithTable(long competenceCoursesWithTable) {
	    this.competenceCoursesWithTable = competenceCoursesWithTable;
	}

	public long getCompetenceCoursesWithTable() {
	    return competenceCoursesWithTable;
	}

	public void setCompetenceCoursesWithoutTable(long competenceCoursesWithoutTable) {
	    this.competenceCoursesWithoutTable = competenceCoursesWithoutTable;
	}

	public long getCompetenceCoursesWithoutTable() {
	    return competenceCoursesWithoutTable;
	}

	public ECTSComparabilityTableStatistics(ECTSTableInputBean target) {
	    this.input = target;
	    for (CompetenceCourse competenceCourse : rootDomainObject.getCompetenceCoursesSet()) {
		ExecutionYear year = ExecutionYear.readByAcademicInterval(target.getExecutionInterval());
		if ((competenceCourse.getCurricularStage() != CurricularStage.APPROVED || competenceCourse.getCurricularStage() != CurricularStage.PUBLISHED)
			&& competenceCourse.hasActiveScopesInExecutionYear(year)) {
		    boolean found = false;
		    for (EctsCompetenceCourseConversionTable table : competenceCourse.getEctsConversionTables()) {
			if (table.getYear().equals(target.getExecutionInterval())) {
			    found = true;
			}
		    }
		    if (found) {
			competenceCoursesWithTable++;
		    } else {
			competenceCoursesWithoutTable++;
		    }
		}
	    }
	}
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("tableInput", new ECTSTableInputBean());
	return mapping.findForward("index");
    }

    public ActionForward submitInput(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ECTSTableInputBean tableInput = (ECTSTableInputBean) getRenderedObject("tableInput");
	RenderUtils.invalidateViewState("tableInput");
	if (tableInput.getExecutionInterval() != null) {
	    // request.setAttribute("statistics", new
	    // ECTSComparabilityTableStatistics(input));
	    if (tableInput.getInputStream() != null) {
		try {
		    switch (tableInput.getType()) {
		    case COMPETENCE_COURSE:
			request.setAttribute("importStatus", readCompetenceCourseTable(tableInput));
			break;
		    case COMPETENCE_COURSE_BY_DEGREE:
			request.setAttribute("importStatus", readDegreeTable(tableInput));
			break;
		    case COMPETENCE_COURSE_BY_CURRICULAR_YEAR:
			request.setAttribute("importStatus", readCurricularYearTable(tableInput));
			break;
		    case GRADUATED_BY_DEGREE:
			request.setAttribute("importStatus", readDegreeGraduationTable(tableInput));
			break;
		    case GRADUATED_BY_CYCLE:
			request.setAttribute("importStatus", readCycleGraduationTable(tableInput));
			break;
		    }
		} catch (IOException e) {
		    addActionMessage(request, "error.ectsComparabilityTable.errorReadingTableFile");
		} catch (DomainException e) {
		    addActionMessage(request, e.getKey(), e.getArgs());
		}
	    }
	}
	return mapping.findForward("tableInputStatus");
    }

    private List<ECTSTableImportStatusLine> readCompetenceCourseTable(ECTSTableInputBean tableInput) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(tableInput.getInputStream()));
	String line = null;
	List<ECTSTableImportStatusLine> tables = new ArrayList<ECTSTableImportStatusLine>();
	try {
	    while ((line = reader.readLine()) != null) {
		String[] parts = line.split("\\s*;\\s*");
		CompetenceCourse competence = DomainObject.fromExternalId(parts[0]);
		if (!competence.getDepartmentUnit().getName().equals(parts[1])) {
		    throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[1]);
		}
		if (!competence.getName().equals(parts[2])) {
		    throw new DomainException("error.ectsComparabilityTable.invalidLine.nonMatchingCourse", parts[0], parts[2]);
		}
		try {
		    EctsCompetenceCourseConversionTable table = EctsCompetenceCourseConversionTable.createConversionTable(
			    competence, tableInput.getExecutionInterval(), Arrays.copyOfRange(parts, 6, 17));
		    tables.add(new ECTSTableImportStatusLine(table, ECTSTableImportStatusLine.TableImportStatus.SUCCESS));
		} catch (DuplicateEctsConversionTable e) {
		    EctsCompetenceCourseConversionTable table = competence.getEctsCourseConversionTable(tableInput
			    .getExecutionInterval());
		    if (table.getEctsTable().equals(new EctsComparabilityTable(Arrays.copyOfRange(parts, 6, 17)))) {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_EQUAL));
		    } else {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_DIFFERENT));
		    }
		}
	    }
	    return tables;
	} finally {
	    reader.close();
	}
    }

    private List<ECTSTableImportStatusLine> readDegreeTable(ECTSTableInputBean tableInput) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(tableInput.getInputStream()));
	String line = null;
	List<ECTSTableImportStatusLine> tables = new ArrayList<ECTSTableImportStatusLine>();
	try {
	    while ((line = reader.readLine()) != null) {
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
		    EctsDegreeByCurricularYearConversionTable table = EctsDegreeByCurricularYearConversionTable
			    .createConversionTable(degree, tableInput.getExecutionInterval(), year, Arrays.copyOfRange(parts, 4,
				    15));
		    tables.add(new ECTSTableImportStatusLine(table, ECTSTableImportStatusLine.TableImportStatus.SUCCESS));
		} catch (DuplicateEctsConversionTable e) {
		    EctsDegreeByCurricularYearConversionTable table = degree.getEctsCourseConversionTable(tableInput
			    .getExecutionInterval(), year);
		    if (table.getEctsTable().equals(new EctsComparabilityTable(Arrays.copyOfRange(parts, 4, 15)))) {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_EQUAL));
		    } else {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_DIFFERENT));
		    }
		}
	    }
	    return tables;
	} finally {
	    reader.close();
	}
    }

    private List<ECTSTableImportStatusLine> readCurricularYearTable(ECTSTableInputBean tableInput) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(tableInput.getInputStream()));
	String line = null;
	List<ECTSTableImportStatusLine> tables = new ArrayList<ECTSTableImportStatusLine>();
	try {
	    while ((line = reader.readLine()) != null) {
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
		    EctsInstitutionByCurricularYearConversionTable table = EctsInstitutionByCurricularYearConversionTable
			    .createConversionTable(ist, tableInput.getExecutionInterval(), cycle, year, Arrays.copyOfRange(parts,
				    2, 13));
		    tables.add(new ECTSTableImportStatusLine(table, ECTSTableImportStatusLine.TableImportStatus.SUCCESS));
		} catch (DuplicateEctsConversionTable e) {
		    EctsInstitutionByCurricularYearConversionTable table = ist.getEctsCourseConversionTable(tableInput
			    .getExecutionInterval(), cycle, year);
		    if (table.getEctsTable().equals(new EctsComparabilityTable(Arrays.copyOfRange(parts, 2, 13)))) {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_EQUAL));
		    } else {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_DIFFERENT));
		    }
		}
	    }
	    return tables;
	} finally {
	    reader.close();
	}
    }

    private List<ECTSTableImportStatusLine> readDegreeGraduationTable(ECTSTableInputBean tableInput) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(tableInput.getInputStream()));
	String line = null;
	List<ECTSTableImportStatusLine> tables = new ArrayList<ECTSTableImportStatusLine>();
	try {
	    while ((line = reader.readLine()) != null) {
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
		    EctsDegreeGraduationGradeConversionTable table = EctsDegreeGraduationGradeConversionTable
			    .createConversionTable(degree, tableInput.getExecutionInterval(), cycle, Arrays.copyOfRange(parts, 4,
				    15), Arrays.copyOfRange(parts, 15, 26));
		    tables.add(new ECTSTableImportStatusLine(table, ECTSTableImportStatusLine.TableImportStatus.SUCCESS));
		} catch (DuplicateEctsConversionTable e) {
		    EctsDegreeGraduationGradeConversionTable table = degree.getEctsGraduationGradeConversionTable(tableInput
			    .getExecutionInterval());
		    if (table.getEctsTable().equals(new EctsComparabilityTable(Arrays.copyOfRange(parts, 4, 15)))) {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_EQUAL));
		    } else {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_DIFFERENT));
		    }
		}
	    }
	    return tables;
	} finally {
	    reader.close();
	}
    }

    private List<ECTSTableImportStatusLine> readCycleGraduationTable(ECTSTableInputBean tableInput) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(tableInput.getInputStream()));
	String line = null;
	List<ECTSTableImportStatusLine> tables = new ArrayList<ECTSTableImportStatusLine>();
	try {
	    while ((line = reader.readLine()) != null) {
		String[] parts = line.split("\\s*;\\s*");
		CycleType cycle = CycleType.getSortedValues().toArray(new CycleType[0])[Integer.parseInt(parts[0]) - 1];
		final Unit ist = UnitUtils.readInstitutionUnit();
		try {
		    EctsCycleGraduationGradeConversionTable table = EctsCycleGraduationGradeConversionTable
			    .createConversionTable(ist, tableInput.getExecutionInterval(), cycle, Arrays
				    .copyOfRange(parts, 1, 12), Arrays.copyOfRange(parts, 12, 23));
		    tables.add(new ECTSTableImportStatusLine(table, ECTSTableImportStatusLine.TableImportStatus.SUCCESS));
		} catch (DuplicateEctsConversionTable e) {
		    EctsCycleGraduationGradeConversionTable table = ist.getEctsGraduationGradeConversionTable(tableInput
			    .getExecutionInterval(), cycle);
		    if (table.getEctsTable().equals(new EctsComparabilityTable(Arrays.copyOfRange(parts, 1, 12)))) {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_EQUAL));
		    } else {
			tables.add(new ECTSTableImportStatusLine(table,
				ECTSTableImportStatusLine.TableImportStatus.DUPLICATE_DIFFERENT));
		    }
		}
	    }
	    return tables;
	} finally {
	    reader.close();
	}
    }
}
