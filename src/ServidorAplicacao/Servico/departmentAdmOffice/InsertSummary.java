/*
 * Created on 21/Jul/2003
 */

package ServidorAplicacao.Servico.departmentAdmOffice;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSummary;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
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
import ServidorAplicacao.utils.summary.SummaryUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author mrsp and jdnf
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

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
            ISummary summary = new Summary();
            persistentSummary.simpleLockWrite(summary);

            //Shift
            ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();

            ITurno shift = (ITurno) persistentShift.readByOID(Turno.class, infoSummary.getInfoShift()
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

                    ISala room = (ISala) persistentRoom.readByOID(Sala.class, infoSummary.getInfoRoom()
                            .getIdInternal());

                    summary.setRoom(room);
                }
            } else {
                summary.setIsExtraLesson(Boolean.FALSE);

                IAula lesson = SummaryUtils.findlesson(shift, infoSummary);

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