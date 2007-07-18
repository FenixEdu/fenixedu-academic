package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
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

    Unit getAcademicUnit();
    
    boolean isApproved();

    boolean isEnroled();
    
    boolean isExternalEnrolment();

    boolean isEnrolment();
    
}
