package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine.PerformanceGridLineYearGroup;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.fenix.tools.excel.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookBuilder;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PerformanceGridSheetBuilder extends SpreadsheetBuilder<PerformanceGridLine> {
    ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
    List<ColumnGroup> groups = new ArrayList<ColumnGroup>();
    List<ColumnBuilder> columns = new ArrayList<ColumnBuilder>();

    class YearColumnGroup extends ColumnGroup {
	private int year;
	private ExecutionYear monitoredYear;
	private boolean tutorated;

	public YearColumnGroup(int year, ExecutionYear monitoredYear, boolean tutorated, ColumnBuilder... columns) {
	    super(columns);
	    this.year = year + 1;
	    this.monitoredYear = monitoredYear;
	    this.tutorated = tutorated;
	}

	@Override
	public void fillHeader(HSSFCell cell) {
	    cell.setCellValue(year + "º Ano (" + (tutorated ? monitoredYear.getName() : "Anos Anteriores") + ")");
	}
    }

    class SemesterColumnBuilder extends ColumnBuilder {
	private ExecutionYear monitoringYear;
	private int year;
	private int sem;
	private boolean tutorated;

	public SemesterColumnBuilder(String headerKey, ResourceBundle headerBundle, ExecutionYear monitoringYear, int year,
		int sem, boolean tutorated) {
	    super(headerKey, headerBundle);
	    this.monitoringYear = monitoringYear;
	    this.year = year;
	    this.sem = sem;
	    this.tutorated = tutorated;
	}

	@Override
	public void fillHeader(HSSFCell cell) {
	    super.fillHeader(cell);
	    cell.setCellValue(cell.getStringCellValue() + " (Ins/Ap/Re)");
	}

	@Override
	public void fillCell(HSSFCell cell, PerformanceGridLine item) {
	    PerformanceGridLineYearGroup yearEnrols = item.getStudentPerformanceByYear().get(year);
	    List enrols;
	    if (sem == 1) {
		enrols = yearEnrols.getFirstSemesterEnrolments();
	    } else {
		enrols = yearEnrols.getSecondSemesterEnrolments();
	    }

	    int notApproved = 0;
	    int approved = 0;
	    int notEvaluated = 0;
	    for (Object object : enrols) {
		Enrolment enrolment = (Enrolment) object;
		if (tutorated && !enrolment.getExecutionYear().equals(monitoringYear))
		    continue;
		if (!tutorated && enrolment.getExecutionYear().equals(monitoringYear))
		    continue;
		if (enrolment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
		    notApproved++;
		} else if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
		    approved++;
		} else if (enrolment.getEnrollmentState().equals(EnrollmentState.ENROLLED)
			|| enrolment.getEnrollmentState().equals(EnrollmentState.NOT_EVALUATED)) {
		    notEvaluated++;
		}
	    }
	    setValue(cell, notEvaluated + "/" + approved + "/" + notApproved);
	}
    }

    public PerformanceGridSheetBuilder(HSSFWorkbook book, PerformanceGridTableDTO performanceGridTable) {
	super(book);
	columns.add(new PropertyColumnBuilder("label.studentNumber", bundle, "registration.number"));
	columns.add(new PropertyColumnBuilder("label.name", bundle, "registration.person.name"));
	columns.add(new NullSafePropertyColumnBuilder("label.entryPhase", bundle, "registration.entryPhase.entryPhase",
		"registration.entryPhase"));
	columns.add(new PropertyColumnBuilder("label.entryGrade", bundle, "registration.entryGrade"));
	columns.add(new PropertyColumnBuilder("label.aritmeticAverage", bundle, "aritmeticAverage"));
	ColumnBuilder approvalFirstSemRatio = new FormatColumnBuilder("label.first.semester.short", bundle,
		"${approvedRatioFirstSemester} %%");
	ColumnBuilder approvalSecondSemRatio = new FormatColumnBuilder("label.second.semester.short", bundle,
		"${approvedRatioSecondSemester} %%");
	columns.add(approvalFirstSemRatio);
	columns.add(approvalSecondSemRatio);
	groups.add(new PropertyColumnGroup("label.approvedRatio", bundle, approvalFirstSemRatio, approvalSecondSemRatio));
	PerformanceGridLine sampleLine = performanceGridTable.getPerformanceGridTableLines().get(0);
	ExecutionYear monitoringYear = performanceGridTable.getMonitoringYear();
	int yearCount = sampleLine.getStudentPerformanceByYear().size();
	for (int year = 0; year < yearCount; year++) {
	    ColumnBuilder semester1Tutorated = new SemesterColumnBuilder("label.first.semester.short", bundle, monitoringYear,
		    year, 1, true);
	    ColumnBuilder semester2Tutorated = new SemesterColumnBuilder("label.second.semester.short", bundle, monitoringYear,
		    year, 2, true);
	    ColumnBuilder semester1NotTutorated = new SemesterColumnBuilder("label.first.semester.short", bundle, monitoringYear,
		    year, 1, false);
	    ColumnBuilder semester2NotTutorated = new SemesterColumnBuilder("label.second.semester.short", bundle,
		    monitoringYear, year, 2, false);
	    columns.add(semester1Tutorated);
	    columns.add(semester2Tutorated);
	    columns.add(semester1NotTutorated);
	    columns.add(semester2NotTutorated);
	    groups.add(new FormatColumnGroup("label.performanceGrid.year.tutorated", bundle, new String[] {
		    new Integer(year + 1).toString(), performanceGridTable.getMonitoringYear().getName() }, semester1Tutorated,
		    semester2Tutorated));
	    groups.add(new FormatColumnGroup("label.performanceGrid.year.notTutorated", bundle, new String[] { new Integer(
		    year + 1).toString() }, semester1NotTutorated, semester2NotTutorated));
	}
    }

    @Override
    protected List<ColumnGroup> getColumnGroups() {
	return groups;
    }

    @Override
    protected List<ColumnBuilder> getColumns() {
	return columns;
    }

    protected void build(WorkbookBuilder book) {
	
    }
}