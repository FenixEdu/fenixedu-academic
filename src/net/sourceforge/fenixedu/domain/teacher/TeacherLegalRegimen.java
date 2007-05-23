/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.RegimenType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public static final Comparator<TeacherLegalRegimen> TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("beginDateYearMonthDay"));
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public TeacherLegalRegimen(Teacher teacher, Category category, YearMonthDay beginDate,
	    YearMonthDay endDate, Double totalHoursNumber, Integer lessonHoursNumber,
	    LegalRegimenType legalRegimenType, RegimenType regimenType, Integer percentage) {

	super();	
	super.init(beginDate, endDate, legalRegimenType, regimenType);
	setTeacher(teacher);
	setCategory(category);
	setTotalHours(totalHoursNumber);
	setLessonHours(lessonHoursNumber);
	setPercentage(percentage);	
    }   

    @Override
    public void delete() {
	super.setCategory(null);
	super.setTeacher(null);
	super.delete();
    }

    @Override
    public void setLessonHours(Integer lessonHours) {
	if(lessonHours == null && !isEndSituation()) {
	    throw new DomainException("error.LegalRegimen.no.lesson.hours");
	}
	super.setLessonHours(lessonHours);
    }
    
    @Override
    public void setTotalHours(Double totalHours) {
	if(totalHours == null && !isEndSituation()) {
	    throw new DomainException("error.LegalRegimen.no.total.hours");
	}
	super.setTotalHours(totalHours);
    }
    
    @Override
    public void setRegimenType(RegimenType regimenType) {
	if (regimenType == null && !isEndSituation()) {
	    throw new DomainException("error.LegalRegimen.no.regimenType");
	}
	super.setRegimenType(regimenType);
    }
    
    @Override
    public void setCategory(Category category) {
	if (category == null && !isEndSituation()) {
	    throw new DomainException("error.teacherLegalRegimen.no.category");
	}
	super.setCategory(category);
    }

    @Override
    public void setTeacher(Teacher teacher) {
	if (teacher == null) {
	    throw new DomainException("error.teacherLegalRegimen.no.teacher");
	}
	super.setTeacher(teacher);
    }       
    
    public boolean isEndSituation() {
	return isEndLegalRegimenType(getLegalRegimenType());
    }
    
    public boolean isFunctionAccumulation() {
	return isFunctionsAccumulation(getLegalRegimenType());
    }
    
    public boolean isFunctionsAccumulation(LegalRegimenType legalRegimenType) {
	return legalRegimenType.equals(LegalRegimenType.FUNCTIONS_ACCUMULATION_WITH_LEADING_POSITIONS);
    }
    
    public static boolean isEndLegalRegimenType(LegalRegimenType legalRegimenType) {
	return (legalRegimenType.equals(LegalRegimenType.DEATH)
		|| legalRegimenType.equals(LegalRegimenType.TERM_WORK_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.EMERITUS)
		|| legalRegimenType.equals(LegalRegimenType.RETIREMENT)
		|| legalRegimenType.equals(LegalRegimenType.RETIREMENT_IN_PROGRESS)		
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
		|| legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)
		|| legalRegimenType.equals(LegalRegimenType.CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.DENUNCIATION)
		|| legalRegimenType.equals(LegalRegimenType.RESIGNATION)
		|| legalRegimenType.equals(LegalRegimenType.IST_OUT_NOMINATION)
		|| legalRegimenType.equals(LegalRegimenType.SERVICE_TURN_OFF)
		|| legalRegimenType.equals(LegalRegimenType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
		|| legalRegimenType.equals(LegalRegimenType.EXONERATION)
		|| legalRegimenType.equals(LegalRegimenType.RESCISSION) 
		|| legalRegimenType.equals(LegalRegimenType.TRANSFERENCE)
		|| legalRegimenType.equals(LegalRegimenType.REFUSED_DEFINITIVE_NOMINATION));
    }   
}
