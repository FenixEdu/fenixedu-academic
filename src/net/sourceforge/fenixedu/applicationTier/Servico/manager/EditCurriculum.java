/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class EditCurriculum extends Service {

    public void run(InfoCurriculum infoCurriculum, String language, String username)
            throws FenixServiceException, ExcepcaoPersistencia {
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurriculum.getInfoCurricularCourse().getIdInternal());

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new NonExistingServiceException();
        }

        Curriculum curriculum = curricularCourse.findLatestCurriculum();

        if (curriculum == null) {
            curriculum = new Curriculum();

            Calendar today = Calendar.getInstance();
            curriculum.setLastModificationDate(today.getTime());
            curriculum.setCurricularCourse(curricularCourse);
        }

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            curriculum.edit(infoCurriculum.getGeneralObjectives(), infoCurriculum
                    .getOperacionalObjectives(), infoCurriculum.getProgram(), infoCurriculum
                    .getGeneralObjectivesEn(), infoCurriculum.getOperacionalObjectivesEn(),
                    infoCurriculum.getProgramEn(), language, person);

        } else {
            Curriculum newCurriculum = new Curriculum();
            newCurriculum.setCurricularCourse(curricularCourse);

            newCurriculum.edit(infoCurriculum.getGeneralObjectives(), infoCurriculum
                    .getOperacionalObjectives(), infoCurriculum.getProgram(), infoCurriculum
                    .getGeneralObjectivesEn(), infoCurriculum.getOperacionalObjectivesEn(),
                    infoCurriculum.getProgramEn(), language, person);

        }
    }
}