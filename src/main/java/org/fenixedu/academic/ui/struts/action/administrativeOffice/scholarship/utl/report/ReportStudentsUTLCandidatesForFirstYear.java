/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.scholarship.utl.report;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.fenixedu.academic.domain.ExecutionYear;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportStudentsUTLCandidatesForFirstYear extends ReportStudentsUTLCandidates {

    private static final Logger logger = LoggerFactory.getLogger(ReportStudentsUTLCandidatesForFirstYear.class);

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
                logger.error(e.getMessage(), e);
            }

            i++;
        }
    }

    @Override
    protected void addHeaders(HSSFSheet sheet) {
        sheet.createRow(0);
        sheet.createRow(1);

        addHeaderCell(sheet, getHeaderInBundle("institutionCode"), 0);
        addHeaderCell(sheet, getHeaderInBundle("institutionName"), 1);
        addHeaderCell(sheet, getHeaderInBundle("candidacyNumber"), 2);
        addHeaderCell(sheet, getHeaderInBundle("studentNumberForPrint"), 3);
        addHeaderCell(sheet, getHeaderInBundle("studentName"), 4);
        addHeaderCell(sheet, getHeaderInBundle("documentTypeName"), 5);
        addHeaderCell(sheet, getHeaderInBundle("documentNumber"), 6);
        addHeaderCell(sheet, getHeaderInBundle("degreeCode"), 7);
        addHeaderCell(sheet, getHeaderInBundle("degreeName"), 8);
        addHeaderCell(sheet, getHeaderInBundle("degreeTypeName"), 9);
        addHeaderCell(sheet, getHeaderInBundle("code"), 10);
        addHeaderCell(sheet, getHeaderInBundle("firstEnrolmentOnCurrentExecutionYear"), 11);
        addHeaderCell(sheet, getHeaderInBundle("gratuityAmount"), 12);
        addHeaderCell(sheet, getHeaderInBundle("numberOfMonthsExecutionYear"), 13);
        addHeaderCell(sheet, getHeaderInBundle("firstMonthOfPayment"), 14);
        addHeaderCell(sheet, getHeaderInBundle("ownerOfCETQualification"), 15);
        addHeaderCell(sheet, getHeaderInBundle("degreeQualificationOwner"), 16);
        addHeaderCell(sheet, getHeaderInBundle("masterQualificationOwner"), 17);
        addHeaderCell(sheet, getHeaderInBundle("phdQualificationOwner"), 18);
        addHeaderCell(sheet, getHeaderInBundle("ownerOfCollegeQualification"), 19);
        addHeaderCell(sheet, getHeaderInBundle("observations"), 20);
        addHeaderCell(sheet, getHeaderInBundle("regime"), 21);
        addHeaderCell(sheet, getHeaderInBundle("numberOfDegreeCurricularYears"), 22);
        addHeaderCell(sheet, getHeaderInBundle("ingression.year.on.cycle.studies.count"), 23);
        addHeaderCell(sheet, getHeaderInBundle("numberOfEnrolledECTS"), 24);
        addHeaderCell(sheet, getHeaderInBundle("nif"), 25);

    }

}
