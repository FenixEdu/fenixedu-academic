package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.YearMonthDay;

public interface IEnrolment extends ICurriculumEntry {

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
    
    Integer getFinalGrade();
    
    String getDescription();
    
    Double getEctsCredits();
    
    Double getWeigth();    
    
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
