/*
 * AulaOJB.java
 *
 * Created on 18 de Outubro de 2002, 00:34
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.ShiftStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

public class AulaOJB extends ObjectFenixOJB implements IAulaPersistente {

    public ILesson readByDiaSemanaAndInicioAndFimAndSala(DiaSemana diaSemana, Calendar inicio,
            Calendar fim, IRoom sala, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        try {
            ILesson aula = null;
            String oqlQuery = "select diasemanainiciofimsala from " + Lesson.class.getName();
            oqlQuery += " where diaSemana = $1";
            oqlQuery += " and inicio = $2";
            oqlQuery += " and fim = $3";
            oqlQuery += " and sala.nome = $4";
            oqlQuery += " and disciplinaExecucao.executionPeriod.name = $5";
            oqlQuery += " and disciplinaExecucao.executionPeriod.executionYear.year = $6";
            query.create(oqlQuery);
            query.bind(diaSemana);
            query.bind(inicio);
            query.bind(fim);
            query.bind(sala.getNome());
            query.bind(executionPeriod.getName());
            query.bind(executionPeriod.getExecutionYear().getYear());
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                aula = (ILesson) result.get(0);
            return aula;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Lesson.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void delete(ILesson aula) throws ExcepcaoPersistencia {
        if (aula != null) {
            /*
             * try { ITurnoAula turnoAula = null; TurnoAulaOJB turnoAulaOJB =
             * new TurnoAulaOJB(); String oqlQuery = "select all from " +
             * TurnoAula.class.getName(); oqlQuery += " where aula.diaSemana =
             * $1" + " and aula.inicio = $2" + " and aula.fim = $3" + " and
             * aula.sala.nome= $4"; query.create(oqlQuery);
             * query.bind(aula.getDiaSemana()); query.bind(aula.getInicio());
             * query.bind(aula.getFim()); query.bind(aula.getSala().getNome());
             * List result = (List) query.execute(); lockRead(result); Iterator
             * iterador = result.iterator(); while (iterador.hasNext()) {
             * turnoAula = (ITurnoAula) iterador.next();
             * turnoAulaOJB.delete(turnoAula); } } catch (QueryException ex) {
             * throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex); }
             */
            super.delete(aula);
        }
    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Lesson.class.getName();
        super.deleteAll(oqlQuery);
    }

    /*
     * public List readByExecutionCourse(IExecutionCourse executionCourse)
     * throws ExcepcaoPersistencia { if (executionCourse == null) return new
     * ArrayList(); try { String oqlQuery = "select aulas from " +
     * Lesson.class.getName(); oqlQuery += " where disciplinaExecucao.sigla = $1";
     * oqlQuery += " and disciplinaExecucao.executionPeriod.name = $2"; oqlQuery += "
     * and disciplinaExecucao.executionPeriod.executionYear.year = $3";
     * 
     * query.create(oqlQuery);
     * 
     * query.bind(executionCourse.getSigla());
     * query.bind(executionCourse.getExecutionPeriod().getName());
     * query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
     * 
     * List result = (List) query.execute(); lockRead(result); return result; }
     * catch (QueryException ex) { throw new
     * ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex); } }
     * 
     * public List readByExecutionCourseAndLessonType(IExecutionCourse
     * executionCourse, TipoAula lessonType) throws ExcepcaoPersistencia { try { //
     * List aulas = new ArrayList(); String oqlQuery = "select aulas from " +
     * Lesson.class.getName() + " where tipo = $1" + " and
     * disciplinaExecucao.sigla = $2" + " and
     * disciplinaExecucao.executionPeriod.name = $3" + " and
     * disciplinaExecucao.executionPeriod.executionYear.year = $4";
     * query.create(oqlQuery);
     * 
     * query.bind(lessonType);
     * 
     * query.bind(executionCourse.getSigla());
     * 
     * query.bind(executionCourse.getExecutionPeriod().getName());
     * query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
     * 
     * List result = (List) query.execute(); lockRead(result); return result; }
     * catch (QueryException ex) { throw new
     * ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex); } }
     *  
     */public List readByRoomAndExecutionPeriod(IRoom room, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        /*
         * try { String oqlQuery = "select aulas from " + Lesson.class.getName();
         * oqlQuery += " where sala.nome = $1" + " and
         * disciplinaExecucao.executionPeriod.name = $2" + " and
         * disciplinaExecucao.executionPeriod.executionYear.year = $3";
         * 
         * query.create(oqlQuery); query.bind(room.getNome());
         * query.bind(executionPeriod.getName());
         * query.bind(executionPeriod.getExecutionYear().getYear());
         * 
         * List result = (List) query.execute(); lockRead(result); return
         * result; } catch (QueryException ex) { throw new
         * ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex); }
         */
        Criteria crit = new Criteria();
        crit.addEqualTo("keyRoom", room.getIdInternal());
        List roomsOccupation = queryList(RoomOccupation.class, crit);
        ArrayList lessonList = new ArrayList();
        for (int i = 0; i < roomsOccupation.size(); i++) {
            crit = new Criteria();
            IRoomOccupation roomOccupation = (IRoomOccupation) roomsOccupation.get(i);
            crit.addEqualTo("keyRoomOccupation", roomOccupation.getIdInternal());
            crit.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
            Lesson lesson = (Lesson) queryObject(Lesson.class, crit);
            if (lesson != null) {
                lessonList.add(lesson);
            }
        }
        return lessonList;
    }

    public List readByRoomNamesAndExecutionPeriod(List roomNames, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addIn("sala.nome", roomNames);
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.executionYear.year", executionPeriod
                .getExecutionYear().getYear());

        return queryList(Lesson.class, criteria);
    }

    /**
     * 
     * @param lesson
     * @return @throws
     *         ExcepcaoPersistencia
     * @deprecated This Method does not meet it's requirements use
     *             readLessonsInBroadPeriodInAnyRoom instead.
     */
    public List readLessonsInPeriod(ILesson lesson) throws ExcepcaoPersistencia {
        try {
            List lessonList = null;
            String oqlQuery = "select aulas from " + Lesson.class.getName();
            oqlQuery += " where inicio >= $1 " + "and fim <= $2 " + "and diaSemana = $3";
            // 
            //oqlQuery += " and disciplinaExecucao.semestre = $2";
            query.create(oqlQuery);
            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());
            //query.bind(semestre);
            lessonList = (List) query.execute();
            lockRead(lessonList);
            return lessonList;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readLessonsInBroadPeriod(ILesson newLesson, ILesson oldLesson,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        try {
            List lessonList = null;
            String oqlQuery = "select aulas from " + Lesson.class.getName();
            oqlQuery += " where (( inicio >$1 " + "and inicio <$2 " + "and diaSemana = $3 "
                    + "and sala.nome = $4 ) " + "or ( fim > $5 " + "and fim < $6 "
                    + "and diaSemana = $7 " + "and sala.nome = $8 )" + "or ( inicio = $9 "
                    + "and fim = $10 " + "and diaSemana = $11 " + "and sala.nome = $12 )"
                    + "or ( inicio <= $13 " + "and fim >= $14" + "and diaSemana = $15 "
                    + "and sala.nome = $16 )" + "or ( inicio = $17 " + "and fim <= $18 "
                    + "and diaSemana = $19 " + "and sala.nome = $20 ))"
                    + "and disciplinaExecucao.executionPeriod.name = $21"
                    + "and disciplinaExecucao.executionPeriod.executionYear.year = $22";

            query.create(oqlQuery);
            query.bind(newLesson.getInicio());
            query.bind(newLesson.getFim());
            query.bind(newLesson.getDiaSemana());
            query.bind(newLesson.getRoomOccupation().getRoom().getNome());
            query.bind(newLesson.getInicio());
            query.bind(newLesson.getFim());
            query.bind(newLesson.getDiaSemana());
            query.bind(newLesson.getRoomOccupation().getRoom().getNome());
            query.bind(newLesson.getInicio());
            query.bind(newLesson.getFim());
            query.bind(newLesson.getDiaSemana());
            query.bind(newLesson.getRoomOccupation().getRoom().getNome());
            query.bind(newLesson.getInicio());
            query.bind(newLesson.getFim());
            query.bind(newLesson.getDiaSemana());
            query.bind(newLesson.getRoomOccupation().getRoom().getNome());
            query.bind(newLesson.getInicio());
            query.bind(newLesson.getFim());
            query.bind(newLesson.getDiaSemana());
            query.bind(newLesson.getRoomOccupation().getRoom().getNome());
            query.bind(executionPeriod.getName());
            query.bind(executionPeriod.getExecutionYear().getYear());

            lessonList = (List) query.execute();
            lockRead(lessonList);

            // Remove the Lesson that is being edited from the list
            // of intercepting lessons.
            // TODO : this aspect is not yet contemplated in the tests.
            if (oldLesson != null && oldLesson instanceof Lesson)
                for (int ipto = 0; ipto < lessonList.size(); ipto++)
                    if (((Lesson) lessonList.get(ipto)).getIdInternal().equals(
                            ((Lesson) oldLesson).getIdInternal()))
                        lessonList.remove(ipto);

            return lessonList;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readLessonsInBroadPeriodInAnyRoom(ILesson lesson, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        try {
            List lessonList = null;
            String oqlQuery = "select aulas from " + Lesson.class.getName();
            oqlQuery += " where (( inicio >$1 " + "and inicio <$2 " + "and diaSemana = $3 ) "
                    + "or ( fim > $4 " + "and fim < $5 " + "and diaSemana = $6 ) " + "or ( inicio = $7 "
                    + "and fim = $8 " + "and diaSemana = $9 ) " + "or ( inicio <= $10 "
                    + "and fim >= $11 " + "and diaSemana = $12 ) " + "or ( inicio = $13 "
                    + "and fim <= $14 " + "and diaSemana = $15 ))"
                    + "and disciplinaExecucao.executionPeriod.name = $16"
                    + "and disciplinaExecucao.executionPeriod.executionYear.year = $17";

            query.create(oqlQuery);
            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());

            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());

            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());

            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());

            query.bind(lesson.getInicio());
            query.bind(lesson.getFim());
            query.bind(lesson.getDiaSemana());

            query.bind(executionPeriod.getName());
            query.bind(executionPeriod.getExecutionYear().getYear());

            lessonList = (List) query.execute();
            lockRead(lessonList);

            return lessonList;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("aluno.person.username", username);
        List studentShifts = queryList(ShiftStudent.class, crit);
        if (studentShifts == null) {
            return null;
        }
        List lessons = new ArrayList();
        for (int i = 0; i < studentShifts.size(); i++) {
            IShift shift = ((ShiftStudent) studentShifts.get(i)).getShift();
            List auxLessons = shift.getAssociatedLessons();
            if (auxLessons != null) {
                lessons.addAll(auxLessons);
            }
        }
        return lessons;
    }

    public List readLessonsByShift(IShift shift) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyShift", shift.getIdInternal());
        List lessons = queryList(Lesson.class, crit);
        return lessons;
    }

    public List readLessonsByShiftAndLessonType(IShift shift, TipoAula lessonType)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("keyShift", shift.getIdInternal());
        crit.addEqualTo("tipo", lessonType);
        List lessons = queryList(Lesson.class, crit);
        return lessons;
    }
}