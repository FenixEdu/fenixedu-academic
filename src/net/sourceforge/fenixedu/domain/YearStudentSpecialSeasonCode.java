package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Registration;

public class YearStudentSpecialSeasonCode extends YearStudentSpecialSeasonCode_Base {
	
	private YearStudentSpecialSeasonCode() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public  YearStudentSpecialSeasonCode(Registration student, ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode) {
        this();
        setStudent(student);
        setExecutionYear(executionYear);
        setSpecialSeasonCode(specialSeasonCode);
    }
    
    public void delete() {
    	setExecutionYear(null);
    	setStudent(null);
    	setSpecialSeasonCode(null);
    	super.deleteDomainObject();
    }
}
