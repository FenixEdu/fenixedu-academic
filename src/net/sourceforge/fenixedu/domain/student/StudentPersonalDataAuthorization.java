package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

public class StudentPersonalDataAuthorization extends StudentPersonalDataAuthorization_Base {
    
    public StudentPersonalDataAuthorization() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public StudentPersonalDataAuthorization(Student student, ExecutionYear executionYear, 
            StudentPersonalDataAuthorizationChoice studentPersonalDataAuthorizationChoice) {
        this();
        
        setStudent(student);
        setExecutionYear(executionYear);
        setAnswer(studentPersonalDataAuthorizationChoice);
    }
}
