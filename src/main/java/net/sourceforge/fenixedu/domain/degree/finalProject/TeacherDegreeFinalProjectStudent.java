package net.sourceforge.fenixedu.domain.degree.finalProject;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.student.Registration;

public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements ICreditsEventOriginator {

    public TeacherDegreeFinalProjectStudent() {
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherDegreeFinalProjectStudent(ExecutionSemester executionSemester, Teacher teacher, Registration registration) {
        this();
        setExecutionPeriod(executionSemester);
        setTeacher(teacher);
        setStudent(registration);
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setExecutionPeriod(null);
        setTeacher(null);
        setStudent(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

}
