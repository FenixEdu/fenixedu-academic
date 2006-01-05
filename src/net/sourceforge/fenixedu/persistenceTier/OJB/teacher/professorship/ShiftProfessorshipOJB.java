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
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

public class ShiftProfessorshipOJB extends PersistentObjectOJB implements IPersistentShiftProfessorship {

    public IShiftProfessorship readByUnique(IShiftProfessorship teacherShiftPercentage) {
        IShiftProfessorship teacherShiftPercentageFromBD = null;

        PersistenceBroker broker = getCurrentPersistenceBroker();

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

    public IShiftProfessorship readByProfessorshipAndShift(IProfessorship professorship, IShift shift)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorship.getIdInternal());
        criteria.addEqualTo("keyShift", shift.getIdInternal());
        return (IShiftProfessorship) queryObject(ShiftProfessorship.class, criteria);
    }

    public List readOverlappingPeriod(Integer teacherId, Integer executionPeriodId,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.keyExecutionPeriod", executionPeriodId);
        criteria.addEqualTo("professorship.keyTeacher", teacherId);
        criteria.addEqualTo("shift.associatedLessons.diaSemana", weekDay);
        criteria.addEqualTo("percentage", new Double(100));

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endTime);

        Criteria startCriteria = new Criteria();
        startCriteria.addGreaterThan("shift.associatedLessons.begin", startTimeCalendar.getTime());
        startCriteria.addLessThan("shift.associatedLessons.begin", endTimeCalendar.getTime());

        Criteria endCriteria = new Criteria();
        endCriteria.addGreaterThan("shift.associatedLessons.end", startTimeCalendar.getTime());
        endCriteria.addLessThan("shift.associatedLessons.end", endTimeCalendar.getTime());

        Criteria equalCriteria = new Criteria();
        equalCriteria.addEqualTo("shift.associatedLessons.begin", startTimeCalendar.getTime());
        equalCriteria.addEqualTo("shift.associatedLessons.end", endTimeCalendar.getTime());
        Criteria timeCriteria = new Criteria();
        timeCriteria.addOrCriteria(startCriteria);
        timeCriteria.addOrCriteria(endCriteria);
        timeCriteria.addOrCriteria(equalCriteria);

        criteria.addAndCriteria(timeCriteria);

        return queryList(ShiftProfessorship.class, criteria);
    }

    public List readByTeacherAndExecutionPeriodAndDegreeType(ITeacher teacher,
            IExecutionPeriod executionPeriod, DegreeType degreeType) throws ExcepcaoPersistencia {
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

    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.idInternal", professorship.getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    public List readByTeacherAndExecutionPeriodWithDifferentIds(ITeacher teacher,
            IExecutionPeriod period, List shiftProfessorShipsIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotIn("idInternal", shiftProfessorShipsIds);
        criteria.addEqualTo("professorship.teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", period
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod period)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", period
                .getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

    public List readByShift(IShift shift) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyShift", shift.getIdInternal());
        return queryList(ShiftProfessorship.class, criteria);
    }

}
