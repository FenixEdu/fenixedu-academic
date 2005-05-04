/*
 * Created on 21/Jul/2003
 * 
 *  
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author João Mota
 * 
 * 21/Jul/2003 fenix-head ServidorPersistente.OJB
 *  
 */
public interface IPersistentSummary extends IPersistentObject {
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public List readByExecutionCourseShifts(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, TipoAula summaryType)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourseShiftsAndTypeLesson(IExecutionCourse executionCourse,
            TipoAula summaryType) throws ExcepcaoPersistencia;

    public List readByShift(IExecutionCourse executionCourse, IShift shift) throws ExcepcaoPersistencia;

    public List readByTeacher(IExecutionCourse executionCourse, ITeacher teacher)
            throws ExcepcaoPersistencia;

    public List readByOtherTeachers(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    public ISummary readSummaryByUnique(IShift shift, Date summaryDate, Date summaryHour)
            throws ExcepcaoPersistencia;

    public abstract void delete(ISummary summary) throws ExcepcaoPersistencia;
}