package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 * Modified by Tânia Pousão at 2/Dez/2003
 */

public class EditObjectives implements IServico {

    private static EditObjectives service = new EditObjectives();

    public static EditObjectives getService() {
        return service;
    }

    private EditObjectives() {
    }

    public final String getNome() {
        return "EditObjectives";
    }

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Person who change all information
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(username);
            if (person == null) {
                throw new NonExistingServiceException("noPerson");
            }

            //inexistent new information
            if (infoCurriculumNew == null) {
                throw new FenixServiceException("nullCurriculum");
            }

            //Curricular Course
            if (infoCurricularCourseCode == null) {
                throw new FenixServiceException("nullCurricularCourseCode");
            }

            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, infoCurricularCourseCode);
            if (curricularCourse == null) {
                throw new NonExistingServiceException("noCurricularCourse");
            }

            //Curriculum
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);

            //information doesn't exists, so it's necessary create it
            if (curriculum == null) {
                curriculum = new Curriculum();

                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            }

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();
            // modification of curriculum is made in context of an execution
            // year
            if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                    && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {
                persistentCurriculum.simpleLockWrite(curriculum);

                // let's edit curriculum
                curriculum.setCurricularCourse(curricularCourse);
                curriculum.setGeneralObjectives(infoCurriculumNew.getGeneralObjectives());
                curriculum.setGeneralObjectivesEn(infoCurriculumNew.getGeneralObjectivesEn());
                curriculum.setOperacionalObjectives(infoCurriculumNew.getOperacionalObjectives());
                curriculum.setOperacionalObjectivesEn(infoCurriculumNew.getOperacionalObjectivesEn());

                curriculum.setPersonWhoAltered(person);

                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            } else {
                // creates new information
                ICurriculum newCurriculum = new Curriculum();
                persistentCurriculum.simpleLockWrite(newCurriculum);

                newCurriculum.setCurricularCourse(curricularCourse);
                newCurriculum.setGeneralObjectives(infoCurriculumNew.getGeneralObjectives());
                newCurriculum.setOperacionalObjectives(infoCurriculumNew.getOperacionalObjectives());
                newCurriculum.setGeneralObjectivesEn(infoCurriculumNew.getGeneralObjectivesEn());
                newCurriculum.setOperacionalObjectivesEn(infoCurriculumNew.getOperacionalObjectivesEn());

                newCurriculum.setProgram(curriculum.getProgram());
                newCurriculum.setProgramEn(curriculum.getProgramEn());

                newCurriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                newCurriculum.setLastModificationDate(today.getTime());
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}