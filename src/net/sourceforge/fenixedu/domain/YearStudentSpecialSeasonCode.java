package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Registration;

public class YearStudentSpecialSeasonCode extends YearStudentSpecialSeasonCode_Base {
	
	private YearStudentSpecialSeasonCode() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public  YearStudentSpecialSeasonCode(Registration registration, ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode) {
        this();
        setStudent(registration);
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
