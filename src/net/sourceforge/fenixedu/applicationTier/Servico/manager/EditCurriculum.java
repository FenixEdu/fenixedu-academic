/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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