/*
 * Created on 21/Jul/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSummary;
import Dominio.IAula;
import Dominio.IProfessorship;
import Dominio.ISala;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Sala;
import Dominio.Summary;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Susana Fernandes 21/Jul/2003 fenix-head
 *         ServidorAplicacao.Servico.teacher
 */
public class EditSummary implements IService {

    private static EditSummary service = new EditSummary();

    public static EditSummary getService() {

        return service;
    }

    /**
     *  
     */
    public EditSummary() {
    }

    public void run(Integer executionCourseId, InfoSummary infoSummary)
            throws FenixServiceException {

        try {
            if (infoSummary == null || infoSummary.getInfoShift() == null
                    || infoSummary.getInfoShift().getIdInternal() == null
                    || infoSummary.getIsExtraLesson() == null
                    || infoSummary.getSummaryDate() == null) { throw new FenixServiceException(
                    "error.summary.impossible.edit"); }

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();

            IPersistentSummary persistentSummary = persistentSuport
                    .getIPersistentSummary();
            ISummary summary = new Summary();
            summary.setIdInternal(infoSummary.getIdInternal());
            summary = (ISummary) persistentSummary.readByOId(summary, true);

            //Shift
            if (infoSummary.getInfoShift() != null
                    && infoSummary.getInfoShift().getIdInternal() != null) {
                if (summary.getShift() == null
                        || (summary.getShift() != null
                                && summary.getShift().getIdInternal() != null && !summary
                                .getShift().getIdInternal().equals(
                                        infoSummary.getInfoShift()
                                                .getIdInternal()))) {
                    ITurnoPersistente persistentShift = persistentSuport
                            .getITurnoPersistente();
                    ITurno shift = new Turno();
                    shift.setIdInternal(infoSummary.getInfoShift()
                            .getIdInternal());

                    shift = (ITurno) persistentShift.readByOId(shift, false);
                    if (shift == null) { throw new FenixServiceException(
                            "error.summary.no.shift"); }
                    summary.setShift(shift);
                }
            }
            if (summary.getShift() == null) { throw new FenixServiceException(
                    "error.summary.no.shift"); }

            if (infoSummary.getIsExtraLesson().equals(Boolean.TRUE)) {
                summary.setIsExtraLesson(Boolean.TRUE);

                if (infoSummary.getInfoRoom() != null && infoSummary.getInfoRoom().getIdInternal() != null                                       
                    && (summary.getRoom() == null
                        || (summary.getRoom() != null
                                && summary.getRoom().getIdInternal() != null
                                && !summary.getRoom().getIdInternal().equals(infoSummary.getInfoRoom().getIdInternal())))) {
                    ISalaPersistente persistentRoom = persistentSuport
                            .getISalaPersistente();

                    ISala room = new Sala();
                    room.setIdInternal(infoSummary.getInfoRoom()
                            .getIdInternal());
                    room = (ISala) persistentRoom.readByOId(room, false);

                    summary.setRoom(room);
                }
            } else {
                summary.setIsExtraLesson(Boolean.FALSE);

                IAula lesson = findlesson(summary.getShift(), infoSummary);
                if (lesson == null) { throw new FenixServiceException(
                        "error.summary.no.shift"); }

                //room
                summary.setRoom(lesson.getSala());//not necessary

                //validate da summary's date
                infoSummary.setSummaryHour(lesson.getInicio());//not necessary
                if (!verifyValidDateSummary(summary.getShift(), infoSummary
                        .getSummaryDate(), infoSummary.getSummaryHour())) { throw new FenixServiceException(
                        "error.summary.invalid.date"); }
            }

            summary.setSummaryDate(infoSummary.getSummaryDate());
            summary.setSummaryHour(infoSummary.getSummaryHour());

            //after verify summary date and hour
            //and before continue check if this summary exists
            ISummary summaryInDB = persistentSummary.readSummaryByUnique(
                    summary.getShift(), infoSummary.getSummaryDate(),
                    infoSummary.getSummaryHour());
            if (summaryInDB != null
                    && !summaryInDB.getIdInternal().equals(
                            summary.getIdInternal())) { throw new FenixServiceException(
                    "error.summary.already.exists"); }

            summary.setStudentsNumber(infoSummary.getStudentsNumber());

            if (infoSummary.getInfoProfessorship() != null
                    && infoSummary.getInfoProfessorship().getIdInternal() != null) {
                IPersistentProfessorship persistentProfessorship = persistentSuport
                        .getIPersistentProfessorship();

                IProfessorship professorship = new Professorship();
                professorship.setIdInternal(infoSummary.getInfoProfessorship()
                        .getIdInternal());

                professorship = (IProfessorship) persistentProfessorship
                        .readByOId(professorship, false);
                if (professorship == null) { throw new FenixServiceException(
                        "error.summary.no.teacher"); }

                summary.setProfessorship(professorship);
                summary.setTeacher(null);
                summary.setTeacherName(null);
            } else if (infoSummary.getInfoTeacher() != null
                    && infoSummary.getInfoTeacher().getTeacherNumber() != null) {
                IPersistentTeacher persistentTeacher = persistentSuport
                        .getIPersistentTeacher();

                ITeacher teacher = persistentTeacher.readByNumber(infoSummary
                        .getInfoTeacher().getTeacherNumber());
                if (teacher == null) { throw new FenixServiceException(
                        "error.summary.no.teacher"); }

                summary.setTeacher(teacher);
                summary.setProfessorship(null);
                summary.setTeacherName(null);
            } else if (infoSummary.getTeacherName() != null) {
                summary.setTeacherName(infoSummary.getTeacherName());
                summary.setProfessorship(null);
                summary.setTeacher(null);
            } else {
                throw new FenixServiceException("error.summary.no.teacher");
            }

            summary.setTitle(infoSummary.getTitle());
            summary.setSummaryText(infoSummary.getSummaryText());
            summary.setLastModifiedDate(Calendar.getInstance().getTime());
            summary.setExecutionCourse(null);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param shift
     * @param infoSummary
     * @return
     */
    private IAula findlesson(ITurno shift, InfoSummary infoSummary) {
        if (shift.getAssociatedLessons() != null
                && shift.getAssociatedLessons().size() > 0) {
            ListIterator iterator = shift.getAssociatedLessons().listIterator();
            while (iterator.hasNext()) {
                IAula lesson = (IAula) iterator.next();
                if (lesson.getIdInternal().equals(
                        infoSummary.getLessonIdSelected())) { return lesson; }
            }
        }

        return null;
    }

    /**
     * @param shift
     * @param summaryDate
     * @param summaryHour
     * @return
     */
    private boolean verifyValidDateSummary(ITurno shift, Calendar summaryDate,
            Calendar summaryHour) {
        Calendar dateAndHourSummary = Calendar.getInstance();
        dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate
                .get(Calendar.DAY_OF_MONTH));
        dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));
        dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour
                .get(Calendar.HOUR_OF_DAY));
        dateAndHourSummary.set(Calendar.MINUTE, summaryHour
                .get(Calendar.MINUTE));
        dateAndHourSummary.set(Calendar.SECOND, 00);

        Calendar beginLesson = Calendar.getInstance();
        beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                .get(Calendar.DAY_OF_MONTH));
        beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        Calendar endLesson = Calendar.getInstance();
        endLesson.set(Calendar.DAY_OF_MONTH, summaryDate
                .get(Calendar.DAY_OF_MONTH));
        endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
        endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

        if (shift.getAssociatedLessons() != null
                && shift.getAssociatedLessons().size() > 0) {
            ListIterator listIterator = shift.getAssociatedLessons()
                    .listIterator();
            while (listIterator.hasNext()) {
                IAula lesson = (IAula) listIterator.next();

                beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(
                        Calendar.HOUR_OF_DAY));
                beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(
                        Calendar.MINUTE));
                beginLesson.set(Calendar.SECOND, 00);

                endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(
                        Calendar.HOUR_OF_DAY));
                endLesson.set(Calendar.MINUTE, lesson.getFim().get(
                        Calendar.MINUTE));
                endLesson.set(Calendar.SECOND, 00);

                if (dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson
                        .getDiaSemana().getDiaSemana().intValue()
                        && !beginLesson.after(dateAndHourSummary)
                        && !endLesson.before(dateAndHourSummary)) { return true; }
            }
        }

        return false;
    }
}