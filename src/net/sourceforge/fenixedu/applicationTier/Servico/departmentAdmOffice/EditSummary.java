/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Manuel Pinto e João Figueiredo
 */
public class EditSummary implements IService {

    /**
     *  
     */
    public EditSummary() {
    }

    public void run(Integer executionCourseId, InfoSummary infoSummary) throws FenixServiceException {

        try {
            if (infoSummary == null || infoSummary.getInfoShift() == null
                    || infoSummary.getInfoShift().getIdInternal() == null
                    || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
                throw new FenixServiceException("error.summary.impossible.edit");
            }

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
            ISummary summary = (ISummary) persistentSummary.readByOID(Summary.class, infoSummary
                    .getIdInternal(), true);

            //Shift
            if (infoSummary.getInfoShift() != null && infoSummary.getInfoShift().getIdInternal() != null) {
                if (summary.getShift() == null
                        || (summary.getShift() != null && summary.getShift().getIdInternal() != null && !summary
                                .getShift().getIdInternal().equals(
                                        infoSummary.getInfoShift().getIdInternal()))) {
                    ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
                    IShift shift = (IShift) persistentShift.readByOID(Shift.class, infoSummary
                            .getInfoShift().getIdInternal());
                    if (shift == null) {
                        throw new FenixServiceException("error.summary.no.shift");
                    }
                    summary.setShift(shift);
                }
            }
            if (summary.getShift() == null) {
                throw new FenixServiceException("error.summary.no.shift");
            }

            if (infoSummary.getIsExtraLesson().equals(Boolean.TRUE)) {
                summary.setIsExtraLesson(Boolean.TRUE);

                if (infoSummary.getInfoRoom() != null
                        && infoSummary.getInfoRoom().getIdInternal() != null
                        && (summary.getRoom() == null || (summary.getRoom() != null
                                && summary.getRoom().getIdInternal() != null && !summary.getRoom()
                                .getIdInternal().equals(infoSummary.getInfoRoom().getIdInternal())))) {
                    ISalaPersistente persistentRoom = persistentSuport.getISalaPersistente();

                    IRoom room = (IRoom) persistentRoom.readByOID(Room.class, infoSummary.getInfoRoom()
                            .getIdInternal());

                    summary.setRoom(room);
                }
            } else {
                summary.setIsExtraLesson(Boolean.FALSE);

                ILesson lesson = SummaryUtils.findlesson(summary.getShift(), infoSummary);
                if (lesson == null) {
                    throw new FenixServiceException("error.summary.no.shift");
                }

                //room
                summary.setRoom(lesson.getSala());//not necessary

                //validate da summary's date
                infoSummary.setSummaryHour(lesson.getInicio());//not necessary
                if (!SummaryUtils.verifyValidDateSummary(lesson, infoSummary.getSummaryDate(),
                        infoSummary.getSummaryHour())) {
                    throw new FenixServiceException("error.summary.invalid.date");
                }
            }

            summary.setSummaryDate(infoSummary.getSummaryDate().getTime());
            summary.setSummaryHour(infoSummary.getSummaryHour().getTime());

            //after verify summary date and hour
            //and before continue check if this summary exists
            ISummary summaryInDB = persistentSummary.readSummaryByUnique(summary.getShift(), infoSummary
                    .getSummaryDate().getTime(), infoSummary.getSummaryHour().getTime());
            if (summaryInDB != null && !summaryInDB.getIdInternal().equals(summary.getIdInternal())) {
                throw new FenixServiceException("error.summary.already.exists");
            }

            summary.setStudentsNumber(infoSummary.getStudentsNumber());

            if (infoSummary.getInfoProfessorship() != null
                    && infoSummary.getInfoProfessorship().getIdInternal() != null) {
                IPersistentProfessorship persistentProfessorship = persistentSuport
                        .getIPersistentProfessorship();

                IProfessorship professorship = (IProfessorship) persistentProfessorship.readByOID(
                        Professorship.class, infoSummary.getInfoProfessorship().getIdInternal());
                if (professorship == null) {
                    throw new FenixServiceException("error.summary.no.teacher");
                }

                summary.setProfessorship(professorship);
                summary.setTeacher(null);
                summary.setTeacherName(null);
            } else if (infoSummary.getInfoTeacher() != null
                    && infoSummary.getInfoTeacher().getTeacherNumber() != null) {
                IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();

                ITeacher teacher = persistentTeacher.readByNumber(infoSummary.getInfoTeacher()
                        .getTeacherNumber());
                if (teacher == null) {
                    throw new FenixServiceException("error.summary.no.teacher");
                }

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

}