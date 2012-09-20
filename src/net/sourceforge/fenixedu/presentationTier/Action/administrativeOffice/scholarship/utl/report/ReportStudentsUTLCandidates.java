package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.Money;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.LocalDate;

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

		String institutionCode = studentLine.getInstitutionCode();
		String institutionName = studentLine.getInstitutionName();
		String candidacyNumber = studentLine.getCandidacyNumber();
		String studentNumberForPrint = studentLine.getStudentNumberForPrint();
		String studentName = studentLine.getStudentName();
		String documentTypeName = studentLine.getDocumentTypeName();
		String documentNumber = studentLine.getDocumentNumber();
		String degreeCode = studentLine.getDegreeCode();
		String degreeName = studentLine.getDegreeName();
		String degreeTypeName = studentLine.getDegreeTypeName();
		Integer countNumberOfDegreeChanges = studentLine.getCountNumberOfDegreeChanges();
		Boolean hasMadeDegreeChange = studentLine.getHasMadeDegreeChange();
		LocalDate firstEnrolmentOnCurrentExecutionYear = studentLine.getFirstEnrolmentOnCurrentExecutionYear();
		String regime = studentLine.getRegime();
		String firstRegistrationExecutionYear = studentLine.getFirstRegistrationExecutionYear();
		Integer countNumberOfEnrolmentsYearsSinceRegistrationStart = studentLine
			.getCountNumberOfEnrolmentsYearsSinceRegistrationStart();
		Integer numberOfDegreeCurricularYears = studentLine.getNumberOfDegreeCurricularYears();
		Integer curricularYearOneYearAgo = studentLine.getCurricularYearOneYearAgo();
		BigDecimal numberOfEnrolledEctsOneYearAgo = studentLine.getNumberOfEnrolledEctsOneYearAgo();
		BigDecimal numberOfApprovedEctsOneYearAgo = studentLine.getNumberOfApprovedEctsOneYearAgo();
		Integer curricularYearInCurrentYear = studentLine.getCurricularYearInCurrentYear();
		Double numberOfEnrolledECTS = studentLine.getNumberOfEnrolledECTS();
		Money gratuityAmount = studentLine.getGratuityAmount();
		Integer numberOfMonthsExecutionYear = studentLine.getNumberOfMonthsExecutionYear();
		String firstMonthOfPayment = studentLine.getFirstMonthOfPayment();
		Boolean ownerOfCETQualification = studentLine.getOwnerOfCETQualification();
		boolean degreeQualificationOwner = studentLine.isDegreeQualificationOwner();
		boolean masterQualificationOwner = studentLine.isMasterQualificationOwner();
		boolean phdQualificationOwner = studentLine.isPhdQualificationOwner();
		boolean ownerOfCollegeQualification = studentLine.isOwnerOfCollegeQualification();
		String observations = studentLine.getObservations();
		String lastEnrolmentExecutionYear = studentLine.getLastEnrolledExecutionYear();
		String nif = studentLine.getNif();

		HSSFRow row = sheet.createRow(i);
		addCellValue(row, onNullEmptyString(institutionCode), 0);
		addCellValue(row, onNullEmptyString(institutionName), 1);
		addCellValue(row, onNullEmptyString(candidacyNumber), 2);
		addCellValue(row, onNullEmptyString(studentNumberForPrint), 3);
		addCellValue(row, onNullEmptyString(studentName), 4);
		addCellValue(row, onNullEmptyString(documentTypeName), 5);
		addCellValue(row, onNullEmptyString(documentNumber), 6);
		addCellValue(row, onNullEmptyString(degreeCode), 7);
		addCellValue(row, onNullEmptyString(degreeName), 8);
		addCellValue(row, onNullEmptyString(degreeTypeName), 9);
		addCellValue(row, "", 10);
		addCellValue(row, onNullEmptyString(countNumberOfDegreeChanges), 11);
		addCellValue(row, onNullEmptyString(hasMadeDegreeChange), 12);
		addCellValue(row, onNullEmptyString(firstEnrolmentOnCurrentExecutionYear), 13);
		addCellValue(row, onNullEmptyString(regime), 14);
		addCellValue(
			row,
 "", // VALUE(IF(ISNA(VLOOKUP(O4,$Regime.A2:B6,2,FALSE()));\"Regime não tipificado\";VLOOKUP(O4,$Regime.A2:B6,2,FALSE())))",
			15);
		addCellValue(row, onNullEmptyString(firstRegistrationExecutionYear), 16);
		addCellValue(row, onNullEmptyString(countNumberOfEnrolmentsYearsSinceRegistrationStart), 17);
		addCellValue(row, onNullEmptyString(numberOfDegreeCurricularYears), 18);
		addCellValue(row, onNullEmptyString(curricularYearOneYearAgo), 19);
		addCellValue(row, onNullEmptyString(numberOfEnrolledEctsOneYearAgo), 20);
		addCellValue(row, onNullEmptyString(numberOfApprovedEctsOneYearAgo), 21);
		addCellValue(row, "", 22); // "=IF(V5/U5<50%,\"Não\",\"Sim\")",
					   // 22);
		addCellValue(row, onNullEmptyString(curricularYearInCurrentYear), 23);
		addCellValue(row, onNullEmptyString(numberOfEnrolledECTS), 24);
		addCellValue(
			row,
 "", // "IF(P3=4;\"Não Aplicável\";IF(P3=3;IF(S3<=3;IF((2*S3+2)>=R3+(S3-X3);\"SIM\";\"NÃO\");IF((2*S3+3)>=R3+(S3-X3);\"SIM\";\"NÃO\"));IF(OR(P3=2;L3>0);IF(S3<=3;IF((S3+2)>=R3+(S3-X3);\"SIM\";\"NÃO\");IF(S3>3;IF((S3+3)>=R3+(S3-X3);\"SIM\";\"NÃO\")));IF(AND(L3=0;P3=1);IF(S3<=3;IF(S3+1>=R3+(S3-X3);\"SIM\";\"NÃO\");IF(S3>3;IF(S3+2>=R3+(S3-X3);\"SIM\";\"NÃO\")))))))",
			25);
		addCellValue(
			row,
 "", // "IF(P3=4;IF(W3=\"Sim\";\"Deferido\";\"Indeferido\");IF(Y3>=30;IF(OR(W3=\"NÃO\";Z3=\"NÃO\";AI3=\"Sim\");\"Indeferido\";\"Deferido\");\"Indeferido\"))",
			26);
		addCellValue(row, onNullEmptyString(gratuityAmount), 27);
		addCellValue(row, onNullEmptyString(numberOfMonthsExecutionYear), 28);
		addCellValue(row, onNullEmptyString(firstMonthOfPayment), 29);
		addCellValue(row, onNullEmptyString(ownerOfCETQualification), 30);
		addCellValue(row, onNullEmptyString(degreeQualificationOwner), 31);
		addCellValue(row, onNullEmptyString(masterQualificationOwner), 32);
		addCellValue(row, onNullEmptyString(phdQualificationOwner), 33);
		addCellValue(row, onNullEmptyString(ownerOfCollegeQualification), 34);
		addCellValue(row, onNullEmptyString(observations), 35);
		addCellValue(row, onNullEmptyString(lastEnrolmentExecutionYear), 36);
		addCellValue(row, onNullEmptyString(nif), 37);
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
	addHeaderCell(sheet, getHeaderInBundle(bundle, "lastEnrolledExecutionYear"), 36);
	addHeaderCell(sheet, getHeaderInBundle(bundle, "nif"), 37);
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
