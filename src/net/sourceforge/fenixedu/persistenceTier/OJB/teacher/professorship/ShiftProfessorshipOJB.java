/*
 * Created on 19/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

/**
 * @author jpvl
 */
public class ShiftProfessorshipOJB extends PersistentObjectOJB implements IPersistentShiftProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTeacherShiftPercentage#readByUnique(Dominio.IShiftProfessorship)
     */
    public IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage)
            throws ExcepcaoPersistencia {
        IShiftProfessorship teacherShiftPercentageFromBD = null;

        PersistenceBroker broker = ((HasBroker) odmg.currentTransaction()).getBroker();

        Criteria criteria = new Criteria();

        IExecutionCourse executionCourse = teacherShiftPercentage.getShift().getDisciplinaExecucao();

        criteria.addEqualTo("shift.nome", teacherShiftPercentage.getShift().getNome());

        criteria.addEqualTo("shift.disciplinaExecucao.sigla", executionCourse.getSigla());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.name", executionCourse
                .getExecutionPeriod().getName());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.executionYear.year",
                executionCourse.getExecutionPeriod().getExecutionYear().getYear());

        ITeacher teacher = teacherShiftPercentage.getProfessorship().getTeacher();

        criteria.addEqualTo("professorShip.teacher.teacherNumber", teacher.getTeacherNumber());
        criteria.addEqualTo("professorShip.teacher.person.username", teacher.getPerson().getUsername());
        criteria.addEqualTo("professorShip.executionCourse.sigla", executionCourse.getSigla());
        criteria.addEqualTo("professorShip.executionCourse.executionPeriod.name", executionCourse
                .getExecutionPeriod().getName());
        criteria.addEqualTo("professorShip.executionCourse.executionPeriod.executionYear.year",
                executionCourse.getExecutionPeriod().getExecutionYear().getYear());

        Query queryPB = new QueryByCriteria(ShiftProfessorship.class, criteria);
        teacherShiftPercentageFromBD = (IShiftProfessorship) broker.getObjectByQuery(queryPB);
        return teacherShiftPercentageFromBD;
    }

    //    /*
    //	 * (non-Javadoc)
    //	 *
    //	 * @see ServidorPersistente.IPersistentObject#lockWrite(java.lang.Object)
    //	 */
    //    public void lockWrite(Object obj) throws ExcepcaoPersistencia
    //    {
    //        if (obj instanceof IShiftProfessorship)
    //        {
    //
    //            IShiftProfessorship teacherShiftPercentageToWrite = (IShiftProfessorship)
    // obj;
    //
    //            IShiftProfessorship teacherShiftPercentageFromBD = this.readByUnique(
    //                    teacherShiftPercentageToWrite);
    //            // If department is not in database, then write it.
    //            if (teacherShiftPercentageFromBD == null)
    //            {
    //                super.lockWrite(teacherShiftPercentageToWrite);
    //            }
    //            else if (// else If the department is mapped to the database, then write
    // any existing
    //					   // changes.
    //            teacherShiftPercentageFromBD.getIdInternal()
    //                    .equals(teacherShiftPercentageToWrite.getIdInternal()))
    //            {
    //                super.lockWrite(teacherShiftPercentageToWrite);
    //
    //            }
    //            else
    //            { // else Throw an already existing exception
    //                throw new ExistingPersistentException();
    //            }
    //        }
    //    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentTeacherShiftPercentage#delete(Dominio.IShiftProfessorship)
     */
    public void delete(IShiftProfessorship teacherShiftPercentage) throws ExcepcaoPersistencia {
        super.delete(teacherShiftPercentage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByProfessorshipAndShift(Dominio.IProfessorship,
     *      Dominio.IShift)
     */
    public IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, IShift shift)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorship.getIdInternal());
        criteria.addEqualTo("keyShift", shift.getIdInternal());
        return (IShiftProfessorship) queryObject(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readOverlappingPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod, Util.DiaSemana, java.util.Date,
     *      java.util.Date)
     */
    public List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.keyExecutionPeriod", executionPeriod
                .getIdInternal());
        criteria.addEqualTo("professorship.keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("shift.associatedLessons.diaSemana", weekDay);
        criteria.addEqualTo("percentage", new Double(100));

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endTime);

        Criteria startCriteria = new Criteria();
        startCriteria.addGreaterThan("shift.associatedLessons.inicio", startTimeCalendar);
        startCriteria.addLessThan("shift.associatedLessons.inicio", endTimeCalendar);

        Criteria endCriteria = new Criteria();
        endCriteria.addGreaterThan("shift.associatedLessons.fim", startTimeCalendar);
        endCriteria.addLessThan("shift.associatedLessons.fim", endTimeCalendar);

        Criteria equalCriteria = new Criteria();
        equalCriteria.addEqualTo("shift.associatedLessons.inicio", startTimeCalendar);
        equalCriteria.addEqualTo("shift.associatedLessons.fim", endTimeCalendar);
        Criteria timeCriteria = new Criteria();
        timeCriteria.addOrCriteria(startCriteria);
        timeCriteria.addOrCriteria(endCriteria);
        timeCriteria.addOrCriteria(equalCriteria);

        criteria.addAndCriteria(timeCriteria);

        return queryList(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriodAndDegreeType(ITeacher teacher,
            IExecutionPeriod executionPeriod, TipoCurso degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        criteria
                .addEqualTo(
                        "professorship.executionCourse.associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso",
                        degreeType);
        return queryList(ShiftProfessorship.class, criteria, true);
    }

    //    /* (non-Javadoc)
    //     * @see
    // ServidorPersistente.IPersistentShiftProfessorship#readByProfessorship(Dominio.IProfessorship)
    //     */
    //    public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    //    {
    //		Criteria criteria = new Criteria();
    //		criteria.addEqualTo("professorship.teacher.idInternal",
    // teacher.getIdInternal());
    //        return queryList(ShiftProfessorship.class, criteria);
    //    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByProfessorship(Dominio.IProfessorship)
     */
    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.idInternal", professorship.getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByProfessorshipWithDifferentIds(Dominio.IProfessorship,
     *      java.util.List)
     */
    public List readByTeacherAndExecutionPeriodWithDifferentIds(ITeacher teacher,
            IExecutionPeriod period, List shiftProfessorShipsIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotIn("idInternal", shiftProfessorShipsIds);
        criteria.addEqualTo("professorship.teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", period
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod period)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", period
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentShiftProfessorship#readByShift(Dominio.IShift)
     */
    public List readByShift(IShift shift) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyShift", shift.getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }
}