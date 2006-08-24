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

/**
 * @author Fernanda Quitério
 * 
 * Modified by Tânia Pousão at 2/Dez/2003
 */

public class EditObjectives extends Service {

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException,
            ExcepcaoPersistencia {

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

        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        // Curriculum       
        Curriculum curriculum = curricularCourse.findLatestCurriculum();

        if (curriculum == null) {                    
            curriculum = curricularCourse.insertCurriculum("", "", "", "", "", "");
        }

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                        
            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectivesEn(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), null, person);

            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectivesEn(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), "Eng", person);
           
        } else {
          
            Curriculum newCurriculum = curricularCourse.insertCurriculum(curriculum.getProgram(), curriculum
                    .getProgramEn(), infoCurriculumNew.getOperacionalObjectives(), infoCurriculumNew
                    .getOperacionalObjectivesEn(), infoCurriculumNew.getGeneralObjectives(),
                    infoCurriculumNew.getGeneralObjectivesEn());

            newCurriculum.setPersonWhoAltered(person);
        }
        
        return true;
    }
}