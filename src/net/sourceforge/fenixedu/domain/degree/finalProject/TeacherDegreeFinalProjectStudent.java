package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.student.Registration;

public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements
        ICreditsEventOriginator {
    
    public TeacherDegreeFinalProjectStudent() {
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherDegreeFinalProjectStudent(ExecutionSemester executionSemester, Teacher teacher,
            Registration registration) {
    	this();
        setExecutionPeriod(executionSemester);
        setTeacher(teacher);
        setStudent(registration);
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }
    
    public void delete() {
        setExecutionPeriod(null);
        setTeacher(null);
        setStudent(null);
        
        removeRootDomainObject();
        super.deleteDomainObject();
    }
}