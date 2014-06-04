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

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

public class RecommendationLetterReport extends PhdReport {

    public RecommendationLetterReport(HSSFWorkbook workbook) {
        super(workbook);
    }

    public HSSFSheet build(final SearchPhdIndividualProgramProcessBean bean) {
        List<PhdIndividualProgramProcess> processes =
                PhdIndividualProgramProcess.search(bean.getExecutionYear(), bean.getPredicates());

        if (!hasEPFLCandidates(processes)) {
            return null;
        }

        HSSFSheet sheet = workbook.createSheet("Recommendations");

        setHeaders(sheet);

        int i = 2;
        for (final PhdIndividualProgramProcess process : processes) {
            if (isProcessFromEPFL(process) && process.isAllowedToManageProcess(Authenticate.getUser())) {
                final String processNumber = process.getProcessNumber();
                
                for (final PhdCandidacyReferee referee : process.getPhdCandidacyReferees()) {
                    final HSSFRow row = sheet.createRow(i++);                    

                    final String email = referee.getEmail();
                    final String name = referee.getName();
                    final PhdCandidacyRefereeLetter letter = referee.getLetter();

                    addCellValue(row, onNullEmptyString(processNumber), 0);
                    addCellValue(row, onNullEmptyString(name), 1);
                    addCellValue(row, onNullEmptyString(email), 2);
                    if (letter == null) {
                        addCellValue(row, "NO", 3);
                    } else {
                        addCellValue(row, "YES", 3);
                        addCellValue(row, onNullEmptyString(letter.getHowLongKnownApplicant()), 4);
                        addCellValue(row, onNullEmptyString(letter.getCapacity()), 5);
                        addCellValue(row, onNullEmptyString(letter.getComparisonGroup()), 6);
                        addCellValue(row, onNullEmptyString(letter.getRankInClass()), 7);

                        addCellValue(row, onNullEmptyString(letter.getAcademicPerformance().getLocalizedName()), 8);
                        addCellValue(row, onNullEmptyString(letter.getSocialAndCommunicationSkills()), 9);
                        addCellValue(row, onNullEmptyString(letter.getPotencialToExcelPhd()), 10);

                        addCellValue(row, onNullEmptyString(letter.getRefereeName()), 11);
                        addCellValue(row, onNullEmptyString(letter.getRefereePosition()), 12);
                        addCellValue(row, onNullEmptyString(letter.getRefereeInstitution()), 13);
                        addCellValue(row, onNullEmptyString(letter.getRefereeAddress()), 14);
                        addCellValue(row, onNullEmptyString(letter.getRefereeCity()), 15);
                        addCellValue(row, onNullEmptyString(letter.getRefereeZipCode()), 16);
                        addCellValue(row, onNullEmptyString(letter.getRefereeCountry().getLocalizedName().getContent()), 17);
                        addCellValue(row, onNullEmptyString(letter.getRefereeEmail()), 18);
                    }
                }
            }

        }

        return sheet;
    }

    private boolean hasEPFLCandidates(List<PhdIndividualProgramProcess> processes) {
        for (PhdIndividualProgramProcess process : processes) {
            if (isProcessFromEPFL(process)) {
                return true;
            }
        }

        return false;
    }

    private boolean isProcessFromEPFL(PhdIndividualProgramProcess process) {
        return (process.getCandidacyProcess().getPublicPhdCandidacyPeriod() != null && process.getCandidacyProcess()
                .getPublicPhdCandidacyPeriod().isEpflCandidacyPeriod())
                || PhdIndividualProgramCollaborationType.EPFL == process.getCollaborationType();
    }

    @Override
    protected void setHeaders(final HSSFSheet sheet) {
        addHeaderCell(sheet, getHeaderInBundle("processNumber"), 0);
        addHeaderCell(sheet, getHeaderInBundle("refererName"), 1);
        addHeaderCell(sheet, getHeaderInBundle("refererEmaol"), 2);
        addHeaderCell(sheet, getHeaderInBundle("hasReferenceLetter"), 3);

        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.howLongKnownApplicant"), 4);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.capacity"), 5);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.comparisonGroup"), 6);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.rankInClass"), 7);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.academicPerformance"), 8);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.socialAndCommunicationSkills"), 9);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.potencialToExcelPhd"), 10);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeName"), 11);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereePosition"), 12);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeInstitution"), 13);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeAddress"), 14);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeCity"), 15);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeZipCode"), 16);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.refereeCountry"), 17);
        addHeaderCell(sheet, getResource("label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter.email"), 18);
    }

    private String getHeaderInBundle(String field) {
        return BundleUtil.getString(Bundle.PHD, "label.net.sourceforge.fenixedu.domain.phd.reports.PhdIndividualProgramProcessesReport."
                + field);
    }

    private String getResource(String key) {
        return BundleUtil.getString(Bundle.PHD, key);
    }

}
