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

    protected ExecutionYear forExecutionYear;
    protected List<StudentLine> correctStudentLines;
    protected List<StudentLine> erroneousStudentLines;
    protected CellStyle headerStyle;

    public ReportStudentsUTLCandidates(final ExecutionYear forExecutionYear) {
        this.forExecutionYear = forExecutionYear;
        this.correctStudentLines = new ArrayList<StudentLine>();
        this.erroneousStudentLines = new ArrayList<StudentLine>();
    }

    public ReportStudentsUTLCandidates(final ExecutionYear forExecutionYear, final HSSFSheet sheet) {
        this(forExecutionYear);

        getStudentLines(sheet);
    }

    protected void getStudentLines(HSSFSheet sheet) {
        int i = 2;
        HSSFRow row;
        while ((row = sheet.getRow(i)) != null) {
            StudentLine studentLine = new StudentLine();
            boolean filledWithSuccess = studentLine.fillWithSpreadsheetRow(forExecutionYear, row);

            try {
                testIt(studentLine);
            } catch (Exception e) {
                filledWithSuccess = false;
            }

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

    public HSSFWorkbook generateErrors() {
        HSSFWorkbook wb = new HSSFWorkbook();

        headerStyle = headerBackgroundStyle(wb);

        HSSFSheet sheet = wb.createSheet("Errors");

        addHeadersForErrors(sheet);

        addValuesForErrors(sheet);

        return wb;
    }

    private void addValuesForErrors(HSSFSheet sheet) {
        int i = 2;
        for (StudentLine studentLine : getErroneousStudentLines()) {

            try {
                String institutionCode = studentLine.getInstitutionCode();
                String institutionName = studentLine.getInstitutionName();
                String candidacyNumber = studentLine.getCandidacyNumber();
                String studentNumberForPrint = studentLine.getStudentNumberForPrint();
                String studentName = studentLine.getStudentName();
                String documentTypeName = studentLine.getDocumentTypeName();
                String documentNumber = studentLine.getDocumentNumber();

                HSSFRow row = sheet.createRow(i);
                addCellValue(row, onNullEmptyString(institutionCode), 0);
                addCellValue(row, onNullEmptyString(institutionName), 1);
                addCellValue(row, onNullEmptyString(candidacyNumber), 2);
                addCellValue(row, onNullEmptyString(studentNumberForPrint), 3);
                addCellValue(row, onNullEmptyString(studentName), 4);
                addCellValue(row, onNullEmptyString(documentTypeName), 5);
                addCellValue(row, onNullEmptyString(documentNumber), 6);
            } catch (Exception e) {
                e.printStackTrace();
            }

            i++;
        }
    }

    private void addHeadersForErrors(HSSFSheet sheet) {
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
    }

    private void fillRegimeTable(HSSFWorkbook wb) {
        HSSFSheet sheet = wb.createSheet("Regime");

        for (int i = 0; i <= 7; i++) {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(false);
            row.createCell(1).setCellValue(false);
        }
    }

    protected void testIt(final StudentLine studentLine) {
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
        Integer countNumberOfEnrolmentsYearsSinceRegistrationStart =
                studentLine.getCountNumberOfEnrolmentsYearsSinceRegistrationStart();
        Integer countNumberOfEnrolmentsYearsInIntegralRegime = studentLine.getCountNumberOfEnrolmentsYearsInIntegralRegime();
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

    }

    protected void addValues(HSSFSheet sheet) {
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
                Integer countNumberOfEnrolmentsYearsSinceRegistrationStart =
                        studentLine.getCountNumberOfEnrolmentsYearsSinceRegistrationStart();
                Integer countNumberOfEnrolmentsYearsInIntegralRegime =
                        studentLine.getCountNumberOfEnrolmentsYearsInIntegralRegime();
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
                addCellValue(row, "", 15);
                addCellValue(row, onNullEmptyString(firstRegistrationExecutionYear), 16);
                addCellValue(row, onNullEmptyString(countNumberOfEnrolmentsYearsSinceRegistrationStart), 17);
                addCellValue(row, onNullEmptyString(countNumberOfEnrolmentsYearsInIntegralRegime), 18);
                addCellValue(row, onNullEmptyString(numberOfDegreeCurricularYears), 19);
                addCellValue(row, onNullEmptyString(curricularYearOneYearAgo), 20);
                addCellValue(row, onNullEmptyString(numberOfEnrolledEctsOneYearAgo != null ? numberOfEnrolledEctsOneYearAgo
                        .toString().replace('.', ',') : ""), 21);
                addCellValue(row, onNullEmptyString(numberOfApprovedEctsOneYearAgo != null ? numberOfApprovedEctsOneYearAgo
                        .toString().replace('.', ',') : ""), 22);
                addCellValue(row, onNullEmptyString(curricularYearInCurrentYear), 23);
                addCellValue(row,
                        onNullEmptyString(numberOfEnrolledECTS != null ? numberOfEnrolledECTS.toString().replace('.', ',') : ""),
                        24);
                addCellValue(row,
                        onNullEmptyString(gratuityAmount != null ? gratuityAmount.toPlainString().replace('.', ',') : ""), 25);
                addCellValue(row, onNullEmptyString(numberOfMonthsExecutionYear), 26);
                addCellValue(row, onNullEmptyString(firstMonthOfPayment), 27);
                addCellValue(row, onNullEmptyString(ownerOfCETQualification), 28);
                addCellValue(row, onNullEmptyString(degreeQualificationOwner), 29);
                addCellValue(row, onNullEmptyString(masterQualificationOwner), 30);
                addCellValue(row, onNullEmptyString(phdQualificationOwner), 31);
                addCellValue(row, onNullEmptyString(ownerOfCollegeQualification), 32);
                addCellValue(row, onNullEmptyString(observations), 33);
                addCellValue(row, onNullEmptyString(lastEnrolmentExecutionYear), 34);
                addCellValue(row, onNullEmptyString(nif), 35);
                addCellValue(row, "", 36);

                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String onNullEmptyString(Object value) {

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

    protected void addHeaders(HSSFSheet sheet) {
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
        addHeaderCell(sheet, getHeaderInBundle(bundle, "code"), 10);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "countNumberOfDegreeChanges"), 11);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "hasMadeDegreeChange"), 12);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "firstEnrolmentOnCurrentExecutionYear"), 13);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "regime"), 14);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "code"), 15);

        HSSFRow row = sheet.getRow(0);
        HSSFCell cell = row.createCell(16);
        cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies"));
        cell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 18));

        cell = sheet.getRow(1).createCell(16);
        cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.year"));
        cell.setCellStyle(headerStyle);

        cell = sheet.getRow(1).createCell(17);
        cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.count"));
        cell.setCellStyle(headerStyle);

        cell = sheet.getRow(1).createCell(18);
        cell.setCellValue(getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.integral.count"));
        cell.setCellStyle(headerStyle);

        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfDegreeCurricularYears"), 19);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "curricularYearOneYearAgo"), 20);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfEnrolledEctsOneYearAgo"), 21);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfApprovedEctsOneYearAgo"), 22);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "curricularYearInCurrentYear"), 23);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfEnrolledECTS"), 24);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "gratuityAmount"), 25);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfMonthsExecutionYear"), 26);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "firstMonthOfPayment"), 27);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCETQualification"), 28);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeQualificationOwner"), 29);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "masterQualificationOwner"), 30);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "phdQualificationOwner"), 31);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCollegeQualification"), 32);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "observations"), 33);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "lastEnrolledExecutionYear"), 34);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "nif"), 35);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "last.conclusion.academic.facts"), 36);
    }

    protected String getHeaderInBundle(ResourceBundle bundle, String field) {
        return bundle
                .getString("label.net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.StudentLine."
                        + field);
    }

    protected void addHeaderCell(HSSFSheet sheet, String value, int columnNumber) {
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

    protected void addCellValue(HSSFRow row, String value, int cellNumber) {
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
