/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import Dominio.IPerson;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class EditCurriculum implements IService {

    /**
     * Executes the service.
     */

    public void run(InfoCurriculum infoCurriculum, String language, String username)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();
            IPersistentCurriculum persistentCurriculum = persistentSuport.getIPersistentCurriculum();
            IPersistentExecutionYear persistentExecutionYear = persistentSuport
                    .getIPersistentExecutionYear();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, infoCurriculum.getInfoCurricularCourse()
                            .getIdInternal());
            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }
            IPerson person = persistentPerson.lerPessoaPorUsername(username);
            if (person == null) {
                throw new NonExistingServiceException();
            }

            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            boolean islockWrite = false;
            if (curriculum == null) {
                curriculum = new Curriculum();
                persistentCurriculum.simpleLockWrite(curriculum);
                islockWrite = true;

                curriculum.setCurricularCourse(curricularCourse);
                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            }
            if (!islockWrite) {
                persistentCurriculum.simpleLockWrite(curriculum);
            }

            IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

            // modification of curriculum is made in context of an execution
            // year
            if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                    && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                if (language == null) {
                    curriculum.setGeneralObjectives(infoCurriculum.getGeneralObjectives());
                    curriculum.setOperacionalObjectives(infoCurriculum.getOperacionalObjectives());
                    curriculum.setProgram(infoCurriculum.getProgram());
                } else {
                    curriculum.setGeneralObjectivesEn(infoCurriculum.getGeneralObjectivesEn());
                    curriculum.setOperacionalObjectivesEn(infoCurriculum.getOperacionalObjectivesEn());
                    curriculum.setProgramEn(infoCurriculum.getProgramEn());
                }
                curriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            } else {
                ICurriculum newCurriculum = new Curriculum();

                persistentCurriculum.simpleLockWrite(newCurriculum);

                newCurriculum.setCurricularCourse(curricularCourse);
                if (language == null) {
                    newCurriculum.setGeneralObjectives(infoCurriculum.getGeneralObjectives());
                    newCurriculum.setOperacionalObjectives(infoCurriculum.getOperacionalObjectives());
                    newCurriculum.setProgram(infoCurriculum.getProgram());
                } else {
                    newCurriculum.setGeneralObjectivesEn(infoCurriculum.getGeneralObjectivesEn());
                    newCurriculum
                            .setOperacionalObjectivesEn(infoCurriculum.getOperacionalObjectivesEn());
                    newCurriculum.setProgramEn(infoCurriculum.getProgramEn());
                }
                newCurriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                newCurriculum.setLastModificationDate(today.getTime());
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}