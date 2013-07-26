package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.security.Authenticate;

public class EPFLCandidatesReport extends PhdReport {

    private final ResourceBundle bundle;

    public EPFLCandidatesReport(HSSFWorkbook workbook) {
        super(workbook);
        this.bundle = ResourceBundle.getBundle("resources.PhdResources");
    }

    public HSSFSheet build(final SearchPhdIndividualProgramProcessBean bean) {
        List<PhdIndividualProgramProcess> processes =
                PhdIndividualProgramProcess.search(bean.getExecutionYear(), bean.getPredicates());

        if (!hasEPFLCandidates(processes)) {
            return null;
        }

        HSSFSheet sheet = workbook.createSheet("Candidaturas EPFL");

        setHeaders(sheet);

        setHeaders(sheet);

        int i = 2;
        for (PhdIndividualProgramProcess process : processes) {
            if (isProcessFromEPFL(process) && process.isAllowedToManageProcess(Authenticate.getUser())) {
                HSSFRow row = sheet.createRow(i);

                fillRow(process, row);
                i++;
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

        String externalPhdProgram =
                process.getExternalPhdProgram() != null ? process.getExternalPhdProgram().getName().getContent() : "";

        addCellValue(row, onNullEmptyString(processNumber), 0);
        addCellValue(row, onNullEmptyString(studentNumber), 1);
        addCellValue(row, onNullEmptyString(studentName), 2);
        addCellValue(row, onNullEmptyString(dateOfBirth), 3);
        addCellValue(row, onNullEmptyString(documentIdNumber), 4);
        addCellValue(row, onNullEmptyString(documentIdTypeName), 5);
        addCellValue(row, onNullEmptyString(focusArea), 6);
        addCellValue(row, onNullEmptyString(phdProgramName), 7);
        addCellValue(row, onNullEmptyString(externalPhdProgram), 8);

        int column = 9;
        Set<ThesisSubjectOrder> thesisSubjectOrdersSet = process.getThesisSubjectOrdersSet();

        for (ThesisSubjectOrder thesisSubjectOrder : thesisSubjectOrdersSet) {
            addCellValue(row, onNullEmptyString(thesisSubjectOrder.getThesisSubject().getName().getContent()), column++);
        }

    }

    @Override
    protected void setHeaders(final HSSFSheet sheet) {
        addHeaderCell(sheet, getHeaderInBundle("processNumber"), 0);
        addHeaderCell(sheet, getHeaderInBundle("studentNumber"), 1);
        addHeaderCell(sheet, getHeaderInBundle("studentName"), 2);
        addHeaderCell(sheet, getHeaderInBundle("dateOfBirth"), 3);
        addHeaderCell(sheet, getHeaderInBundle("identification"), 4);
        addHeaderCell(sheet, getHeaderInBundle("idDocumentType"), 5);
        addHeaderCell(sheet, getHeaderInBundle("focusArea"), 6);
        addHeaderCell(sheet, getHeaderInBundle("phdProgram"), 7);
        addHeaderCell(sheet, getHeaderInBundle("epfl.phdProgram"), 8);
        addHeaderCell(sheet, getHeaderInBundle("thesis.rank"), 9);
    }

    private String getHeaderInBundle(String field) {
        return this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdIndividualProgramProcessesReport."
                + field);
    }

}
