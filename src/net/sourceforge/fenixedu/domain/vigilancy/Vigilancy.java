package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class Vigilancy extends Vigilancy_Base {
	
	public static final Comparator<Vigilancy> COMPARATOR_BY_WRITTEN_EVALUATION_BEGGINING = new BeanComparator(
    "writtenEvaluation.beginning");
	
    public Vigilancy() {
    	  super();
          setRootDomainObject(RootDomainObject.getInstance());
          setOjbConcreteClass(getClass().getName());
    }
    
    public Vigilancy(WrittenEvaluation evaluation) {
    	this();
    	this.setWrittenEvaluation(evaluation);
    }
    
    public int getPoints() {
    	return 0;
    }
 
    public ExecutionYear getExecutionYear() {
        return this.getVigilant().getExecutionYear();
    }
    public long getBeginDate() {
        return this.getBeginDateTime().getMillis();
    }

    public long getEndDate() {
        return this.getEndDateTime().getMillis();
    }

    public DateTime getBeginDateTime() {
        return this.getWrittenEvaluation().getBeginningDateTime();
    }

    public DateTime getEndDateTime() {
        return this.getWrittenEvaluation().getEndDateTime();
    }

    public YearMonthDay getBeginYearMonthDay() {
        DateTime begin = this.getWrittenEvaluation().getBeginningDateTime();
        YearMonthDay date = new YearMonthDay(begin.getYear(), begin.getMonthOfYear(), begin
                .getDayOfMonth());
        return date;
    }

    public YearMonthDay getEndYearMonthDay() {
        DateTime end = this.getWrittenEvaluation().getEndDateTime();
        YearMonthDay date = new YearMonthDay(end.getYear(), end.getMonthOfYear(), end.getDayOfMonth());
        return date;
    }

    public List<ExecutionCourse> getAssociatedExecutionCourses() {

        return this.getWrittenEvaluation().getAssociatedExecutionCourses();

    }
    
    
    public static List<String> getEmailsThatShouldBeContactedFor(WrittenEvaluation writtenEvaluation) {
    	List<String> emails = getEmailsBySite(writtenEvaluation);
    	if(emails.size()==0) {
    		emails = getEmailsByTeachers(writtenEvaluation);
    	}
    	return emails;
    }
    
    private static List<String> getEmailsBySite(WrittenEvaluation writtenEvaluation) {
    	Set<String> emails = new HashSet<String>();
    	for(ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
    		String mail = course.getSite().getMail();
    		if(mail!=null) {
    			emails.add(course.getSite().getMail());
    		}
    	}
    	return new ArrayList<String>(emails);
    }
    
    private static List<String> getEmailsByTeachers(WrittenEvaluation writtenEvaluation) {
    	Set<String> emails = new HashSet<String>();
    	for(ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
    		for(Professorship professorship : course.getProfessorships()) {
    			String mail = professorship.getTeacher().getPerson().getEmail(); 
    			emails.add(mail);
    		}
    	}
    	return new ArrayList<String>(emails);
    }
    
    public static List<VigilancyWithCredits> getAllVigilancyWithCredits() {
    	List<VigilancyWithCredits> vigilancys = new ArrayList<VigilancyWithCredits>();
    	for(Vigilancy vigilancy : RootDomainObject.getInstance().getVigilancys()) {
    		if (vigilancy instanceof VigilancyWithCredits) {
				VigilancyWithCredits vigilancyWithCredits = (VigilancyWithCredits) vigilancy;
				vigilancys.add(vigilancyWithCredits);
			}
    	}
    	return vigilancys;
    }
    
    public static List<VigilancyWithCredits> getAllVigilancyWithCredits(Vigilant vigilant) {
    	List<VigilancyWithCredits> vigilancies = new ArrayList<VigilancyWithCredits>();
    	for(VigilancyWithCredits vigilancy : getAllVigilancyWithCredits()) {
    		if(vigilancy.getVigilant().equals(vigilant)) {
    			vigilancies.add(vigilancy);
    		}
    	}
    	return vigilancies;
    }
 
}
