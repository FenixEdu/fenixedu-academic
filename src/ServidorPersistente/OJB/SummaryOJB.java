/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.Summary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernades
 * 
 * 21/Jul/2003 fenix-head ServidorPersistente.OJB
 *  
 */
public class SummaryOJB extends PersistentObjectOJB implements IPersistentSummary {

    /**
     *  
     */
    public SummaryOJB() {
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return queryList(Summary.class, criteria);

    }

    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, TipoAula summaryType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("summaryType", summaryType.getTipo());
        return queryList(Summary.class, criteria);

    }

    public void delete(ISummary summary) throws ExcepcaoPersistencia {
        super.delete(summary);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readByExecutionCourseShifts(Dominio.IExecutionCourse)
     *      TODO: This method will replace the method readByExecutionCourse
     */
    public List readByExecutionCourseShifts(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());

        return queryList(Summary.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readByExecutionCourseShiftsAndTypeLesson(Dominio.IExecutionCourse,
     *      Util.TipoAula) TODO: This method will replace the method
     *      readByExecutionCourseAndType
     */
    public List readByExecutionCourseShiftsAndTypeLesson(IExecutionCourse executionCourse,
            TipoAula summaryType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        criteria.addEqualTo("shift.tipo", summaryType.getTipo());

        return queryList(Summary.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readByShift(Dominio.ITurno)
     */
    public List readByShift(IExecutionCourse executionCourse, ITurno shift) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());

        return queryList(Summary.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readByTeacher(Dominio.ITeacher)
     */
    public List readByTeacher(IExecutionCourse executionCourse, ITeacher teacher)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        criteria.addEqualTo("professorship.teacher.teacherNumber", teacher.getTeacherNumber());

        return queryList(Summary.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readByOtherTeachers(Dominio.IExecutionCourse)
     */
    public List readByOtherTeachers(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        criteria.addIsNull("professorship");

        Criteria criteria2 = new Criteria();
        criteria2.addNotNull("teacher");

        Criteria criteria3 = new Criteria();
        criteria3.addNotNull("teacherName");

        criteria2.addOrCriteria(criteria3);

        criteria.addAndCriteria(criteria2);

        return queryList(Summary.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentSummary#readSummaryByUnique(Dominio.ITurno,
     *      java.util.Calendar, java.util.Calendar)
     */
    public ISummary readSummaryByUnique(ITurno shift, Calendar summaryDate, Calendar summaryHour)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());
        criteria.addEqualTo("summaryDate", summaryDate);
        criteria.addEqualTo("summaryHour", summaryHour);

        return (ISummary) queryObject(Summary.class, criteria);
    }
}