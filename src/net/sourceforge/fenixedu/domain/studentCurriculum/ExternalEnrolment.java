package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class ExternalEnrolment extends ExternalEnrolment_Base implements IEnrolment {
    
    public final static Comparator<ExternalEnrolment> COMPARATOR_BY_NAME = new Comparator<ExternalEnrolment>() {
	public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result != 0) ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };
    
    protected ExternalEnrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
	if(AccessControl.getPerson() != null){
	    setCreatedBy(AccessControl.getPerson().getUsername());
	}
    }
    
    public  ExternalEnrolment(Student student, ExternalCurricularCourse externalCurricularCourse, String grade, ExecutionPeriod executionPeriod) {
        this();
        if(externalCurricularCourse == null) {
            throw new DomainException("error.externalEnrolment.externalCurricularCourse.cannot.be.null");
        }
        if(student == null) {
            throw new DomainException("error.externalEnrolment.student.cannot.be.null");
        }
        setStudent(student);
        setExternalCurricularCourse(externalCurricularCourse);
        setGrade(grade);
        setExecutionPeriod(executionPeriod);
    }

    public MultiLanguageString getName() {
	MultiLanguageString multiLanguageString = new MultiLanguageString();
	multiLanguageString.setContent(getExternalCurricularCourse().getName());
	return multiLanguageString;
    }
    
    public String getFullPathName() {
	return getExternalCurricularCourse().getFullPathName();
    }
    
    public void delete() {
	removeExecutionPeriod();
	removeExternalCurricularCourse();
	removeRootDomainObject();
	removeStudent();
	super.deleteDomainObject();
    }

}
