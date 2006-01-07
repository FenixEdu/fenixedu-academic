/*
 * TurmaOJB.java
 * 
 * Created on 17 de Outubro de 2002, 18:44
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class TurmaVO extends VersionedObjectsBase implements ITurmaPersistente {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(SchoolClass.class);
    }

    public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(Integer executionPeriodOID,
            Integer curricularYear, Integer executionDegreeOID) throws ExcepcaoPersistencia {

        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<SchoolClass> schoolClasses = executionDegree.getSchoolClasses();

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        List<SchoolClass> result = new ArrayList();

        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getExecutionPeriod().getExecutionYear().getYear().equals(
                    executionPeriod.getExecutionYear().getYear())
                    && schoolClass.getExecutionPeriod().getName().equals(executionPeriod.getName())
                    && schoolClass.getAnoCurricular().equals(curricularYear)
                    && schoolClass.getExecutionDegree().getExecutionYear().getYear().equals(
                            executionDegree.getExecutionYear().getYear())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName().equals(
                            executionDegree.getDegreeCurricularPlan().getName())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla()
                            .equals(executionDegree.getDegreeCurricularPlan().getDegree().getSigla())) {

                result.add(schoolClass);
            }
        }
        return result;

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("executionPeriod.executionYear.year",
         * executionPeriod.getExecutionYear() .getYear());
         * crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
         * crit.addEqualTo("anoCurricular", curricularYear);
         * crit.addEqualTo("executionDegree.executionYear.year",
         * executionDegree.getExecutionYear() .getYear());
         * crit.addEqualTo("executionDegree.degreeCurricularPlan.name",
         * executionDegree .getDegreeCurricularPlan().getName());
         * crit.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla",
         * executionDegree .getDegreeCurricularPlan().getDegree().getSigla());
         * return queryList(SchoolClass.class, crit);
         */

    }

    public SchoolClass readByNameAndExecutionDegreeAndExecutionPeriod(String className,
            Integer executionDegreeOID, Integer executionPeriodOID) throws ExcepcaoPersistencia {

        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<SchoolClass> schoolClasses = executionDegree.getSchoolClasses();

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getNome().equals(className)
                    && schoolClass.getExecutionPeriod().getName().equals(executionPeriod.getName())
                    && schoolClass.getExecutionPeriod().getExecutionYear().getYear().equals(
                            executionPeriod.getExecutionYear().getYear())
                    && schoolClass.getExecutionDegree().getExecutionYear().getYear().equals(
                            executionDegree.getExecutionYear().getYear())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName().equals(
                            executionDegree.getDegreeCurricularPlan().getName())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla()
                            .equals(executionDegree.getDegreeCurricularPlan().getDegree().getSigla())) {
                return schoolClass;
            }
        }
        return null;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("nome",className);
         * criteria.addEqualTo("executionPeriod.name",executionPeriod.getName());
         * criteria.addEqualTo("executionPeriod.executionYear.year",executionPeriod.getExecutionYear().getYear());
         * criteria.addEqualTo("executionDegree.executionYear.year",executionDegree.getExecutionYear().getYear());
         * criteria.addEqualTo("executionDegree.degreeCurricularPlan.name",executionDegree.getDegreeCurricularPlan().getName());
         * criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla",executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
         * 
         * return (SchoolClass) queryObject(SchoolClass.class, criteria);
         */

    }

    public List readByExecutionPeriod(Integer executionPeriodOID) throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        List<SchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        List<SchoolClass> result = new ArrayList();
        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getExecutionPeriod().getExecutionYear().getYear().equals(
                    executionPeriod.getExecutionYear().getYear())
                    && schoolClass.getExecutionPeriod().getName().equals(executionPeriod.getName())) {
                result.add(schoolClass);
            }
        }
        return result;

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("executionPeriod.executionYear.year",
         * executionPeriod.getExecutionYear() .getYear());
         * crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
         * return queryList(SchoolClass.class, crit);
         */

    }

    public List readByExecutionDegree(Integer executionDegreeOID) throws ExcepcaoPersistencia {
        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<SchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<SchoolClass> result = new ArrayList();
        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getExecutionDegree().getExecutionYear().getYear().equals(
                    executionDegree.getExecutionYear().getYear())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName().equals(
                            executionDegree.getDegreeCurricularPlan().getName())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla()
                            .equals(executionDegree.getDegreeCurricularPlan().getDegree().getSigla())) {
                result.add(schoolClass);
            }
        }
        return result;

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("executionDegree.executionYear.year",
         * executionDegree.getExecutionYear() .getYear());
         * crit.addEqualTo("executionDegree.degreeCurricularPlan.name",
         * executionDegree .getDegreeCurricularPlan().getName());
         * crit.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla",
         * executionDegree .getDegreeCurricularPlan().getDegree().getSigla());
         * return queryList(SchoolClass.class, crit);
         */

    }

    public List readByExecutionDegreeAndExecutionPeriod(Integer execucaoOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia {

        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                execucaoOID);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        List<SchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<SchoolClass> result = new ArrayList();
        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getExecutionPeriod().getExecutionYear().getYear().equals(
                    executionPeriod.getExecutionYear().getYear())
                    && schoolClass.getExecutionPeriod().getName().equals(executionPeriod.getName())
                    && schoolClass.getExecutionDegree().getExecutionYear().getYear().equals(
                            executionDegree.getExecutionYear().getYear())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName().equals(
                            executionDegree.getDegreeCurricularPlan().getName())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla()
                            .equals(executionDegree.getDegreeCurricularPlan().getDegree().getSigla())) {
                result.add(schoolClass);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("executionPeriod.executionYear.year",
         * executionPeriod.getExecutionYear() .getYear());
         * criteria.addEqualTo("executionPeriod.name",
         * executionPeriod.getName());
         * criteria.addEqualTo("executionDegree.executionYear.year",
         * execucao.getExecutionYear().getYear());
         * criteria.addEqualTo("executionDegree.degreeCurricularPlan.name",
         * execucao .getDegreeCurricularPlan().getName());
         * criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla",
         * execucao .getDegreeCurricularPlan().getDegree().getSigla()); // Query
         * queryPB = new QueryByCriteria(SchoolClass.class, criteria); return
         * queryList(SchoolClass.class, criteria);
         */
    }

    public List readByExecutionDegreeAndDegreeAndExecutionPeriod(Integer execucaoOID, Integer degreeOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        ExecutionDegree executionDegree = (ExecutionDegree) readByOID(ExecutionDegree.class,
                execucaoOID);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        Degree degree = (Degree) readByOID(Degree.class, degreeOID);
        List<SchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<SchoolClass> result = new ArrayList();
        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getExecutionDegree().getIdInternal().equals(executionDegree.getIdInternal())
                    && schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                            .getIdInternal().equals(degree.getIdInternal())
                    && schoolClass.getExecutionPeriod().getIdInternal().equals(
                            executionPeriod.getIdInternal())) {
                result.add(schoolClass);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("executionDegree.idInternal",
         * execucao.getIdInternal());
         * criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.idInternal",
         * degree .getIdInternal());
         * criteria.addEqualTo("executionPeriod.idInternal",
         * executionPeriod.getIdInternal()); return queryList(SchoolClass.class,
         * criteria);
         */
    }

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia {
        ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<Shift> associatedShifts = executionCourse.getAssociatedShifts();
        List<SchoolClass> result = new ArrayList();
        for (Shift shift : associatedShifts) {
            List<SchoolClass> schoolClasses = shift.getAssociatedClasses();
            for (SchoolClass schoolClass : schoolClasses) {
                List<Shift> shifts = schoolClass.getAssociatedShifts();
                for (Shift turno : shifts) {
                    if (turno.getDisciplinaExecucao().getIdInternal().equals(
                            executionCourse.getIdInternal()) && !result.contains(schoolClass)) {
                        result.add(schoolClass);
                    }
                }
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("associatedShifts.disciplinaExecucao.idInternal",
         * executionCourse .getIdInternal()); return
         * queryList(SchoolClass.class, criteria, true);
         */
    }

    public List readClassesThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        Student student = (Student) readByOID(Student.class, studentOID);
        List<SchoolClass> result = new ArrayList();
        List<SchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        for (SchoolClass schoolClass : schoolClasses) {
            List<Shift> shifts = schoolClass.getAssociatedShifts();
            for (Shift shift : shifts) {
                if (shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal().equals(
                        executionPeriod.getIdInternal())) {
                    final List<Attends> attends = shift.getDisciplinaExecucao().getAttends();
                    for (final Attends attend : attends) {
                        final Student otherStudent = attend.getAluno();
                        if (student.equals(otherStudent)) {
                            result.add(schoolClass);
                        }
                    }
                }
            }
        }
        
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addEqualTo("associatedShifts.disciplinaExecucao.attendingStudents.idInternal",
         * student .getIdInternal());
         * criteria.addEqualTo("associatedShifts.disciplinaExecucao.executionPeriod.idInternal",
         * executionPeriod.getIdInternal()); return queryList(SchoolClass.class,
         * criteria, true);
         */
    }
}