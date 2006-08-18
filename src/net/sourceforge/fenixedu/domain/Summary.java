/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.space.OldRoom;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Jo�o Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 * 
 */
public class Summary extends Summary_Base {

	public static final Comparator<Summary> COMPARATOR_BY_DATE_AND_HOUR = new ComparatorChain();
	static {
		((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("summaryDateYearMonthDay"), true);
		((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("summaryHourHourMinuteSecond"), true);
		((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("idInternal"));
	}

    public Summary() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean compareTo(Object obj) {
        boolean resultado = false;
        if (obj instanceof Summary) {
            Summary summary = (Summary) obj;

            resultado = (summary != null) && this.getShift().equals(summary.getShift())
                    && this.getSummaryDate().equals(summary.getSummaryDate())
                    && this.getSummaryHour().equals(summary.getSummaryHour())
                    && this.getSummaryText().equals(summary.getSummaryText())
                    && this.getTitle().equals(summary.getTitle());
        }
        return resultado;
    }

    private void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson) {

        if (title == null || summaryText == null || isExtraLesson == null)
            throw new NullPointerException();

        setTitle(title);
        setSummaryText(summaryText);
        setStudentsNumber(studentsNumber);
        setIsExtraLesson(isExtraLesson);
        setLastModifiedDate(Calendar.getInstance().getTime());
    }
    
    public void modifyShift(Shift shift, Date summaryDate, Date summaryHour, OldRoom room) {        
        setShift(shift);
        setSummaryDate(summaryDate);
        setSummaryHour(summaryHour);
        setRoom(room);
        setSummaryType(shift.getTipo());
    }

    public void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Professorship professorship) {

        if (professorship == null)
            throw new NullPointerException();
        
        edit(title, summaryText, studentsNumber, isExtraLesson);
        setProfessorship(professorship);
        setTeacher(null);
        setTeacherName(null);
    }

    public void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Teacher teacher) {

        if (teacher == null)
            throw new NullPointerException();
        
        edit(title, summaryText, studentsNumber, isExtraLesson);
        setTeacher(teacher);
        setProfessorship(null);
        setTeacherName(null);
    }

    public void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson,
            String teacherName) {

        if (teacherName == null)
            throw new NullPointerException();
        
        edit(title, summaryText, studentsNumber, isExtraLesson);
        setTeacherName(teacherName);
        setTeacher(null);
        setProfessorship(null);
    }

    public void delete() {        
        removeExecutionCourse();
        removeProfessorship();
        removeRoom();
        removeShift();       
        removeTeacher();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
