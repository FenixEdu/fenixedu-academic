/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class PhdIndividualProgramProcessesReport extends PhdReport {

    public PhdIndividualProgramProcessesReport(HSSFWorkbook workbook) {
        super(workbook);
    }

    public HSSFSheet build(final SearchPhdIndividualProgramProcessBean bean) {
        HSSFSheet sheet = workbook.createSheet("Processos de doutoramento");

        setHeaders(sheet);
        List<PhdIndividualProgramProcess> processes =
                PhdIndividualProgramProcess.search(bean.getExecutionYear(), bean.getPredicates());

        setHeaders(sheet);

        int i = 1;
        for (PhdIndividualProgramProcess process : processes) {
            if (!process.isAllowedToManageProcess(Authenticate.getUser())) {
                continue;
            }

            HSSFRow row = sheet.createRow(i);

            fillRow(process, row);
            i++;
        }

        return sheet;
    }

    private void fillRow(PhdIndividualProgramProcess process, HSSFRow row) {
        String processNumber = process.getProcessNumber();
        String studentNumber = process.getStudent() != null ? process.getStudent().getNumber().toString() : "";
        String studentName = process.getPerson().getName();
        YearMonthDay dateOfBirth = process.getPerson().getDateOfBirthYearMonthDay();
        String documentIdNumber = process.getPerson().getDocumentIdNumber();
        String documentIdTypeName = process.getPerson().getIdDocumentType().getLocalizedName();
        String phdProgramName = process.getPhdProgram() != null ? process.getPhdProgram().getName().getContent() : "";
        String focusArea =
                process.getPhdProgramFocusArea() != null ? process.getPhdProgramFocusArea().getName().getContent() : "";

        String activeStateName = process.getActiveState().getLocalizedName();
        LocalDate whenStartStudies = process.getWhenStartedStudies();
        DateTime stateDate =
                process.getMostRecentState().getStateDate() != null ? process.getMostRecentState().getStateDate() : process
                        .getMostRecentState().getWhenCreated();
        Boolean migratedProcess = process.getPhdConfigurationIndividualProgramProcess().isMigratedProcess();

        addCellValue(row, onNullEmptyString(processNumber), 0);
        addCellValue(row, onNullEmptyString(studentNumber), 1);
        addCellValue(row, onNullEmptyString(studentName), 2);
        addCellValue(row, onNullEmptyString(dateOfBirth), 3);
        addCellValue(row, onNullEmptyString(documentIdNumber), 4);
        addCellValue(row, onNullEmptyString(documentIdTypeName), 5);
        addCellValue(row, onNullEmptyString(phdProgramName), 6);
        addCellValue(row, onNullEmptyString(focusArea), 7);
        addCellValue(row, onNullEmptyString(whenStartStudies), 8);
        addCellValue(row, onNullEmptyString(activeStateName), 9);
        addCellValue(row, onNullEmptyString(stateDate), 10);
        addCellValue(row, onNullEmptyString(migratedProcess), 11);
    }

    @Override
    protected void setHeaders(final HSSFSheet sheet) {
        addHeaderCell(sheet, getHeaderInBundle("processNumber"), 0);
        addHeaderCell(sheet, getHeaderInBundle("studentNumber"), 1);
        addHeaderCell(sheet, getHeaderInBundle("studentName"), 2);
        addHeaderCell(sheet, getHeaderInBundle("dateOfBirth"), 3);
        addHeaderCell(sheet, getHeaderInBundle("identification"), 4);
        addHeaderCell(sheet, getHeaderInBundle("idDocumentType"), 5);
        addHeaderCell(sheet, getHeaderInBundle("phdProgram"), 6);
        addHeaderCell(sheet, getHeaderInBundle("focusArea"), 7);
        addHeaderCell(sheet, getHeaderInBundle("whenStartStudies"), 8);
        addHeaderCell(sheet, getHeaderInBundle("currentState"), 9);
        addHeaderCell(sheet, getHeaderInBundle("stateDate"), 10);
        addHeaderCell(sheet, getHeaderInBundle("migrated"), 11);
    }

    private String getHeaderInBundle(String field) {
        return BundleUtil.getString(Bundle.PHD,
                "label.net.sourceforge.fenixedu.domain.phd.reports.PhdIndividualProgramProcessesReport." + field);
    }
}
