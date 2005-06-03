/*
 * ITurnoOJB.java
 * 
 * Created on 17 de Outubro de 2002, 19:35
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class TurnoVO extends VersionedObjectsBase implements ITurnoPersistente {

    public IShift readByNameAndExecutionCourse(String shiftName, Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<IShift> shifts = executionCourse.getAssociatedShifts();
        for (IShift shift : shifts) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("disciplinaExecucao.idInternal",
         * executionCourse.getIdInternal()); crit.addEqualTo("nome", shiftName);
         * return (IShift) queryObject(Shift.class, crit);
         */
    }

    public List readByExecutionCourseAndType(Integer executionCourseOID, ShiftType type)
            throws ExcepcaoPersistencia {

        IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<IShift> shifts = executionCourse.getAssociatedShifts();
        List<IShift> result = new ArrayList();
        for (IShift shift : shifts) {
            if (shift.getTipo().equals(type)) {
                result.add(shift);
            }
        }
        return result;

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("disciplinaExecucao.idInternal",
         * executionCourse.getIdInternal()); crit.addEqualTo("tipo", type);
         * return queryList(Shift.class, crit);
         */

    }

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia {
        IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        return executionCourse.getAssociatedShifts();

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("disciplinaExecucao.idInternal",
         * executionCourse.getIdInternal()); return queryList(Shift.class,
         * crit);
         */

    }

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(Integer executionPeriodOID,
            Integer executionDegreeOID, Integer curricularYearOID) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);

        List<IShift> shifts = (List<IShift>) readAll(Shift.class);

        List result = new ArrayList();

        for (IShift shift : shifts) {
            List<ICurricularCourse> curricularCourses = shift.getDisciplinaExecucao()
                    .getAssociatedCurricularCourses();
            for (ICurricularCourse curricularCourse : curricularCourses) {
                List<ICurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
                for (ICurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                    if (curricularCourseScope.getCurricularSemester().getCurricularYear()
                            .getIdInternal().equals(curricularYearOID)
                            && curricularCourseScope.getCurricularSemester().getSemester().equals(
                                    executionPeriod.getSemester())
                            && curricularCourse.getDegreeCurricularPlan().getIdInternal().equals(
                                    executionDegree.getDegreeCurricularPlan().getIdInternal())
                            && shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal()
                                    .equals(executionPeriodOID)) {

                        result.add(shift);

                    }
                }
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria .addEqualTo(
         * "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
         * curricularYear.getIdInternal()); criteria.addEqualTo(
         * "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.semester",
         * executionPeriod.getSemester()); criteria.addEqualTo(
         * "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
         * executionDegree.getDegreeCurricularPlan().getIdInternal());
         * criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal",
         * executionPeriod .getIdInternal());
         * 
         * List shifts = queryList(Shift.class, criteria, true); return shifts;
         */
    }

    public List readAvailableShiftsForClass(Integer schoolClassOID) throws ExcepcaoPersistencia {
        List<IShift> shifts = (List<IShift>) readAll(Shift.class);
        ISchoolClass schoolClass = (ISchoolClass) readByOID(SchoolClass.class, schoolClassOID);
        List result = new ArrayList();

        for (IShift shift : shifts) {
            List<ICurricularCourse> curricularCourses = shift.getDisciplinaExecucao()
                    .getAssociatedCurricularCourses();
            for (ICurricularCourse curricularCourse : curricularCourses) {
                List<ICurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
                for (ICurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                    if (curricularCourseScope.getCurricularSemester().getCurricularYear()
                            .getIdInternal().equals(schoolClass.getAnoCurricular())
                            && curricularCourse.getDegreeCurricularPlan().getIdInternal().equals(
                                    schoolClass.getExecutionDegree().getDegreeCurricularPlan()
                                            .getIdInternal())
                            && shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal()
                                    .equals(schoolClass.getExecutionPeriod().getIdInternal())) {
                        result.add(shift);
                    }
                }
            }
        }
        List classShifts = new TurmaTurnoVO().readByClass(schoolClassOID);
        result.removeAll(classShifts);

        return result;

        /*
         * Criteria criteria = new Criteria();
         * 
         * criteria .addEqualTo(
         * "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
         * schoolClass.getAnoCurricular()); criteria.addEqualTo(
         * "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
         * schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
         * criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal",
         * schoolClass .getExecutionPeriod().getIdInternal());
         * 
         * List shifts = queryList(Shift.class, criteria, true);
         */
        /*
         * List classShifts = new
         * TurmaTurnoOJB().readByClass(schoolClass.getIdInternal());
         */

    }

    public IShift readByLesson(Integer lessonOID) throws ExcepcaoPersistencia {

        if (lessonOID != null) {
            ILesson lesson = (ILesson) readByOID(Lesson.class, lessonOID);
            return lesson.getShift();
        } else
            return null;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("associatedLessons.idInternal",
         * lesson.getIdInternal()); return queryList(Shift.class, criteria);
         */
    }

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        List<IShift> shifts = (List<IShift>) readAll(Shift.class);
        List<IShift> result = new ArrayList();
        for (IShift shift : shifts) {
            if (shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal().equals(
                    executionPeriodOID)
                    && shift.getDisciplinaExecucao().getAttendingStudents().contains(student)) {
                result.add(shift);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("disciplinaExecucao.attendingStudents.idInternal",
         * student.getIdInternal());
         * criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal",
         * executionPeriod .getIdInternal()); return queryList(Shift.class,
         * criteria, true);
         */
    }

}