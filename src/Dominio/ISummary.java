/*
 * Created on 21/Jul/2003
 *
 * 
 */
package Dominio;

import java.util.Calendar;
import java.util.Date;

import Util.TipoAula;

/**
 * @author Tânia Pousão
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author João Mota
 *
 * 21/Jul/2003
 * fenix-head
 * Dominio
 * 
 */
public interface ISummary extends IDomainObject {
 
    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the Execution Course object.
     * Now Summary object are linked to the Shift object.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by Execution Course object.  
     */
	public abstract IExecutionCourse getExecutionCourse();

    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the Execution Course object.
     * Now Summary object are linked to the Shift object.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by Execution Course object.  
     */
	public abstract void setExecutionCourse(IExecutionCourse executionCourse);
    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the Execution Course object.
     * Now Summary object are linked to the Shift object.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by Execution Course object.  
     */
	public abstract Integer getKeyExecutionCourse();
    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the Execution Course object.
     * Now Summary object are linked to the Shift object.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by Execution Course object.  
     */
	public abstract void setKeyExecutionCourse(Integer keyExecutionCourse);
    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the ExecutionCourse object 
     * and has Summary Type attribute.
     * Now Summary object are linked to the Shift object, then Summary Type attribute is the same than Shift Type.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by ExecutionCourse object and if necessary by Summary Type.
     */
	public abstract TipoAula getSummaryType();
	
    /**
     * @deprecated
     * In spite of this method is deprecated it is used, 
     * because in the past Summary object are linked to the ExecutionCourse object 
     * and has Summary Type attribute.
     * Now Summary object are linked to the Shift object, then Summary Type attribute is the same than Shift Type.
     * Once were decided not make migrations, all Summary with Shift null or zero 
     * should be readed by ExecutionCourse object and if necessary by Summary Type.  
     */
	public abstract void setSummaryType(TipoAula summaryType);
	
	public abstract Date getLastModifiedDate();
	public abstract void setLastModifiedDate(Date lastModifiedDate);
	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract Calendar getSummaryDate();
	public abstract void setSummaryDate(Calendar summaryDate);
	public abstract String getSummaryText();
	public abstract void setSummaryText(String summaryText);
	public Calendar getSummaryHour();
	public void setSummaryHour(Calendar summaryHour);
	public boolean compareTo(Object obj);
	
	public Integer getKeyProfessorship();
	public void setKeyProfessorship(Integer keyProfessorship);
	public IProfessorship getProfessorship();
	public void setProfessorship(IProfessorship professorship);
	public Integer getKeyShift();
	public void setKeyShift(Integer keyShift);
	public ITurno getShift();
	public void setShift(ITurno shift);
	public Integer getKeyTeacher();
	public void setKeyTeacher(Integer keyTeacher);
	public ITeacher getTeacher();
	public void setTeacher(ITeacher teacher);
	public String getTeacherName();
	public void setTeacherName(String teacherName);
	public Integer getStudentsNumber();
	public void setStudentsNumber(Integer studentsNumber);
	public Boolean getIsExtraLesson();
	public void setIsExtraLesson(Boolean isExtraLesson);
	public ISala getRoom();
	public void setRoom(ISala room);
}