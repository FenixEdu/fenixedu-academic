package org.fenixedu.academic.domain.treasury;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;

import pt.ist.fenixframework.DomainObject;

public interface IAcademicTreasuryTarget extends DomainObject {
    
    // Required, must not return null
    public Person getAcademicTreasuryTargetPerson();
    public String getAcademicTreasuryTargetDescription();
    
    
    // Optional, may return null
    public Registration getAcademicTreasuryTargetRegistration();
    public ExecutionYear getAcademicTreasuryTargetExecutionYear();
    public ExecutionSemester getAcademicTreasuryTargetExecutionSemester();
    public Degree getAcademicTreasuryTargetDegree();
    
}
