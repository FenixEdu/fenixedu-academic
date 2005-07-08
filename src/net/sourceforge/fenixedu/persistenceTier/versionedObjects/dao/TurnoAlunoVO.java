/*
 * TurnoAlunoOJB.java Created on 21 de Outubro de 2002, 19:03
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author Pedro Santos & Rita Carvalho
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class TurnoAlunoVO extends VersionedObjectsBase implements ITurnoAlunoPersistente {

    public List readByStudentAndExecutionPeriod(Integer studentOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia {

        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        List<IShiftStudent> shiftStudents = student.getShiftStudents();

        List<IShiftStudent> result = new ArrayList();

        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getKeyStudent().equals(studentOID)
                    && shiftStudent.getShift().getDisciplinaExecucao().getKeyExecutionPeriod().equals(
                            executionPeriodOID)) {
                result.add(shiftStudent);

            }
        }
        return result;

        /*
         * Criteria crit = new Criteria(); crit.addEqualTo("keyStudent",
         * student.getIdInternal());
         * crit.addEqualTo("shift.disciplinaExecucao.keyExecutionPeriod",
         * executionPeriod.getIdInternal()); return
         * queryList(ShiftStudent.class, crit);
         */
    }

    public List readByStudentAndExecutionCourse(Integer studentOID, Integer executionCourseOID)
            throws ExcepcaoPersistencia {

        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        List<IShiftStudent> shiftStudents = student.getShiftStudents();

        List<IShiftStudent> result = new ArrayList();

        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getKeyStudent().equals(studentOID)
                    && shiftStudent.getShift().getChaveDisciplinaExecucao().equals(executionCourseOID)) {
                result.add(shiftStudent);

            }
        }
        return result;

        /*
         * Criteria crit = new Criteria(); crit.addEqualTo("keyStudent",
         * student.getIdInternal());
         * crit.addEqualTo("shift.chaveDisciplinaExecucao",
         * executionCourse.getIdInternal());
         * 
         * return queryList(ShiftStudent.class, crit);
         */
    }

    public IShiftStudent readByTurnoAndAluno(Integer turnoOID, Integer alunoOID)
            throws ExcepcaoPersistencia {
        IShift shift = (IShift) readByOID(Shift.class, turnoOID);
        IStudent student = (IStudent) readByOID(Student.class, alunoOID);

        List<IShiftStudent> shiftStudents = student.getShiftStudents();
        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getStudent().getNumber().equals(student.getNumber())
                    && shiftStudent.getStudent().getDegreeType().equals(student.getDegreeType())
                    && shiftStudent.getShift().getNome().equals(shift.getNome())
                    && shiftStudent.getShift().getDisciplinaExecucao().getSigla().equals(
                            shift.getDisciplinaExecucao().getSigla())
                    && shiftStudent.getShift().getDisciplinaExecucao().getExecutionPeriod().getName()
                            .equals(shift.getDisciplinaExecucao().getExecutionPeriod().getName())
                    && shiftStudent.getShift().getDisciplinaExecucao().getExecutionPeriod()
                            .getExecutionYear().getYear().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod()
                                            .getExecutionYear().getYear())) {
                return shiftStudent;
            }
        }
        return null;
    }

    public List readStudentShiftByShift(Integer shiftOID) throws ExcepcaoPersistencia {

        IShift shift = (IShift) readByOID(Shift.class, shiftOID);
        List<IShiftStudent> shiftStudents = shift.getStudentShifts();

        List result = new ArrayList();

        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getShift().getNome().equals(shift.getNome())
                    && shiftStudent.getShift().getDisciplinaExecucao().getSigla().equals(
                            shift.getDisciplinaExecucao().getSigla())
                    && shiftStudent.getShift().getDisciplinaExecucao().getExecutionPeriod().getName()
                            .equals(shift.getDisciplinaExecucao().getExecutionPeriod().getName())
                    && shiftStudent.getShift().getDisciplinaExecucao().getExecutionPeriod()
                            .getExecutionYear().getYear().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod()
                                            .getExecutionYear().getYear())) {
                result.add(shiftStudent);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria.addEqualTo("shift.nome",
         * shift.getNome());
         * criteria.addEqualTo("shift.disciplinaExecucao.sigla",
         * shift.getDisciplinaExecucao().getSigla());
         * criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.name",
         * shift .getDisciplinaExecucao().getExecutionPeriod().getName());
         * criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.executionYear.year",
         * shift
         * .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
         * 
         * return queryList(ShiftStudent.class, criteria);
         */
    }

    /**
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByTurno(Dominio.IShift)
     */
    public List readByShift(Integer shiftOID) throws ExcepcaoPersistencia {
        List result = readStudentShiftByShift(shiftOID);

        List studentList = new ArrayList();

        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            IShiftStudent shiftStudent = (IShiftStudent) iterator.next();
            studentList.add(shiftStudent.getStudent());
        }
        // lockRead(studentList);
        return studentList;

    }

    // by gedl AT rnl DOT ist DOT utl DOT pt, September the 16th, 2003
    public List readByShiftID(Integer id) throws ExcepcaoPersistencia {
        List<IShiftStudent> shiftStudents = (List<IShiftStudent>) readAll(ShiftStudent.class);
        List<IShiftStudent> result = new ArrayList();
        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getKeyShift().equals(id)) {
                result.add(shiftStudent);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria.addEqualTo("key_shift",
         * id); return queryList(ShiftStudent.class, criteria);
         */
    }

    public List readByStudent(Integer studentOID) throws ExcepcaoPersistencia {
        List<IShiftStudent> shiftStudents = (List<IShiftStudent>) readAll(ShiftStudent.class);
        List<IShiftStudent> result = new ArrayList();
        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getKeyStudent().equals(studentOID)) {
                result.add(shiftStudent);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria.addEqualTo("keyStudent",
         * student.getIdInternal()); return queryList(ShiftStudent.class,
         * criteria);
         */
    }

    public IShiftStudent readByStudentAndExecutionCourseAndLessonType(Integer studentOID,
            Integer executionCourseOID, ShiftType lessonType) throws ExcepcaoPersistencia {
        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        List<IShiftStudent> shiftStudents = student.getShiftStudents();
        for (IShiftStudent shiftStudent : shiftStudents) {
            if (shiftStudent.getShift().getTipo().equals(lessonType)
                    && shiftStudent.getShift().getDisciplinaExecucao().getIdInternal().equals(
                            executionCourseOID)) {
                return shiftStudent;
            }
        }
        return null;

        /*Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        criteria.addEqualTo("shift.tipo", lessonType);
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        return (IShiftStudent) queryObject(ShiftStudent.class, criteria);*/
    }

    public int readNumberOfStudentsByShift(Integer shiftOID) throws ExcepcaoPersistencia {
        IShift shift = (IShift) readByOID(Shift.class, shiftOID);
        List<IShiftStudent> shiftStudents = shift.getStudentShifts();
        return shiftStudents.size();
        
        /*Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());
        return count(ShiftStudent.class, criteria);*/
    }
}