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
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
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

        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<ISchoolClass> schoolClasses = executionDegree.getSchoolClasses();

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        List<ISchoolClass> result = new ArrayList();

        for (ISchoolClass schoolClass : schoolClasses) {
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

    public ISchoolClass readByNameAndExecutionDegreeAndExecutionPeriod(String className,
            Integer executionDegreeOID, Integer executionPeriodOID) throws ExcepcaoPersistencia {

        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<ISchoolClass> schoolClasses = executionDegree.getSchoolClasses();

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        for (ISchoolClass schoolClass : schoolClasses) {
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
         * return (ISchoolClass) queryObject(SchoolClass.class, criteria);
         */

    }

    public List readByExecutionPeriod(Integer executionPeriodOID) throws ExcepcaoPersistencia {
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        List<ISchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        List<ISchoolClass> result = new ArrayList();
        for (ISchoolClass schoolClass : schoolClasses) {
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
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        List<ISchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<ISchoolClass> result = new ArrayList();
        for (ISchoolClass schoolClass : schoolClasses) {
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

        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                execucaoOID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        List<ISchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<ISchoolClass> result = new ArrayList();
        for (ISchoolClass schoolClass : schoolClasses) {
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
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                execucaoOID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IDegree degree = (IDegree) readByOID(Degree.class, degreeOID);
        List<ISchoolClass> schoolClasses = executionDegree.getSchoolClasses();
        List<ISchoolClass> result = new ArrayList();
        for (ISchoolClass schoolClass : schoolClasses) {
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
        IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        List<IShift> associatedShifts = executionCourse.getAssociatedShifts();
        List<ISchoolClass> result = new ArrayList();
        for (IShift shift : associatedShifts) {
            List<ISchoolClass> schoolClasses = shift.getAssociatedClasses();
            for (ISchoolClass schoolClass : schoolClasses) {
                List<IShift> shifts = schoolClass.getAssociatedShifts();
                for (IShift turno : shifts) {
                    if (turno.getDisciplinaExecucao().getIdInternal().equals(
                            executionCourse.getIdInternal())) {
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
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        List<ISchoolClass> result = new ArrayList();
        List<ISchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        for (ISchoolClass schoolClass : schoolClasses) {
            List<IShift> shifts = schoolClass.getAssociatedShifts();
            for (IShift shift : shifts) {
                if (shift.getDisciplinaExecucao().getExecutionPeriod().getIdInternal().equals(
                        executionPeriod.getIdInternal())) {
                    List<IStudent> students = shift.getDisciplinaExecucao().getAttendingStudents();
                    if (students.contains(student)) {
                        result.add(schoolClass);
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