package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.Money;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.joda.time.LocalDate;

public class ReportStudentsUTLCandidatesForFirstYear extends ReportStudentsUTLCandidates {

    public ReportStudentsUTLCandidatesForFirstYear(final ExecutionYear forExecutionYear, final HSSFSheet sheet) {
        super(forExecutionYear, sheet);
    }

    @Override
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
                String regime = studentLine.getRegime();
                Integer countNumberOfEnrolmentsYearsSinceRegistrationStart =
                        studentLine.getCountNumberOfEnrolmentsYearsSinceRegistrationStart();
                Integer numberOfDegreeCurricularYears = studentLine.getNumberOfDegreeCurricularYears();
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
                String nif = studentLine.getNif();
                LocalDate firstEnrolmentOnCurrentExecutionYear = studentLine.getFirstEnrolmentOnCurrentExecutionYear();

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
                addCellValue(row, onNullEmptyString(firstEnrolmentOnCurrentExecutionYear), 11);
                addCellValue(row,
                        onNullEmptyString(gratuityAmount != null ? gratuityAmount.toPlainString().replace('.', ',') : ""), 12);
                addCellValue(row, onNullEmptyString(numberOfMonthsExecutionYear), 13);
                addCellValue(row, onNullEmptyString(firstMonthOfPayment), 14);
                addCellValue(row, onNullEmptyString(ownerOfCETQualification), 15);
                addCellValue(row, onNullEmptyString(degreeQualificationOwner), 16);
                addCellValue(row, onNullEmptyString(masterQualificationOwner), 17);
                addCellValue(row, onNullEmptyString(phdQualificationOwner), 18);
                addCellValue(row, onNullEmptyString(ownerOfCollegeQualification), 19);
                addCellValue(row, onNullEmptyString(observations), 20);
                addCellValue(row, onNullEmptyString(regime), 21);
                addCellValue(row, onNullEmptyString(numberOfDegreeCurricularYears), 22);
                addCellValue(row, onNullEmptyString(countNumberOfEnrolmentsYearsSinceRegistrationStart), 23);
                addCellValue(row,
                        onNullEmptyString(numberOfEnrolledECTS != null ? numberOfEnrolledECTS.toString().replace('.', ',') : ""),
                        24);
                addCellValue(row, onNullEmptyString(nif), 25);
            } catch (Exception e) {
                e.printStackTrace();
            }

            i++;
        }
    }

    @Override
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
        addHeaderCell(sheet, getHeaderInBundle(bundle, "firstEnrolmentOnCurrentExecutionYear"), 11);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "gratuityAmount"), 12);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfMonthsExecutionYear"), 13);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "firstMonthOfPayment"), 14);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCETQualification"), 15);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "degreeQualificationOwner"), 16);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "masterQualificationOwner"), 17);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "phdQualificationOwner"), 18);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "ownerOfCollegeQualification"), 19);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "observations"), 20);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "regime"), 21);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfDegreeCurricularYears"), 22);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "ingression.year.on.cycle.studies.count"), 23);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "numberOfEnrolledECTS"), 24);
        addHeaderCell(sheet, getHeaderInBundle(bundle, "nif"), 25);

    }

}
