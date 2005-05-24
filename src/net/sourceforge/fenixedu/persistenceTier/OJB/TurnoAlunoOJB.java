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

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.ojb.broker.query.Criteria;

public class TurnoAlunoOJB extends PersistentObjectOJB implements ITurnoAlunoPersistente {

    public List readByStudentAndExecutionPeriod(IStudent student, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudent", student.getIdInternal());
        crit.addEqualTo("shift.disciplinaExecucao.keyExecutionPeriod", executionPeriod.getIdInternal());

        return queryList(ShiftStudent.class, crit);
    }

    public List readByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyStudent", student.getIdInternal());
        crit.addEqualTo("shift.chaveDisciplinaExecucao", executionCourse.getIdInternal());

        return queryList(ShiftStudent.class, crit);
    }

    public IShiftStudent readByTurnoAndAluno(IShift turno, IStudent aluno) throws ExcepcaoPersistencia {
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

    public void delete(IShiftStudent turnoAluno) throws ExcepcaoPersistencia {
        super.delete(turnoAluno);
    }

    /**
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByTurno(Dominio.IShift)
     */
    public List readStudentShiftByShift(IShift shift) throws ExcepcaoPersistencia {
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
    public List readByShift(IShift shift) throws ExcepcaoPersistencia {
        List result = readStudentShiftByShift(shift);

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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoAlunoPersistente#readByStudent(Dominio.IStudent)
     */
    public List readByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return queryList(ShiftStudent.class, criteria);
    }

    public IShiftStudent readByStudentAndExecutionCourseAndLessonType(IStudent student,
            IExecutionCourse executionCourse, TipoAula lessonType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("student.idInternal", student.getIdInternal());
        criteria.addEqualTo("shift.tipo", lessonType);
        criteria.addEqualTo("shift.disciplinaExecucao.idInternal", executionCourse.getIdInternal());

        return (IShiftStudent) queryObject(ShiftStudent.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoAlunoPersistente#readNumberOfStudentsByShift(Dominio.IShift)
     */
    public int readNumberOfStudentsByShift(IShift shift) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());
        return count(ShiftStudent.class, criteria);
    }
}