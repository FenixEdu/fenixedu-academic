/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.ojb.broker.query.Criteria;

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
     * @see ServidorPersistente.IPersistentSummary#readByShift(Dominio.IShift)
     */
    public List readByShift(IExecutionCourse executionCourse, IShift shift) throws ExcepcaoPersistencia {
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
     * @see ServidorPersistente.IPersistentSummary#readSummaryByUnique(Dominio.IShift,
     *      java.util.Calendar, java.util.Calendar)
     */
    public ISummary readSummaryByUnique(IShift shift, Date summaryDate, Date summaryHour)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());
        criteria.addEqualTo("summaryDate", summaryDate);
        criteria.addEqualTo("summaryHour", summaryHour);

        return (ISummary) queryObject(Summary.class, criteria);
    }
}