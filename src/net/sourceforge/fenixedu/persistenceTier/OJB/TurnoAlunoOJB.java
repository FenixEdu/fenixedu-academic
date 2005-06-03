/*
 * TurnoAlunoOJB.java Created on 21 de Outubro de 2002, 19:03
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.ojb.broker.query.Criteria;

public class TurnoAlunoOJB extends PersistentObjectOJB implements ITurnoAlunoPersistente {

    public List readByStudentAndExecutionPeriod(Integer studentOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudent", studentOID);
        crit.addEqualTo("shift.disciplinaExecucao.keyExecutionPeriod", executionPeriodOID);

        return queryList(ShiftStudent.class, crit);
    }

    public List readByStudentAndExecutionCourse(Integer studentOID, Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudent", studentOID);
        crit.addEqualTo("shift.chaveDisciplinaExecucao", executionCourseOID);

        return queryList(ShiftStudent.class, crit);
    }

    public IShiftStudent readByTurnoAndAluno(Integer turnoOID, Integer alunoOID) throws ExcepcaoPersistencia {
        IShift turno = (IShift) readByOID(Shift.class, turnoOID);
        IStudent aluno = (IStudent) readByOID(Student.class, alunoOID);
        
        Criteria criteria = new Criteria();

        criteria.addEqualTo("student.number", aluno.getNumber());
        criteria.addEqualTo("student.degreeType", aluno.getDegreeType());
        criteria.addEqualTo("shift.nome", turno.getNome());
        criteria.addEqualTo("shift.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.name", turno
                .getDisciplinaExecucao().getExecutionPeriod().getName());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.executionYear.year", turno
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());

        return (IShiftStudent) queryObject(ShiftStudent.class, criteria);

    }

    /**
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByTurno(Dominio.IShift)
     */
    public List readStudentShiftByShift(Integer shiftOID) throws ExcepcaoPersistencia {
        IShift shift = (IShift) readByOID(Shift.class, shiftOID);
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.nome", shift.getNome());
        criteria.addEqualTo("shift.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.name", shift
                .getDisciplinaExecucao().getExecutionPeriod().getName());
        criteria.addEqualTo("shift.disciplinaExecucao.executionPeriod.executionYear.year", shift
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());

        return queryList(ShiftStudent.class, criteria);
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
        lockRead(studentList);
        return studentList;

    }

    // by gedl AT rnl DOT ist DOT utl DOT pt, September the 16th, 2003
    public List readByShiftID(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("key_shift", id);
        return queryList(ShiftStudent.class, criteria);
    }

    public List readByStudent(Integer studentOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentOID);
        return queryList(ShiftStudent.class, criteria);
    }

    public IShiftStudent readByStudentAndExecutionCourseAndLessonType(Integer studentOID,
            Integer executionCourseOID, ShiftType lessonType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("student.idInternal", studentOID);
        criteria.addEqualTo("shift.tipo", lessonType);
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourseOID);

        return (IShiftStudent) queryObject(ShiftStudent.class, criteria);
    }

    public int readNumberOfStudentsByShift(Integer shiftOID) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shiftOID);
        return count(ShiftStudent.class, criteria);
    }
}