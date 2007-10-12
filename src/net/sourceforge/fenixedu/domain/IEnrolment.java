package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public interface IEnrolment {

    static final Comparator<IEnrolment> COMPARATOR_BY_EXECUTION_PERIOD = new Comparator<IEnrolment>() {
        public int compare(IEnrolment o1, IEnrolment o2) {
	    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
        }
    };

    static final public Comparator<IEnrolment> COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME = new Comparator<IEnrolment>() {
        public int compare(IEnrolment o1, IEnrolment o2) {
	    int result = COMPARATOR_BY_EXECUTION_PERIOD.compare(o1, o2);
	    return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };

    static final Comparator<IEnrolment> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<IEnrolment>() {
        public int compare(IEnrolment o1, IEnrolment o2) {
            return o1.getExecutionYear().compareTo(o2.getExecutionYear());
        }
    };
    
    static final public Comparator<IEnrolment> COMPARATOR_BY_EXECUTION_YEAR_AND_NAME = new Comparator<IEnrolment>() {
        public int compare(IEnrolment o1, IEnrolment o2) {
	    int result = COMPARATOR_BY_EXECUTION_YEAR.compare(o1, o2);
	    return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };

    static final public Comparator<IEnrolment> COMPARATOR_BY_APPROVEMENT_DATE = new Comparator<IEnrolment>() {
        public int compare(IEnrolment o1, IEnrolment o2) {
	    if (o1.getApprovementDate() == null && o2.getApprovementDate() == null) {
		return 0;
	    }
	    if (o1.getApprovementDate() == null) {
		return -1;
	    }
	    if (o2.getApprovementDate() == null) {
		return 1;
	    }            
            return o1.getApprovementDate().compareTo(o2.getApprovementDate());
        }
    };

    Integer getIdInternal();
    
    MultiLanguageString getName();
    
    String getCode();
    
    Grade getGrade();
    
    String getGradeValue();
    
    Integer getFinalGrade();
    
    String getDescription();
    
    Double getEctsCredits();
    
    Double getWeigth();

    ExecutionPeriod getExecutionPeriod();
    
    ExecutionYear getExecutionYear();
    
    YearMonthDay getApprovementDate();

    Unit getAcademicUnit();
    
    boolean isApproved();

    boolean isEnroled();
    
    boolean isExternalEnrolment();

    boolean isEnrolment();

    /**
     * Obtains the last valid thesis for this enrolment. The returned thesis may
     * not be evaluated. You can used {@link Thesis#isFinalThesis()} and
     * {@link Thesis#isFinalAndApprovedThesis()} to distinguish between a thesis
     * currently in evaluation and a final thesis.
     * 
     * @return the last valid thesis for this enrolment
     */
    Thesis getThesis();

    void delete();
}
