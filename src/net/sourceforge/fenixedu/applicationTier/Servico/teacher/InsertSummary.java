/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

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
 * @author João Mota
 * @author Susana Fernandes modified by Tânia Pousão 5/Abr/2004 21/Jul/2003
 *         fenix-head ServidorAplicacao.Servico.teacher
 */
public class InsertSummary implements IService {

    /**
     *  
     */
    public InsertSummary() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "InsertSummary";
    }

    public Boolean run(Integer executionCourseId, InfoSummary infoSummary) throws FenixServiceException {

        try {
            if (infoSummary == null || infoSummary.getInfoShift() == null
                    || infoSummary.getInfoShift().getIdInternal() == null
                    || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
                throw new FenixServiceException("error.summary.impossible.insert");
            }

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
            ISummary summary = new Summary();
            persistentSummary.simpleLockWrite(summary);

            //Shift
            ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();

            IShift shift = (IShift) persistentShift.readByOID(Shift.class, infoSummary.getInfoShift()
                    .getIdInternal());
            if (shift == null) {
                throw new FenixServiceException("error.summary.no.shift");
            }
            summary.setShift(shift);

            //Extra lesson or not
            if (infoSummary.getIsExtraLesson().equals(Boolean.TRUE)) {
                summary.setIsExtraLesson(Boolean.TRUE);

                if (infoSummary.getInfoRoom() != null
                        && infoSummary.getInfoRoom().getIdInternal() != null) {
                    ISalaPersistente persistentRoom = persistentSuport.getISalaPersistente();

                    IRoom room = (IRoom) persistentRoom.readByOID(Room.class, infoSummary.getInfoRoom()
                            .getIdInternal());

                    summary.setRoom(room);
                }
            } else {
                summary.setIsExtraLesson(Boolean.FALSE);

                ILesson lesson = SummaryUtils.findlesson(shift, infoSummary);

                //room
                summary.setRoom(lesson.getSala());//not necessary

                //validate da summary's date
                infoSummary.setSummaryHour(lesson.getInicio());//not necessary

                if (!SummaryUtils.verifyValidDateSummary(lesson, infoSummary.getSummaryDate(),
                        infoSummary.getSummaryHour())) {
                    throw new FenixServiceException("error.summary.invalid.date");
                }
            }

            summary.setSummaryDate(infoSummary.getSummaryDate());
            summary.setSummaryHour(infoSummary.getSummaryHour());

            //after verify summary date and hour
            //and before continue check if this summary exists
            if (persistentSummary.readSummaryByUnique(shift, infoSummary.getSummaryDate(), infoSummary
                    .getSummaryHour()) != null) {
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
            } else if (infoSummary.getInfoTeacher() != null
                    && infoSummary.getInfoTeacher().getTeacherNumber() != null) {
                IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();

                ITeacher teacher = persistentTeacher.readByNumber(infoSummary.getInfoTeacher()
                        .getTeacherNumber());
                if (teacher == null) {
                    throw new FenixServiceException("error.summary.no.teacher");
                }

                summary.setTeacher(teacher);
            } else if (infoSummary.getTeacherName() != null) {
                summary.setTeacherName(infoSummary.getTeacherName());
            } else {

                throw new FenixServiceException("error.summary.no.teacher");
            }


            summary.setTitle(infoSummary.getTitle());
            summary.setSummaryText(infoSummary.getSummaryText());
            summary.setLastModifiedDate(Calendar.getInstance().getTime());

            return Boolean.TRUE;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

}