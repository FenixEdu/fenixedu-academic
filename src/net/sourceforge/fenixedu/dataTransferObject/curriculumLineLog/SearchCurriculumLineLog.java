package net.sourceforge.fenixedu.dataTransferObject.curriculumLineLog;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class SearchCurriculumLineLog implements Serializable {

    private Integer studentNumber;
    private ExecutionSemester executionSemester;

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

}
