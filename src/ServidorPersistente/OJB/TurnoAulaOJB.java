/*
 * TurnoAulaOJB.java
 * 
 * Created on 22 de Outubro de 2002, 9:18
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IAula;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.ShiftStudent;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;

public class TurnoAulaOJB extends ObjectFenixOJB implements ITurnoAulaPersistente
{

    public ITurnoAula readByShiftAndLesson(ITurno shift, IAula lesson) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("turno.nome", shift.getNome());
        criteria.addEqualTo(
            "turno.disciplinaExecucao.executionPeriod.name",
            shift.getDisciplinaExecucao().getExecutionPeriod().getName());
        criteria.addEqualTo(
            "turno.disciplinaExecucao.executionPeriod.executionYear.year",
            shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        criteria.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
        criteria.addEqualTo("aula.diaSemana", lesson.getDiaSemana());
        criteria.addEqualTo("aula.inicio", lesson.getInicio());
        criteria.addEqualTo("aula.fim", lesson.getFim());
        criteria.addEqualTo("aula.tipo", lesson.getTipo());
        criteria.addEqualTo("aula.sala.nome", lesson.getSala().getNome());

        List result = queryList(TurnoAula.class, criteria);
        if (result.size() != 0)
            return (ITurnoAula) result.get(0);
        return null;
    }

    public void lockWrite(ITurnoAula shiftLessonToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ITurnoAula shiftLessonFromDB = null;

        // If there is nothing to write, simply return.
        if (shiftLessonToWrite == null)
            return;

        // Read shiftLesson from database.
        shiftLessonFromDB =
            this.readByShiftAndLesson(shiftLessonToWrite.getTurno(), shiftLessonToWrite.getAula());

        // If shiftLesson is not in database, then write it.
        if (shiftLessonFromDB == null)
            super.lockWrite(shiftLessonToWrite);
        // else If the shiftLesson is mapped to the database, then write any
        // existing changes.
        else if (
            (shiftLessonToWrite instanceof TurnoAula)
                && ((TurnoAula) shiftLessonFromDB).getIdInternal().equals(
                    ((TurnoAula) shiftLessonToWrite).getIdInternal()))
        {
            super.lockWrite(shiftLessonToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(ITurnoAula turnoAula) throws ExcepcaoPersistencia
    {
        super.delete(turnoAula);
    }

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.person.username", username);
        List studentShifts = queryList(ShiftStudent.class, crit);
        if (studentShifts == null)
        {
            return null;
        }
        List lessons = new ArrayList();
        for (int i = 0; i < studentShifts.size(); i++)
        {
            Criteria crit2 = new Criteria();
            crit2.addEqualTo("turno.nome", ((ShiftStudent) studentShifts.get(i)).getShift().getNome());
            List auxLessons = queryList(TurnoAula.class, crit2);
            if (auxLessons != null)
            {
                lessons.addAll(auxLessons);
            }
        }
        return lessons;

    }

    /**
	 * @see ServidorPersistente.ITurnoAulaPersistente#readByShift(ITurno)
	 */
    public List readByShift(ITurno shift) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.idInternal", shift.getIdInternal());
        List shiftLessons = queryList(TurnoAula.class, crit);
        if (shiftLessons == null)
        {
            return null;
        }

        List lessons = new ArrayList();
        for (int j = 0; j < shiftLessons.size(); j++)
            lessons.add(((TurnoAula) shiftLessons.get(j)).getAula());
        return lessons;

    }
    /**
	 * @see ServidorPersistente.ITurnoAulaPersistente#delete(Dominio.ITurno,
	 *      Util.DiaSemana, java.util.Calendar, java.util.Calendar,
	 *      Dominio.ISala)
	 */
    public void delete(ITurno shift, DiaSemana diaSemana, Calendar inicio, Calendar fim, ISala sala)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.nome", shift.getNome());
        crit.addEqualTo(
            "turno.disciplinaExecucao.executionPeriod.name",
            shift.getDisciplinaExecucao().getExecutionPeriod().getName());
        crit.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
        crit.addEqualTo(
            "turno.disciplinaExecucao.executionPeriod.executionYear.year",
            shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        crit.addEqualTo("aula.diaSemana", diaSemana);
        crit.addEqualTo("aula.inicio", inicio);
        crit.addEqualTo("aula.fim", fim);
        crit.addEqualTo("aula.sala.nome", sala.getNome());

        List result = queryList(TurnoAula.class, crit);
        if (!result.isEmpty())
            delete((ITurnoAula) result.get(0));

    }
    /**
	 * @see ServidorPersistente.ITurnoAulaPersistente#readLessonsByShift(Dominio.ITurno)
	 */
    public List readLessonsByShift(ITurno shift) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        if (shift.getIdInternal() != null && shift.getIdInternal().intValue() != 0)
        {
            crit.addEqualTo("turno.idInternal", shift.getIdInternal());
        }
        else
        {
            crit.addEqualTo("turno.nome", shift.getNome());
            crit.addEqualTo(
                "turno.disciplinaExecucao.executionPeriod.name",
                shift.getDisciplinaExecucao().getExecutionPeriod().getName());
            crit.addEqualTo(
                "turno.disciplinaExecucao.executionPeriod.executionYear.year",
                shift.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
            crit.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());

        }

        List shiftLessons = queryList(TurnoAula.class, crit);

        return shiftLessons;

    }
}
