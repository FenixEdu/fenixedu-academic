package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.MarkSheetType;

public class MarkSheetManagementSearchBean extends MarkSheetManagementBaseBean {

    private String teacherId;
    private Date evaluationDate;

    private MarkSheetState markSheetState;
    private MarkSheetType markSheetType;

    public MarkSheetType getMarkSheetType() {
        return markSheetType;
    }

    public void setMarkSheetType(MarkSheetType markSheetType) {
        this.markSheetType = markSheetType;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public MarkSheetState getMarkSheetState() {
        return markSheetState;
    }

    public void setMarkSheetState(MarkSheetState markSheetState) {
        this.markSheetState = markSheetState;
    }
}
