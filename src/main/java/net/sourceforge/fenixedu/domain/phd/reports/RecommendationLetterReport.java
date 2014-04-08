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
                    addCellValue(row, onNullEmptyString(letter == null ? "NO" : "YES"), 3);
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
    }

    private String getHeaderInBundle(String field) {
        return this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdIndividualProgramProcessesReport."
                + field);
    }

}
