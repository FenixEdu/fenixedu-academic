/*
 * Created on 21/Jul/2003
 * 
 *  
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.IShift;
import Util.TipoAula;

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

    public ISummary readSummaryByUnique(IShift shift, Calendar summaryDate, Calendar summaryHour)
            throws ExcepcaoPersistencia;

    public abstract void delete(ISummary summary) throws ExcepcaoPersistencia;
}