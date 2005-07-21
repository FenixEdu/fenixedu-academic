/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 * 
 */
public class Summary extends Summary_Base {

    public boolean compareTo(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISummary) {
            ISummary summary = (ISummary) obj;

            resultado = (summary != null) && this.getShift().equals(summary.getShift())
                    && this.getSummaryDate().equals(summary.getSummaryDate())
                    && this.getSummaryHour().equals(summary.getSummaryHour())
                    && this.getSummaryText().equals(summary.getSummaryText())
                    && this.getTitle().equals(summary.getTitle());
        }
        return resultado;
    }

    private void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson) {

        if (title == null || summaryText == null || studentsNumber == null || isExtraLesson == null)
            throw new NullPointerException();

        setTitle(title);
        setSummaryText(summaryText);
        setStudentsNumber(studentsNumber);
        setIsExtraLesson(isExtraLesson);
        setLastModifiedDate(Calendar.getInstance().getTime());
    }
    
    public void modifyShift(IShift shift, Date summaryDate, Date summaryHour, IRoom room) {        
        setShift(shift);
        setSummaryDate(summaryDate);
        setSummaryHour(summaryHour);
        setRoom(room);
    }

    public void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson,
            IProfessorship professorship) {

        if (professorship == null)
            throw new NullPointerException();
        
        edit(title, summaryText, studentsNumber, isExtraLesson);
        setProfessorship(professorship);
        setTeacher(null);
        setTeacherName(null);
    }

    public void edit(String title, String summaryText, Integer studentsNumber, Boolean isExtraLesson,
            ITeacher teacher) {

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
        setExecutionCourse(null);
        setProfessorship(null);
        setShift(null);
        setRoom(null);
        setTeacher(null);
    }

}
