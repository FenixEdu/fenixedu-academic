/*
 * EditarAula.java
 *
 * Created on 27 de Outubro de 2002, 19:05
 */
package ServidorAplicacao.Servico.sop;

/**
 * Servi�o EditarAula.
 * 
 * @author tfc130
 */
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoShift;
import DataBeans.InfoShiftServiceResult;
import DataBeans.util.Cloner;
import Dominio.Lesson;
import Dominio.ILesson;
import Dominio.IExecutionPeriod;
import Dominio.IRoomOccupation;
import Dominio.IRoom;
import Dominio.IShift;
import Dominio.RoomOccupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class EditLesson implements IServico {
    private static EditLesson _servico = new EditLesson();

    /**
     * The singleton access method of this class.
     */
    public static EditLesson getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private EditLesson() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "EditLesson";
    }

    public Object run(InfoLesson aulaAntiga, InfoLesson aulaNova, InfoShift infoShift)
            throws FenixServiceException {
        ILesson aula = null;
        InfoLessonServiceResult result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IAulaPersistente aulaPersistente = sp.getIAulaPersistente();
            IRoom salaAntiga = sp.getISalaPersistente().readByName(aulaAntiga.getInfoSala().getNome());
            IRoom salaNova = sp.getISalaPersistente().readByName(aulaNova.getInfoSala().getNome());

            IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(aulaNova
                    .getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod());
            aula = aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(aulaAntiga.getDiaSemana(),
                    aulaAntiga.getInicio(), aulaAntiga.getFim(), salaAntiga, executionPeriod);
            IShift shift = Cloner.copyInfoShift2Shift(aulaNova.getInfoShift());
            IRoomOccupation roomOccupation = aula.getRoomOccupation();

            ILesson newLesson = new Lesson(aulaNova.getDiaSemana(), aulaNova.getInicio(), aulaNova.getFim(),
                    aulaNova.getTipo(), salaNova, roomOccupation, shift /* ,null */
            );
            newLesson.setIdInternal(aula.getIdInternal());
            List associatedLessons = SuportePersistenteOJB.getInstance().getIAulaPersistente()
                    .readLessonsByShift(shift);
            for (int i = 0; i < associatedLessons.size(); i++) {
                ILesson lessonAssociated = (ILesson) associatedLessons.get(i);
                if (lessonAssociated.getIdInternal().equals(aula.getIdInternal())) {
                    associatedLessons.remove(i);
                    //associatedLessons.set(i, newLesson);
                    break;
                }
            }
            shift.setAssociatedLessons(associatedLessons);
            if (aula != null) {
                result = valid(newLesson);
                if (result.getMessageType() == 1) {
                    throw new InvalidTimeIntervalServiceException();
                }
                boolean resultB = validNoInterceptingLesson(Cloner
                        .copyInfoRoomOccupation2RoomOccupation(aulaNova.getInfoRoomOccupation()),
                        roomOccupation);
                /*
                 * IShift shift = (IShift) sp.getITurnoPersistente().readByOID(
                 * Shift.class, infoShift.getIdInternal());
                 */
                InfoShiftServiceResult infoShiftServiceResult = valid(shift, newLesson);
                if (result.isSUCESS() && resultB && infoShiftServiceResult.isSUCESS()) {
                    //aula = (ILesson) aulaPersistente.readByOId(aula, true);
                    aula = (ILesson) aulaPersistente.readByOID(Lesson.class, aula.getIdInternal());
                    aulaPersistente.simpleLockWrite(aula);
                    //sp.getITurnoPersistente().simpleLockWrite(shift);
                    aula.setDiaSemana(aulaNova.getDiaSemana());
                    aula.setInicio(aulaNova.getInicio());
                    aula.setFim(aulaNova.getFim());
                    aula.setTipo(aulaNova.getTipo());
                    aula.setSala(salaNova);
                    aula.setRoomOccupation(roomOccupation);

                    roomOccupation = (IRoomOccupation) sp.getIPersistentRoomOccupation().readByOID(
                            RoomOccupation.class, roomOccupation.getIdInternal());

                    sp.getIPersistentRoomOccupation().simpleLockWrite(roomOccupation);

                    roomOccupation.setDayOfWeek(aulaNova.getDiaSemana());
                    roomOccupation.setStartTime(aulaNova.getInicio());
                    roomOccupation.setEndTime(aulaNova.getFim());
                    roomOccupation.setRoom(salaNova);

                    roomOccupation.setFrequency(aulaNova.getInfoRoomOccupation().getFrequency()
                            .intValue());
                    roomOccupation.setWeekOfQuinzenalStart(aulaNova.getInfoRoomOccupation()
                            .getWeekOfQuinzenalStart());

                    //					//O Period nunca pode vir a null
                    //					IPeriod period = (IPeriod)
                    // sp.getIPersistentPeriod().readBy(
                    //							aulaNova.getInfoRoomOccupation().getInfoPeriod().getStartDate(),
                    //							aulaNova.getInfoRoomOccupation().getInfoPeriod().getEndDate());
                    //					
                    //					roomOccupation.setPeriod(period);

                } else if (!infoShiftServiceResult.isSUCESS()) {
                    throw new InvalidLoadException(infoShiftServiceResult.toString());
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }
        return result;
    }

    private InfoLessonServiceResult valid(ILesson lesson) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    /**
     * @param aula
     * @return InfoLessonServiceResult
     */
    private boolean validNoInterceptingLesson(IRoomOccupation roomOccupation,
            IRoomOccupation oldroomOccupation) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();

            Iterator iter = roomOccupationInDBList.iterator();
            while (iter.hasNext()) {
                IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();
                if (roomOccupationInDB.equals(oldroomOccupation)) {
                    continue;
                }
                if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB)) {
                    return false;
                }
            }
            return true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        /*
         * throws ExistingServiceException,InterceptingServiceException { try {
         * ISuportePersistente sp = SuportePersistenteOJB.getInstance();
         * IAulaPersistente persistentLesson = sp.getIAulaPersistente(); List
         * lessonMatchList =
         * persistentLesson.readLessonsInBroadPeriod(newLesson, oldLesson,
         * executionPeriod); if (lessonMatchList.size() > 0) { if
         * (lessonMatchList.contains(newLesson)) { throw new
         * ExistingServiceException(); } else { throw new
         * InterceptingServiceException(); } } else { return true; } } catch
         * (ExcepcaoPersistencia e) { return false; }
         */
    }

    private InfoShiftServiceResult valid(IShift shift, ILesson lesson) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift, lesson);
        double lessonDuration = (getLessonDurationInMinutes(lesson).doubleValue()) / 60;

        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            } else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
            }
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getPraticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoPratHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
            if (hours == shift.getDisciplinaExecucao().getLabHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getLabHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
        }
        return result;
    }

    private double getTotalHoursOfShiftType(IShift shift, ILesson alteredLesson)
            throws ExcepcaoPersistencia {
        /*
         * IShift shiftCriteria = new Shift();
         * shiftCriteria.setNome(shift.getNome());
         * shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());
         * 
         * List lessonsOfShiftType = SuportePersistenteOJB .getInstance()
         * .getITurnoAulaPersistente() .readLessonsByShift(shiftCriteria);
         * 
         * ILesson lesson = null; double duration = 0; for (int i = 0; i <
         * lessonsOfShiftType.size(); i++) { lesson = ((ITurnoAula)
         * lessonsOfShiftType.get(i)).getAula(); if
         * (!lesson.getIdInternal().equals(alteredLesson.getIdInternal())) {
         * duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60); } }
         * return duration;
         */
        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        if (associatedLessons == null) {
            associatedLessons = SuportePersistenteOJB.getInstance().getIAulaPersistente()
                    .readLessonsByShift(shift);
            shift.setAssociatedLessons(associatedLessons);
        }
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            if (!lesson.getIdInternal().equals(alteredLesson.getIdInternal())) {
                duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
            }
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(ILesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;
        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidLoadException extends FenixServiceException {
        /**
         *  
         */
        private InvalidLoadException() {
            super();
        }

        /**
         * @param s
         */
        InvalidLoadException(String s) {
            super(s);
        }
    }
}