package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

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
 * @author Fernanda Quitério 21/Nov/2003
 *  
 */
public class EditCurriculumForCurricularCourse implements IService {

    public Boolean run(Integer infoExecutionDegreeId, Integer oldCurriculumId,
            Integer curricularCourseCode, InfoCurriculum newInfoCurriculum, String username,
            String language) throws FenixServiceException {
        Boolean result = new Boolean(false);
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            if (oldCurriculumId == null) {
                throw new FenixServiceException("nullCurriculumCode");
            }
            if (curricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourseCode");
            }
            if (newInfoCurriculum == null) {
                throw new FenixServiceException("nullCurriculum");
            }
            if (username == null) {
                throw new FenixServiceException("nullUsername");
            }

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);
            if (curricularCourse == null) {
                throw new NonExistingServiceException("noCurricularCourse");
            }

            IPerson person = persistentPerson.lerPessoaPorUsername(username);
            if (person == null) {
                throw new NonExistingServiceException("noPerson");
            }

            ICurriculum oldCurriculum = (ICurriculum) persistentCurriculum.readByOID(Curriculum.class,
                    oldCurriculumId);
            if (oldCurriculum == null) {
                oldCurriculum = new Curriculum();

                oldCurriculum.setCurricularCourse(curricularCourse);
                Calendar today = Calendar.getInstance();
                oldCurriculum.setLastModificationDate(today.getTime());
            }
            persistentCurriculum.simpleLockWrite(oldCurriculum);

            IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();
            // modification of curriculum is made in context of an execution
            // year
            if (!oldCurriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                    && !oldCurriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                // let's edit curriculum
                if (language == null) {
                    oldCurriculum.setGeneralObjectives(newInfoCurriculum.getGeneralObjectives());
                    oldCurriculum.setOperacionalObjectives(newInfoCurriculum.getOperacionalObjectives());
                    oldCurriculum.setProgram(newInfoCurriculum.getProgram());
                } else {
                    oldCurriculum.setGeneralObjectivesEn(newInfoCurriculum.getGeneralObjectivesEn());
                    oldCurriculum.setOperacionalObjectivesEn(newInfoCurriculum
                            .getOperacionalObjectivesEn());
                    oldCurriculum.setProgramEn(newInfoCurriculum.getProgramEn());
                }

                oldCurriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                oldCurriculum.setLastModificationDate(today.getTime());
            } else {
                ICurriculum newCurriculum = new Curriculum();
                persistentCurriculum.simpleLockWrite(newCurriculum);
                newCurriculum.setCurricularCourse(curricularCourse);
                if (language == null) {
                    newCurriculum.setGeneralObjectives(newInfoCurriculum.getGeneralObjectives());
                    newCurriculum.setOperacionalObjectives(newInfoCurriculum.getOperacionalObjectives());
                    newCurriculum.setProgram(newInfoCurriculum.getProgram());
                } else {
                    newCurriculum.setGeneralObjectivesEn(newInfoCurriculum.getGeneralObjectivesEn());
                    newCurriculum.setOperacionalObjectivesEn(newInfoCurriculum
                            .getOperacionalObjectivesEn());
                    newCurriculum.setProgramEn(newInfoCurriculum.getProgramEn());
                }

                newCurriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                newCurriculum.setLastModificationDate(today.getTime());

            }
            result = Boolean.TRUE;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        return result;
    }
}