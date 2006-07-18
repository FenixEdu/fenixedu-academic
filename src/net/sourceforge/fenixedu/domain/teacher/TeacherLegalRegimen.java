/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.RegimenType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public static final Comparator TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE = new BeanComparator("beginDate");
    
    public TeacherLegalRegimen(Teacher teacher, Category category, Date beginDate, 
            Date endDate, Double totalHoursNumber, Integer lessonHoursNumber, 
            LegalRegimenType legalRegimenType, RegimenType regimenType, Integer percentage) {
		
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        checkParameters(teacher, beginDate, endDate);        		
        setTeacher(teacher);
        setCategory(category);
        setLegalRegimenType(legalRegimenType);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setTotalHours(totalHoursNumber);
        setLessonHours(lessonHoursNumber);            
        setPercentage(percentage);
        setRegimenType(regimenType);  
	}
    
	private void checkParameters(Teacher teacher, Date beginDate, Date endDate) {
        if(teacher == null) {
            throw new DomainException("error.teacherLegalRegimen.no.teacher");
        }
        if(beginDate == null) {
            throw new DomainException("error.teacherLegalRegimen.no.beginDate");
        }
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.teacherLegalRegimen.endDateBeforeBeginDate");
        }
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(endDate)
                && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(currentDate) &&
                (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(currentDate)));
    }
    
    public void delete(){
        removeCategory();
        removeTeacher();
        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public boolean isEndSituation() {
       return (this.getLegalRegimenType().equals(LegalRegimenType.DEATH)
                || this.getLegalRegimenType().equals(LegalRegimenType.EMERITUS)
                || this.getLegalRegimenType().equals(LegalRegimenType.RETIREMENT)
                || this.getLegalRegimenType().equals(LegalRegimenType.RETIREMENT_IN_PROGRESS)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
                || this.getLegalRegimenType().equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)                
                || this.getLegalRegimenType().equals(LegalRegimenType.CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.DENUNCIATION)
                || this.getLegalRegimenType().equals(LegalRegimenType.IST_OUT_NOMINATION)
                || this.getLegalRegimenType().equals(LegalRegimenType.SERVICE_TURN_OFF)
                || this.getLegalRegimenType().equals(LegalRegimenType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
                || this.getLegalRegimenType().equals(LegalRegimenType.EXONERATION)                
                || this.getLegalRegimenType().equals(LegalRegimenType.RESCISSION)
                || this.getLegalRegimenType().equals(LegalRegimenType.TRANSFERENCE));
     }               
}
