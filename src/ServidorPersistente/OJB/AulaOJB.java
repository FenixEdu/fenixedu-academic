/*
 * AulaOJB.java
 * 
 * Created on 18 de Outubro de 2002, 00:34
 */

package ServidorPersistente.OJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurnoAula;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;
import Util.TipoAula;

public class AulaOJB extends ObjectFenixOJB implements IAulaPersistente
{

    public IAula readByDiaSemanaAndInicioAndFimAndSala(
        DiaSemana diaSemana,
        Calendar inicio,
        Calendar fim,
        ISala sala,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("diaSemana", diaSemana);
        crit.addEqualTo("inicio", inicio);
        crit.addEqualTo("fim", fim);
        crit.addEqualTo("sala.nome", sala.getNome());
        crit.addEqualTo("disciplinaExecucao.executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        return (IAula) queryObject(Aula.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        return queryList(Aula.class, crit);

    }

    public void lockWrite(IAula lessonToWrite) throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IAula lessonFromDB = null;
        if (lessonToWrite == null)
            // Should we throw an exception saying nothing to write or
            // something of the sort?
            // By default, if OJB received a null object it would complain.
            return;

        // read lesson
        lessonFromDB =
            this.readByDiaSemanaAndInicioAndFimAndSala(
                lessonToWrite.getDiaSemana(),
                lessonToWrite.getInicio(),
                lessonToWrite.getFim(),
                lessonToWrite.getSala(),
                lessonToWrite.getDisciplinaExecucao().getExecutionPeriod());

        // if (lesson not in database) then write it
        if (lessonFromDB == null)
            super.lockWrite(lessonToWrite);
        // else if (lesson is mapped to the database then write any existing
        // changes)
        else if (
            (lessonToWrite instanceof Aula)
                && ((Aula) lessonFromDB).getIdInternal().equals(((Aula) lessonToWrite).getIdInternal()))
        {

            lessonFromDB.setDisciplinaExecucao(lessonToWrite.getDisciplinaExecucao());
            lessonFromDB.setTipo(lessonToWrite.getTipo());
            // No need to werite it because it is already mapped.
            //super.lockWrite(lessonToWrite);
            // else throw an AlreadyExists exception.
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(IAula aula) throws ExcepcaoPersistencia
    {
        if (aula != null)
        {

            ITurnoAula turnoAula = null;
            TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
            Criteria crit = new Criteria();
            crit.addEqualTo("aula.diaSemana", aula.getDiaSemana());
            crit.addEqualTo("aula.inicio", aula.getInicio());
            crit.addEqualTo("aula.fim", aula.getFim());
            crit.addEqualTo("aula.sala.nome", aula.getSala().getNome());

            List result = queryList(TurnoAula.class, crit);

            Iterator iterador = result.iterator();
            while (iterador.hasNext())
            {
                turnoAula = (ITurnoAula) iterador.next();
                turnoAulaOJB.delete(turnoAula);
            }

            super.delete(aula);
        }
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        if (executionCourse == null)
        {
            return new ArrayList();
        }
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());

        return queryList(Aula.class, crit);

    }

    public List readByExecutionCourseAndLessonType(
        IExecutionCourse executionCourse,
        TipoAula lessonType)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("tipo", lessonType);
        crit.addEqualTo("disciplinaExecucao.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        return queryList(Aula.class, crit);

    }

    public List readByRoomAndExecutionPeriod(ISala room, IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("sala.nome", room.getNome());
        crit.addEqualTo("disciplinaExecucao.executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());
        return queryList(Aula.class, crit);

    }

    public List readLessonsInBroadPeriod(
        IAula newLesson,
        IAula oldLesson,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();

        List lessonList = null;

        Criteria crit1 = new Criteria();
        crit1.addGreaterThan("inicio", newLesson.getInicio());
        crit1.addLessThan("inicio", newLesson.getFim());
        crit1.addEqualTo("diaSemana", newLesson.getDiaSemana());
        crit1.addEqualTo("sala.nome", newLesson.getSala().getNome());
        crit.addOrCriteria(crit1);

        Criteria crit2 = new Criteria();
        crit2.addGreaterThan("fim", newLesson.getInicio());
        crit2.addLessThan("fim", newLesson.getFim());
        crit2.addEqualTo("diaSemana", newLesson.getDiaSemana());
        crit2.addEqualTo("sala.nome", newLesson.getSala().getNome());
        crit.addOrCriteria(crit2);

        Criteria crit3 = new Criteria();
        crit3.addEqualTo("inicio", newLesson.getInicio());
        crit3.addEqualTo("fim", newLesson.getFim());
        crit3.addEqualTo("diaSemana", newLesson.getDiaSemana());
        crit3.addEqualTo("sala.nome", newLesson.getSala().getNome());
        crit.addOrCriteria(crit3);

        Criteria crit4 = new Criteria();
        crit4.addLessOrEqualThan("inicio", newLesson.getInicio());
        crit4.addGreaterOrEqualThan("fim", newLesson.getFim());
        crit4.addEqualTo("diaSemana", newLesson.getDiaSemana());
        crit4.addEqualTo("sala.nome", newLesson.getSala().getNome());
        crit.addOrCriteria(crit4);

        Criteria crit5 = new Criteria();
        crit5.addEqualTo("inicio", newLesson.getInicio());
        crit5.addLessOrEqualThan("fim", newLesson.getFim());
        crit5.addEqualTo("diaSemana", newLesson.getDiaSemana());
        crit5.addEqualTo("sala.nome", newLesson.getSala().getNome());
        crit.addOrCriteria(crit5);

        crit.addEqualTo("disciplinaExecucao.executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());

        lessonList = queryList(Aula.class, crit);

        // Remove the Lesson that is being edited from the list
        // of intercepting lessons.
        // TODO : this aspect is not yet contemplated in the tests.
        if (oldLesson != null && oldLesson instanceof Aula)
        {
            for (int ipto = 0; ipto < lessonList.size(); ipto++)
            {
                if (((Aula) lessonList.get(ipto))
                    .getIdInternal()
                    .equals(((Aula) oldLesson).getIdInternal()))
                {
                    lessonList.remove(ipto);
                }
            }
        }
        return lessonList;
    }

    public List readLessonsInBroadPeriodInAnyRoom(IAula lesson, IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        List lessonList = null;

        Criteria crit2 = new Criteria();
        crit2.addGreaterThan("inicio", lesson.getInicio());
        crit2.addLessThan("inicio", lesson.getFim());
        crit2.addEqualTo("diaSemana", lesson.getDiaSemana());

        Criteria crit3 = new Criteria();
        crit3.addGreaterThan("fim", lesson.getInicio());
        crit3.addLessThan("fim", lesson.getFim());
        crit3.addEqualTo("diaSemana", lesson.getDiaSemana());

        Criteria crit4 = new Criteria();
        crit4.addEqualTo("inicio", lesson.getInicio());
        crit4.addEqualTo("fim", lesson.getFim());
        crit4.addEqualTo("diaSemana", lesson.getDiaSemana());

        Criteria crit5 = new Criteria();
        crit5.addLessOrEqualThan("inicio", lesson.getInicio());
        crit5.addGreaterOrEqualThan("fim", lesson.getFim());
        crit5.addEqualTo("diaSemana", lesson.getDiaSemana());

        Criteria crit6 = new Criteria();
        crit6.addEqualTo("inicio", lesson.getInicio());
        crit6.addLessOrEqualThan("fim", lesson.getFim());
        crit6.addEqualTo("diaSemana", lesson.getDiaSemana());

        Criteria crit1 = new Criteria();
        crit1.addEqualTo("disciplinaExecucao.executionPeriod.name", executionPeriod.getName());
        crit1.addEqualTo(
            "disciplinaExecucao.executionPeriod.executionYear.year",
            executionPeriod.getExecutionYear().getYear());

        Criteria crit = new Criteria();
        crit.addAndCriteria(crit1);
        crit.addOrCriteria(crit2);
        crit.addOrCriteria(crit3);
        crit.addOrCriteria(crit4);
        crit.addOrCriteria(crit5);
        crit.addOrCriteria(crit6);
        lessonList = queryList(Aula.class, crit);

        return lessonList;
    }

}
