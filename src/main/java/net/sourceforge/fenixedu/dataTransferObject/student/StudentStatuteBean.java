/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.SeniorStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import java.util.Locale;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class StudentStatuteBean implements Serializable {

    private StudentStatuteType statuteType;

    private ExecutionSemester executionSemester;

    private StudentStatute studentStatute;

    public StudentStatuteBean(StudentStatuteType statuteType, ExecutionSemester executionSemester) {
        this.statuteType = statuteType;
        this.executionSemester = executionSemester;
    }

    public StudentStatuteBean(StudentStatuteType statuteType) {
        this.statuteType = statuteType;
    }

    public StudentStatuteBean(StudentStatute studentStatute, ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
        this.studentStatute = studentStatute;
    }

    public StudentStatuteBean(StudentStatute studentStatute) {
        this.studentStatute = studentStatute;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public StudentStatuteType getStatuteType() {
        return statuteType != null ? statuteType : getStudentStatute().getStatuteType();
    }

    public StudentStatute getStudentStatute() {
        return studentStatute;
    }

    public String getBeginPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().hasBeginExecutionPeriod() ? getStudentStatute()
                .getBeginExecutionPeriod().getQualifiedName() : " ... ";
    }

    public String getEndPeriodFormatted() {
        return getStudentStatute() != null && getStudentStatute().hasEndExecutionPeriod() ? getStudentStatute()
                .getEndExecutionPeriod().getQualifiedName() : " ... ";
    }

    public String getDescription() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale());
        return bundle.getString(getStatuteType().getDeclaringClass().getSimpleName() + "." + getStatuteType().name())
                + (studentStatute instanceof SeniorStatute ? (" ("
                        + ((SeniorStatute) studentStatute).getRegistration().getDegree().getPresentationName() + ") ") : "");
    }

}
