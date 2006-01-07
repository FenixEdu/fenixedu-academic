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
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class TurnoVO extends VersionedObjectsBase implements ITurnoPersistente {

    public Shift readByNameAndExecutionCourse(String shiftName, Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<Shift> shifts = executionCourse.getAssociatedShifts();
        for (Shift shift : shifts) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;

    }

    public List readByExecutionCourseAndType(Integer executionCourseOID, ShiftType type)
            throws ExcepcaoPersistencia {

        ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<Shift> shifts = executionCourse.getAssociatedShifts();
        List<Shift> result = new ArrayList();
        for (Shift shift : shifts) {
            if (shift.getTipo().equals(type)) {
                result.add(shift);
            }
        }
        return result;
    }

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia {
        ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        return executionCourse.getAssociatedShifts();
    }

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(Integer executionPeriodOID,
            Integer executionDegreeOID, Integer curricularYearOID) throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);

        List<Shift> shifts = (List<Shift>) readAll(Shift.class);

        List result = new ArrayList();

        for (Shift shift : shifts) {
            List<CurricularCourse> curricularCourses = shift.getDisciplinaExecucao()
                    .getAssociatedCurricularCourses();
            for (CurricularCourse curricularCourse : curricularCourses) {
                List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
                for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
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

    }

    public List readAvailableShiftsForClass(Integer schoolClassOID) throws ExcepcaoPersistencia {

        SchoolClass schoolClass = (SchoolClass) readByOID(SchoolClass.class, schoolClassOID);

        List result = new ArrayList();

        ExecutionPeriod executionPeriod = schoolClass.getExecutionPeriod();
        List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            List<Shift> shifts = executionCourse.getAssociatedShifts();
            for (Shift shift : shifts) {
                List<CurricularCourse> curricularCourses = shift.getDisciplinaExecucao()
                        .getAssociatedCurricularCourses();
                for (CurricularCourse curricularCourse : curricularCourses) {
                    if (curricularCourse.getDegreeCurricularPlan().getIdInternal().equals(
                            schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal())) {
                        List<CurricularCourseScope> curricularCourseScopes = curricularCourse
                                .getScopes();
                        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                            if (curricularCourseScope.getCurricularSemester().getCurricularYear()
                                    .getIdInternal().equals(schoolClass.getAnoCurricular())) {
                                if (!result.contains(shift)) {
                                    result.add(shift);
                                }

                            }
                        }
                    }
                }
            }
        }
        List classShifts = schoolClass.getAssociatedShifts();
        result.removeAll(classShifts);
        return result;
    }

    public Shift readByLesson(Integer lessonOID) throws ExcepcaoPersistencia {

        if (lessonOID != null) {
            Lesson lesson = (Lesson) readByOID(Lesson.class, lessonOID);
            return lesson.getShift();
        }
        return null;
    }

    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        Student student = (Student) readByOID(Student.class, studentOID);

        List<Attends> attends = student.getAssociatedAttends();
        List<Shift> result = new ArrayList();

        for (Attends attend : attends) {
            ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
            if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodOID)) {
                List<Shift> shifts = executionCourse.getAssociatedShifts();
                System.out.println("ADICIONei turnos");
                result.addAll(shifts);
            }
        }

        System.out.println("TAMANHO " + result.size());
        return result;

    }
}