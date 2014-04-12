package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.fenixedu.bennu.core.security.Authenticate;

public class RecommendationLetterReport extends PhdReport {

    private final ResourceBundle bundle;

    public RecommendationLetterReport(HSSFWorkbook workbook) {
        super(workbook);
        this.bundle = ResourceBundle.getBundle("resources.PhdResources");
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
        return this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdIndividualProgramProcessesReport."
                + field);
    }

    private String getResource(String key) {
        return this.bundle.getString(key);
    }

}
