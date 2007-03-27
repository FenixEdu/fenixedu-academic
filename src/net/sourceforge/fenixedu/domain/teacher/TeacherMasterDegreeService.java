package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TeacherMasterDegreeService extends TeacherMasterDegreeService_Base {

    public TeacherMasterDegreeService(TeacherService teacherService, Professorship professorship) {
	
	super();
	if (teacherService == null || professorship == null) {
	    throw new DomainException("arguments can't be null");
	}
	if (!professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly()) {
	    throw new DomainException("message.invalid.executionCourse");
	}	
	
	setTeacherService(teacherService);
	setProfessorship(professorship);	
    }

    public void updateValues(Double hours, Double credits) {	
	if ((hours == null && credits == null) || ((hours == null || hours == 0) && (credits == null || credits == 0))) {
	    delete();
	} else {
	    setCredits(credits);
	    setHours(hours);
	}
    }

    public void delete() {
	removeProfessorship();
	removeTeacherService();
	super.delete();
    }

    @Override
    public Double getCredits() {
	Double credits = super.getCredits();
	return credits != null ? round(credits) : 0.0;
    }

    private Double round(double n) {
	return Math.round((n * 100.0)) / 100.0;
    }

    @Override
    public void setCredits(Double credits) {
	if (credits != null && credits < 0) {
	    throw new DomainException("error.invalid.credits");
	}
	super.setCredits(credits);
    }

    @Override
    public void setHours(Double hours) {
	if (hours != null && hours < 0) {
	    throw new DomainException("error.invalid.hours");
	}
	super.setHours(hours);
    }
}
