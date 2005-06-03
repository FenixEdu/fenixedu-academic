/*
 * ITurnoOJB.java
 * 
 * Created on 17 de Outubro de 2002, 19:35
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;

import org.apache.ojb.broker.query.Criteria;

public class TurnoOJB extends ObjectFenixOJB implements ITurnoPersistente {

    public IShift readByNameAndExecutionCourse(String shiftName, Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourseOID);
        crit.addEqualTo("nome", shiftName);
        return (IShift) queryObject(Shift.class, crit);
    }

    public List readByExecutionCourseAndType(Integer executionCourseOID, ShiftType type)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourseOID);
        crit.addEqualTo("tipo", type);
        return queryList(Shift.class, crit);

    }
    
    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourseOID);
        return queryList(Shift.class, crit);

    }
 
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(Integer executionPeriodOID,
            Integer executionDegreeOID, Integer curricularYearOID) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);

        Criteria criteria = new Criteria();

        criteria
                .addEqualTo(
                        "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                        curricularYearOID);
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriod.getSemester());
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriod
                .getIdInternal());

        List shifts = queryList(Shift.class, criteria, true);
        return shifts;
    }

    public List readAvailableShiftsForClass(Integer schoolClassOID) throws ExcepcaoPersistencia {

        ISchoolClass schoolClass = (ISchoolClass) readByOID(SchoolClass.class, schoolClassOID);

        Criteria criteria = new Criteria();

        criteria
                .addEqualTo(
                        "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                        schoolClass.getAnoCurricular());
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", schoolClass
                .getExecutionPeriod().getIdInternal());

        List shifts = queryList(Shift.class, criteria, true);

        List classShifts = new TurmaTurnoOJB().readByClass(schoolClass.getIdInternal());

        shifts.removeAll(classShifts);

        return shifts;
    }

    public IShift readByLesson(Integer lessonOID) throws ExcepcaoPersistencia {
        if (lessonOID != null) {
        Criteria criteria = new Criteria();
            criteria.addEqualTo("associatedLessons.idInternal", lessonOID);
            return (IShift) queryObject(Shift.class, criteria);
    }

        return null;

    }

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
            Criteria criteria = new Criteria();

        criteria.addEqualTo("disciplinaExecucao.attendingStudents.idInternal", studentOID);
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriodOID);
        return queryList(Shift.class, criteria, true);
    }

}