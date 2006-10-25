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
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public static final Comparator<TeacherLegalRegimen> TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator(
		"beginDateYearMonthDay"));
	((ComparatorChain) TEACHER_LEGAL_REGIMEN_COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator(
		"idInternal"));
    }
    
    public TeacherLegalRegimen(Teacher teacher, Category category, Date beginDate, 
            Date endDate, Double totalHoursNumber, Integer lessonHoursNumber, 
            LegalRegimenType legalRegimenType, RegimenType regimenType, Integer percentage) {
		
        super();        
        checkParameters(teacher, beginDate, endDate, legalRegimenType, category, totalHoursNumber, lessonHoursNumber);          
        setRootDomainObject(RootDomainObject.getInstance());
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
    
	private void checkParameters(Teacher teacher, Date beginDate, Date endDate, LegalRegimenType legalRegimenType,
            Category category, Double totalHoursNumber, Integer lessonHoursNumber) {
        
        if(teacher == null) {
            throw new DomainException("error.teacherLegalRegimen.no.teacher");
        }
        if(beginDate == null) {
            throw new DomainException("error.teacherLegalRegimen.no.beginDate");
        }
        if (endDate != null && endDate.before(beginDate)) {
            System.out.println("Teacher Number: " + teacher.getTeacherNumber());
            throw new DomainException("error.teacherLegalRegimen.endDateBeforeBeginDate");
        }
        if (legalRegimenType == null) {
            throw new DomainException("error.teacherLegalRegimen.no.legalRegimenType");
        }
        if(category == null) {
            throw new DomainException("error.teacherLegalRegimen.no.category");
        }        
//        if(!isEndLegalRegimenType(legalRegimenType)) {
//            checkTeacherLegalRegimenDatesIntersection(teacher, beginDate, endDate);
//        }
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(endDate)
                && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));
    }
    
    public boolean isActive(YearMonthDay currentDate) {
        return belongsToPeriod(currentDate, currentDate);
    }
    
    public void delete(){
        removeCategory();
        removeTeacher();
        removeRootDomainObject();
        super.deleteDomainObject();
    }      
    
//    private void checkTeacherLegalRegimenDatesIntersection(Teacher teacher, Date begin, Date end) {
//        for (TeacherLegalRegimen legalRegimen : teacher.getAllLegalRegimensWithoutEndSituations()) {
//            if(legalRegimen.checkDatesIntersections(begin, end)) {
//                System.out.println("Teacher Number: " + teacher.getTeacherNumber());
//                throw new DomainException("error.teacherLegalRegimen.dates.intersection");
//            }
//        }        
//    }
//    
//    private boolean checkDatesIntersections(Date begin, Date end) {
//        return ((end == null || this.getBeginDate().before(end))
//                && (this.getEndDate() == null || this.getEndDate().after(begin)));
//    } 
    
    public boolean isEndSituation() {
        return isEndLegalRegimenType(getLegalRegimenType());
    }
    
    private boolean isEndLegalRegimenType(LegalRegimenType legalRegimenType) {
        return (legalRegimenType.equals(LegalRegimenType.DEATH)
                || legalRegimenType.equals(LegalRegimenType.EMERITUS)
                || legalRegimenType.equals(LegalRegimenType.RETIREMENT)
                || legalRegimenType.equals(LegalRegimenType.RETIREMENT_IN_PROGRESS)
                || legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END)
                || legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
                || legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
                || legalRegimenType.equals(LegalRegimenType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)                
                || legalRegimenType.equals(LegalRegimenType.CONTRACT_END)
                || legalRegimenType.equals(LegalRegimenType.DENUNCIATION)
                || legalRegimenType.equals(LegalRegimenType.IST_OUT_NOMINATION)
                || legalRegimenType.equals(LegalRegimenType.SERVICE_TURN_OFF)
                || legalRegimenType.equals(LegalRegimenType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
                || legalRegimenType.equals(LegalRegimenType.EXONERATION)                
                || legalRegimenType.equals(LegalRegimenType.RESCISSION)
                || legalRegimenType.equals(LegalRegimenType.TRANSFERENCE));
    }
}
