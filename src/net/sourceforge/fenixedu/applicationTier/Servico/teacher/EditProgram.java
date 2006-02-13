package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

/**
 * @author Fernanda Quitério
 * 
 * Modified by Tânia Pousão at 2/Dez/2003
 */
public class EditProgram extends Service {

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException,
            ExcepcaoPersistencia {
        IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
        IPersistentCurriculum persistentCurriculum = persistentSupport.getIPersistentCurriculum();
        
        // Person who change all information
        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        // inexistent new information
        if (infoCurriculumNew == null) {
            throw new FenixServiceException("nullCurriculum");
        }

        // Curricular Course
        if (infoCurricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }

        CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
                CurricularCourse.class, infoCurricularCourseCode);

        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        // Curriculum       
        Curriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());

        // information doesn't exists, so it's necessary create it
        if (curriculum == null) {
            curriculum = curricularCourse.insertCurriculum("", "", "", "", "", "");
        }

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        ExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            curriculum.edit(curriculum.getGeneralObjectives(), curriculum.getOperacionalObjectives(),
                    infoCurriculumNew.getProgram(), curriculum.getGeneralObjectivesEn(), curriculum
                            .getOperacionalObjectivesEn(), infoCurriculumNew.getProgramEn(), null,
                    person);

            curriculum.edit(curriculum.getGeneralObjectives(), curriculum.getOperacionalObjectives(),
                    infoCurriculumNew.getProgram(), curriculum.getGeneralObjectivesEn(), curriculum
                            .getOperacionalObjectivesEn(), infoCurriculumNew.getProgramEn(), "Eng",
                    person);

        } else {

            Curriculum newCurriculum;

            newCurriculum = curricularCourse.insertCurriculum(infoCurriculumNew.getProgram(),
                    infoCurriculumNew.getProgramEn(), curriculum.getOperacionalObjectives(), curriculum
                            .getOperacionalObjectivesEn(), curriculum.getGeneralObjectivesEn(), curriculum
                            .getGeneralObjectivesEn());

            newCurriculum.setPersonWhoAltered(person);
        }

        return true;
    }
}