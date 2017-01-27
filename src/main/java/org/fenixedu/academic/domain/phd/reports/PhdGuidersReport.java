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
package org.fenixedu.academic.domain.phd.reports;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.SearchPhdIndividualProgramProcessBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

public class PhdGuidersReport extends PhdReport {
    private int rowCounter;

    public PhdGuidersReport(HSSFWorkbook workbook) {
        super(workbook);
        this.rowCounter = 2;
    }

    public HSSFSheet build(final SearchPhdIndividualProgramProcessBean bean) {
        HSSFSheet sheet = workbook.createSheet("Orientadores");

        setHeaders(sheet);
        List<PhdIndividualProgramProcess> processes =
                PhdIndividualProgramProcess.search(bean.getExecutionYear(), bean.getPredicates());

        setHeaders(sheet);

        for (PhdIndividualProgramProcess process : processes) {
            if (process.isAllowedToManageProcess(Authenticate.getUser())) {
                fillProcess(process, sheet);
            }
        }

        return sheet;
    }

    private void fillProcess(PhdIndividualProgramProcess process, HSSFSheet sheet) {
        String guiderRole =
                BundleUtil.getString(Bundle.PHD, "label.org.fenixedu.academic.domain.phd.reports.PhdGuidersReport.guider");
        String assistantGuiderRole =
                BundleUtil.getString(Bundle.PHD,
                        "label.org.fenixedu.academic.domain.phd.reports.PhdGuidersReport.assistantGuider");

        for (PhdParticipant phdParticipant : process.getGuidingsSet()) {
            HSSFRow row = sheet.createRow(this.rowCounter++);
            fillParticipant(guiderRole, process, phdParticipant, row);
        }

        for (PhdParticipant phdParticipant : process.getAssistantGuidingsSet()) {
            HSSFRow row = sheet.createRow(this.rowCounter++);
            fillParticipant(assistantGuiderRole, process, phdParticipant, row);
        }
    }

    private void fillParticipant(String guiderRole, final PhdIndividualProgramProcess process, PhdParticipant phdParticipant,
            HSSFRow row) {
        String processNumber = process.getProcessNumber();
        String studentNumber = process.getStudent() != null ? process.getStudent().getNumber().toString() : "";
        String studentName = process.getPerson().getName();

        String participantName = phdParticipant.getName();
        String institution = phdParticipant.getWorkLocation();

        addCellValue(row, onNullEmptyString(processNumber), 0);
        addCellValue(row, onNullEmptyString(studentNumber), 1);
        addCellValue(row, onNullEmptyString(studentName), 2);
        addCellValue(row, onNullEmptyString(participantName), 3);
        addCellValue(row, onNullEmptyString(guiderRole), 4);
        addCellValue(row, onNullEmptyString(institution), 5);

        if (!phdParticipant.isTeacher()) {
            addCellValue(row, onNullEmptyString(null), 6);
            addCellValue(row, onNullEmptyString(null), 7);
            addCellValue(row, onNullEmptyString(null), 8);
        } else {
            InternalPhdParticipant internalPhdParticipant = (InternalPhdParticipant) phdParticipant;
            Teacher teacher = internalPhdParticipant.getTeacher();

            addCellValue(row, onNullEmptyString(teacher.getTeacherId()), 6);
            Department department = internalPhdParticipant.getDepartment();

            addCellValue(row, onNullEmptyString(department != null ? department.getCode() : ""), 7);
            addCellValue(row, onNullEmptyString(department != null ? department.getName().getContent() : ""), 8);
        }
    }

    @Override
    protected void setHeaders(HSSFSheet sheet) {
        addHeaderCell(sheet, getHeaderInBundle("processNumber"), 0);
        addHeaderCell(sheet, getHeaderInBundle("studentNumber"), 1);
        addHeaderCell(sheet, getHeaderInBundle("studentName"), 2);
        addHeaderCell(sheet, getHeaderInBundle("guiderName"), 3);
        addHeaderCell(sheet, getHeaderInBundle("guiderRole"), 4);
        addHeaderCell(sheet, getHeaderInBundle("guiderInstitution"), 5);
        addHeaderCell(sheet, getHeaderInBundle("guiderId"), 6);
        addHeaderCell(sheet, getHeaderInBundle("guiderDepartmentCode"), 7);
        addHeaderCell(sheet, getHeaderInBundle("guiderDepartmentName"), 8);
    }

    private String getHeaderInBundle(String field) {
        return BundleUtil.getString(Bundle.PHD, "label.org.fenixedu.academic.domain.phd.reports.PhdGuidersReport." + field);
    }

}
