package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
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
 * @author Fernanda Quitério
 * 
 * Modified by Tânia Pousão at 2/Dez/2003
 */
public class EditProgram implements IService {

    public EditProgram() {
    }

    public boolean run(Integer infoExecutionCourseCode, Integer infoCurricularCourseCode,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Person who change all information
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(username);
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
            if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())) {
                persistentCurriculum.simpleLockWrite(curriculum);

                // let's edit curriculum
                curriculum.setCurricularCourse(curricularCourse);

                curriculum.setProgram(infoCurriculumNew.getProgram());
                curriculum.setProgramEn(infoCurriculumNew.getProgramEn());

                curriculum.setPersonWhoAltered(person);

                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());

            } else {
                // creates new information
                ICurriculum newCurriculum = new Curriculum();
                persistentCurriculum.simpleLockWrite(newCurriculum);

                newCurriculum.setCurricularCourse(curricularCourse);

                newCurriculum.setProgram(infoCurriculumNew.getProgram());
                newCurriculum.setProgramEn(infoCurriculumNew.getProgramEn());

                newCurriculum.setGeneralObjectives(curriculum.getGeneralObjectives());
                newCurriculum.setOperacionalObjectives(curriculum.getOperacionalObjectives());
                newCurriculum.setGeneralObjectivesEn(curriculum.getGeneralObjectivesEn());
                newCurriculum.setOperacionalObjectivesEn(curriculum.getOperacionalObjectivesEn());

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