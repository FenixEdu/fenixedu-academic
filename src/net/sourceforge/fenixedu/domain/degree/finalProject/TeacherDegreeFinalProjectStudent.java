package net.sourceforge.fenixedu.domain.degree.finalProject;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base implements
        ICreditsEventOriginator {
    
    public TeacherDegreeFinalProjectStudent() {}

    public TeacherDegreeFinalProjectStudent(IExecutionPeriod executionPeriod, ITeacher teacher,
            IStudent student) {
        setExecutionPeriod(executionPeriod);
        setTeacher(teacher);
        setStudent(student);
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }
    
    public void delete() {
        setExecutionPeriod(null);
        setTeacher(null);
        setStudent(null);
        
        super.deleteDomainObject();
    }
}