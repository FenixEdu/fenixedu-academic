package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

public class ReportStudentsUTLCandidates implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionYear forExecutionYear;
    private List<StudentLine> correctStudentLines;
    private List<StudentLine> erroneousStudentLines;
    private CellStyle headerStyle;

    public ReportStudentsUTLCandidates(final ExecutionYear forExecutionYear, final HSSFSheet sheet) {
	this.forExecutionYear = forExecutionYear;
	this.correctStudentLines = new ArrayList<StudentLine>();
	this.erroneousStudentLines = new ArrayList<StudentLine>();

	getStudentLines(sheet);
    }

    private void getStudentLines(HSSFSheet sheet) {
	int i = 2;
	HSSFRow row;
	while ((row = sheet.getRow(i)) != null) {
	    StudentLine studentLine = new StudentLine();
	    boolean filledWithSuccess = studentLine.fillWithSpreadsheetRow(forExecutionYear, row);

	    if (filledWithSuccess) {
		correctStudentLines.add(studentLine);
	    } else {
		erroneousStudentLines.add(studentLine);
	    }

	    i++;
	}
    }

    public HSSFWorkbook generateReport() {
	HSSFWorkbook wb = new HSSFWorkbook();

	headerStyle = headerBackgroundStyle(wb);
	fillRegimeTable(wb);
	
	HSSFSheet sheet = wb.createSheet("Dados Academicos");

	addHeaders(sheet);

	addValues(sheet);

	return wb;
    }

    private void fillRegimeTable(HSSFWorkbook wb) {
	HSSFSheet sheet = wb.createSheet("Regime");

	for (int i = 0; i <= 7; i++) {
	    HSSFRow row = sheet.createRow(i);
	    row.createCell(0).setCellValue(false);
	    row.createCell(1).setCellValue(false);
	}
    }

    private void addValues(HSSFSheet sheet) {
	int i = 2;
	for (StudentLine studentLine : getCorrectStudentLines()) {

	    try {
		HSSFRow row = sheet.createRow(i);

		addCellValue(row, onNullEmptyString(studentLine.getInstitutionCode()), 0);
		addCellValue(row, onNullEmptyString(studentLine.getInstitutionName()), 1);
		addCellValue(row, onNullEmptyString(studentLine.getCandidacyNumber()), 2);
		addCellValue(row, onNullEmptyString(studentLine.getStudentNumberForPrint()), 3);
		addCellValue(row, onNullEmptyString(studentLine.getStudentName()), 4);
		addCellValue(row, onNullEmptyString(studentLine.getDocumentTypeName()), 5);
		addCellValue(row, onNullEmptyString(studentLine.getDocumentNumber()), 6);
		addCellValue(row, onNullEmptyString(studentLine.getDegreeCode()), 7);
		addCellValue(row, onNullEmptyString(studentLine.getDegreeName()), 8);
		addCellValue(row, onNullEmptyString(studentLine.getDegreeTypeName()), 9);
		addCellValue(row, "", 10);
		addCellValue(row, onNullEmptyString(studentLine.getCountNumberOfDegreeChanges()), 11);
		addCellValue(row, onNullEmptyString(studentLine.getHasMadeDegreeChange()), 12);
		addCellValue(row, onNullEmptyString(studentLine.getFirstEnrolmentOnCurrentExecutionYear()), 13);
		addCellValue(row, onNullEmptyString(studentLine.getRegime()), 14);
		addCellValue(
			row,
 "", // VALUE(IF(ISNA(VLOOKUP(O4,$Regime.A2:B6,2,FALSE()));\"Regime não tipificado\";VLOOKUP(O4,$Regime.A2:B6,2,FALSE())))",
			15);
		addCellValue(row, onNullEmptyString(studentLine.getFirstRegistrationExecutionYear()), 16);
		addCellValue(row, onNullEmptyString(studentLine.getCountNumberOfEnrolmentsYearsSinceRegistrationStart()), 17);
		addCellValue(row, onNullEmptyString(studentLine.getNumberOfDegreeCurricularYears()), 18);
		addCellValue(row, onNullEmptyString(studentLine.getCurricularYearOneYearAgo()), 19);
		addCellValue(row, onNullEmptyString(studentLine.getNumberOfEnrolledEctsOneYearAgo()), 20);
		addCellValue(row, onNullEmptyString(studentLine.getNumberOfApprovedEctsOneYearAgo()), 21);
		addCellValue(row, "", 22); // "=IF(V5/U5<50%,\"Não\",\"Sim\")",
					   // 22);
		addCellValue(row, onNullEmptyString(studentLine.getCurricularYearInCurrentYear()), 23);
		addCellValue(row, onNullEmptyString(studentLine.getNumberOfEnrolledECTS()), 24);
		addCellValue(
			row,
 "", // "IF(P3=4;\"Não Aplicável\";IF(P3=3;IF(S3<=3;IF((2*S3+2)>=R3+(S3-X3);\"SIM\";\"NÃO\");IF((2*S3+3)>=R3+(S3-X3);\"SIM\";\"NÃO\"));IF(OR(P3=2;L3>0);IF(S3<=3;IF((S3+2)>=R3+(S3-X3);\"SIM\";\"NÃO\");IF(S3>3;IF((S3+3)>=R3+(S3-X3);\"SIM\";\"NÃO\")));IF(AND(L3=0;P3=1);IF(S3<=3;IF(S3+1>=R3+(S3-X3);\"SIM\";\"NÃO\");IF(S3>3;IF(S3+2>=R3+(S3-X3);\"SIM\";\"NÃO\")))))))",
			25);
		addCellValue(
			row,
 "", // "IF(P3=4;IF(W3=\"Sim\";\"Deferido\";\"Indeferido\");IF(Y3>=30;IF(OR(W3=\"NÃO\";Z3=\"NÃO\";AI3=\"Sim\");\"Indeferido\";\"Deferido\");\"Indeferido\"))",
			26);
		addCellValue(row, onNullEmptyString(studentLine.getGratuityAmount()), 27);
		addCellValue(row, onNullEmptyString(studentLine.getNumberOfMonthsExecutionYear()), 28);
		addCellValue(row, onNullEmptyString(studentLine.getFirstMonthOfPayment()), 29);
		addCellValue(row, onNullEmptyString(studentLine.getOwnerOfCETQualification()), 30);
		addCellValue(row, onNullEmptyString(studentLine.isDegreeQualificationOwner()), 31);
		addCellValue(row, onNullEmptyString(studentLine.isMasterQualificationOwner()), 32);
		addCellValue(row, onNullEmptyString(studentLine.isPhdQualificationOwner()), 33);
		addCellValue(row, onNullEmptyString(studentLine.isOwnerOfCollegeQualification()), 34);
		addCellValue(row, onNullEmptyString(studentLine.getObservations()), 35);

	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    i++;
	}
    }

    private String onNullEmptyString(Object value) {

	if (value == null) {
	    return "";
	} else if (value instanceof String) {
	    return (String) value;
	} else if (value instanceof Boolean) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");
	    return bundle.getString(((Boolean) value) ? "label.yes" : "label.no");
	}

	return value.toString();
    }

    private void addHeaders(HSSFSheet sheet) {
	sheet.createRow(0);
	sheet.createRow(1);

	ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");

	addHeaderCell(sheet, getHeaderInBundle(bundle, "institutionCode"), 0);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "institutionName"), 1);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "candidacyNumber"), 2);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "studentNumberForPrint"), 3);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "studentName"), 4);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "documentTypeName"), 5);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "documentNumber"), 6);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeCode"), 7);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeName"), 8);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeTypeName"), 9);
	addHeaderCell(sheet, "", 10);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "countNumberOfDegreeChanges"), 11);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "hasMadeDegreeChange"), 12);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "firstEnrolmentOnCurrentExecutionYear"), 13);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "regime"), 14);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "regimeCode"), 15);

	HSSFRow row = sheet.getRow(0);
	HSSFCell cell = row.createCell(16);
	cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies"));
	cell.setCellStyle(headerStyle);
	sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 17));

	cell = sheet.getRow(1).createCell(16);
	cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.year"));
	cell.setCellStyle(headerStyle);

	cell = sheet.getRow(1).createCell(17);
	cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.count"));
	cell.setCellStyle(headerStyle);

	addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfDegreeCurricularYears"), 18);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "curricularYearOneYearAgo"), 19);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfEnrolledEctsOneYearAgo"), 20);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfApprovedEctsOneYearAgo"), 21);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "studentHadPerformanceLastYear"), 22);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "curricularYearInCurrentYear"), 23);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfEnrolledECTS"), 24);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeConclusionValue"), 25);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "finalResultValue"), 26);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "gratuityAmount"), 27);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfMonthsExecutionYear"), 28);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "firstMonthOfPayment"), 29);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCETQualification"), 30);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeQualificationOwner"), 31);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "masterQualificationOwner"), 32);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "phdQualificationOwner"), 33);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCollegeQualification"), 34);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "observations"), 35);
    }
    
    private String getHeaderInBundle(ResourceBundle bundle, String field) {
	return bundle.getString("label.net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.StudentLine." + field);
    }

    private void addHeaderCell(HSSFSheet sheet, String value, int columnNumber) {
	HSSFRow row = sheet.getRow(0);
	HSSFCell cell = row.createCell(columnNumber);
	
	cell.setCellValue(value);
	cell.setCellStyle(headerStyle);
	
	sheet.addMergedRegion(new CellRangeAddress(0, 1, columnNumber, columnNumber));
    }

    private CellStyle headerBackgroundStyle(final HSSFWorkbook wb) {
	CellStyle style = wb.createCellStyle();
	style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
	style.setFillPattern(CellStyle.BIG_SPOTS);
	
	return style;
    }

    private void addCellValue(HSSFRow row, String value, int cellNumber) {
	HSSFCell cell = row.createCell(cellNumber);
	cell.setCellValue(value);
    }

    private void addCellFormula(HSSFRow row, String value, int cellNumber) {
	HSSFCell cell = row.createCell(cellNumber);
	cell.setCellFormula(value);
    }

    public List<StudentLine> getCorrectStudentLines() {
	return correctStudentLines;
    }

    public List<StudentLine> getErroneousStudentLines() {
	return erroneousStudentLines;
    }
}
