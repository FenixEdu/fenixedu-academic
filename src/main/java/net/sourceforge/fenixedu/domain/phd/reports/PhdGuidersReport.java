package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.ist.bennu.core.security.Authenticate;

public class PhdGuidersReport extends PhdReport {
    private final ResourceBundle bundle;
    private int rowCounter;

    public PhdGuidersReport(HSSFWorkbook workbook) {
        super(workbook);
        this.bundle = ResourceBundle.getBundle("resources.PhdResources");
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
        String guiderRole = this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdGuidersReport.guider");
        String assistantGuiderRole =
                this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdGuidersReport.assistantGuider");

        for (PhdParticipant phdParticipant : process.getGuidings()) {
            HSSFRow row = sheet.createRow(this.rowCounter++);
            fillParticipant(guiderRole, process, phdParticipant, row);
        }

        for (PhdParticipant phdParticipant : process.getAssistantGuidings()) {
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
            addCellValue(row, onNullEmptyString(department != null ? department.getName() : ""), 8);
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
        addHeaderCell(sheet, getHeaderInBundle("guiderNumber"), 6);
        addHeaderCell(sheet, getHeaderInBundle("guiderDepartmentCode"), 7);
        addHeaderCell(sheet, getHeaderInBundle("guiderDepartmentName"), 8);
    }

    private String getHeaderInBundle(String field) {
        return this.bundle.getString("label.net.sourceforge.fenixedu.domain.phd.reports.PhdGuidersReport." + field);
    }

}
