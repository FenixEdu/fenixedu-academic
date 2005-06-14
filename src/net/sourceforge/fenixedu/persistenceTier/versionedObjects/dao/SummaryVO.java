/*
 * Created on May 5, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author mrsp and jdnf
 */
public class SummaryVO extends VersionedObjectsBase implements IPersistentSummary {
    
    public List readByExecutionCourse(final Integer executionCourseID) throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class, executionCourseID);
        return (executionCourse != null) ? executionCourse.getAssociatedSummaries() : new ArrayList();
    }
    
    public List readByExecutionCourseShifts(final Integer executionCourseID) throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class, executionCourseID);
        List summaries = new ArrayList();        
        List shifts = executionCourse.getAssociatedShifts();
        Iterator iter = shifts.iterator();        
        while(iter.hasNext()){
            IShift shift = (IShift) iter.next();
            summaries.addAll(shift.getAssociatedSummaries());             
        }     
        return summaries;
    }
    
    public List readByExecutionCourseAndType(final Integer executionCourseID, ShiftType summaryType)
    throws ExcepcaoPersistencia {
       
        List summaries = readByExecutionCourse(executionCourseID);
        Iterator iter = summaries.iterator();
        List summariesAux = new ArrayList();
               
        while(iter.hasNext()){
            ISummary summary = (ISummary) iter.next();
           if(summary.getSummaryType() != null){
	            if(summary.getSummaryType().equals(summaryType))
                summariesAux.add(summary);
        }
        }
        
        return summariesAux;
    }
    
    public List readByExecutionCourseShiftsAndTypeLesson(final Integer executionCourseID, ShiftType summaryType) throws ExcepcaoPersistencia {
        
        List summaries = readByExecutionCourseShifts(executionCourseID);
        Iterator iter = summaries.iterator();
        List summariesAux = new ArrayList();
        
        while(iter.hasNext()){
            ISummary summary = (ISummary) iter.next();
               if(summary.getShift().getTipo().equals(summaryType))
                summariesAux.add(summary);
        }       
        
        return summariesAux;
    }
    
    public List readByShift(final Integer executionCourseID, final Integer shiftID) throws ExcepcaoPersistencia {
        
        List summaries = readByExecutionCourseShifts(executionCourseID);        
        Iterator iter = summaries.iterator();
        List summariesAux = new ArrayList();
        
        while(iter.hasNext()){
            ISummary summary = (ISummary) iter.next();
            if(summary.getKeyShift() == shiftID)
                summariesAux.add(summary);
        }       
        
        return summariesAux;
    }
    
    public List readByTeacher(final Integer executionCourseID, final Integer teacherNumber)
    throws ExcepcaoPersistencia {
        
        List summaries = readByExecutionCourseShifts(executionCourseID);
        Iterator iter = summaries.iterator();
        List summariesAux = new ArrayList();
        
        while(iter.hasNext()){
            ISummary summary = (ISummary) iter.next();
            if(summary.getKeyProfessorship() != null)
                if(summary.getProfessorship().getTeacher().getTeacherNumber() == teacherNumber)                  
                    summariesAux.add(summary);               
        }
        
        return summariesAux;
    }
    
    public List readByOtherTeachers(final Integer executionCourseID) throws ExcepcaoPersistencia {
        
        List summaries = readByExecutionCourse(executionCourseID);
        Iterator iter = summaries.iterator();
        List summariesAux = new ArrayList();

        while(iter.hasNext()){
            ISummary summary = (ISummary) iter.next();
            if(summary.getProfessorship() == null)
                if((summary.getTeacher() != null) || (summary.getTeacherName() != null))
                    summariesAux.add(summary);            
        }        
        return summariesAux;
    }
    
    public ISummary readSummaryByUnique(final Integer shiftID, Date summaryDate, Date summaryHour)
    throws ExcepcaoPersistencia {
                
        final IShift shift = (IShift) readByOID(Shift.class, shiftID);
        ISummary summary = null;
       
        if(shift != null){
        List summaries = shift.getAssociatedSummaries();
        Iterator iter = summaries.iterator();
        while(iter.hasNext()){
	            ISummary summary2 = (ISummary) iter.next();
	            if((summary2.getSummaryDate().equals(summaryDate)) && (summary2.getSummaryHour().equals(summaryHour)))
	                summary = summary2;            
        }        
        }
        return summary;
    }
}
