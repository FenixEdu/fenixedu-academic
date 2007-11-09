package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.curriculum.SimpleStudentCurriculum;

public class StudentCurriculum extends SimpleStudentCurriculum {

    public StudentCurriculum(final Registration registration, final ExecutionYear executionYear) {
	super(registration, executionYear);
    }

}
