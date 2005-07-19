package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
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
 * @author Fernanda Quitério
 * 
 * Modified by Tânia Pousão at 2/Dez/2003
 */

public class EditObjectives implements IService {

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        
        // Person who change all information        
        IPerson person = persistentPerson.lerPessoaPorUsername(username);
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

        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, infoCurricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        // Curriculum       
        ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());

        if (curriculum == null) {                    
            curriculum = curricularCourse.insertCurriculum("", "", "", "", "", "");
        }

        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                        
            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectives(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), null, person);

            curriculum.edit(infoCurriculumNew.getGeneralObjectives(), infoCurriculumNew
                    .getOperacionalObjectives(), curriculum.getProgram(), infoCurriculumNew
                    .getGeneralObjectives(), infoCurriculumNew.getOperacionalObjectivesEn(),
                    curriculum.getProgramEn(), "Eng", person);
           
        } else {
          
            ICurriculum newCurriculum;

            newCurriculum = curricularCourse.insertCurriculum(curriculum.getProgram(), curriculum
                    .getProgramEn(), infoCurriculumNew.getOperacionalObjectives(), infoCurriculumNew
                    .getOperacionalObjectivesEn(), infoCurriculumNew.getGeneralObjectives(),
                    infoCurriculumNew.getGeneralObjectivesEn());

            newCurriculum.setPersonWhoAltered(person);
        }
        
        return true;
    }
}