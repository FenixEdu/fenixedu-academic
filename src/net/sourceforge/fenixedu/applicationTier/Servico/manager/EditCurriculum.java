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

    public void run(InfoCurriculum infoCurriculum, String language, String username)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                .getIPersistentCurricularCourse();
        IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();
        IPersistentCurriculum persistentCurriculum = persistentSuport.getIPersistentCurriculum();
        IPersistentExecutionYear persistentExecutionYear = persistentSuport
                .getIPersistentExecutionYear();

        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, infoCurriculum.getInfoCurricularCourse().getIdInternal());

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        IPerson person = persistentPerson.lerPessoaPorUsername(username);
        if (person == null) {
            throw new NonExistingServiceException();
        }

        ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());

        if (curriculum == null) {
            curriculum = new Curriculum();

            Calendar today = Calendar.getInstance();
            curriculum.setLastModificationDate(today.getTime());
            curriculum.setCurricularCourse(curricularCourse);
        }

        IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            curriculum.edit(infoCurriculum.getGeneralObjectives(), infoCurriculum
                    .getOperacionalObjectives(), infoCurriculum.getProgram(), infoCurriculum
                    .getGeneralObjectivesEn(), infoCurriculum.getOperacionalObjectivesEn(),
                    infoCurriculum.getProgramEn(), language, person);

        } else {
            ICurriculum newCurriculum = new Curriculum();
            newCurriculum.setCurricularCourse(curricularCourse);

            newCurriculum.edit(infoCurriculum.getGeneralObjectives(), infoCurriculum
                    .getOperacionalObjectives(), infoCurriculum.getProgram(), infoCurriculum
                    .getGeneralObjectivesEn(), infoCurriculum.getOperacionalObjectivesEn(),
                    infoCurriculum.getProgramEn(), language, person);

        }
    }
}