package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
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

public class EditObjectives implements IServico
{

    private static EditObjectives service = new EditObjectives();
    public static EditObjectives getService()
    {
        return service;
    }

    private EditObjectives()
    {
    }
    public final String getNome()
    {
        return "EditObjectives";
    }

    public boolean run(
        Integer infoExecutionCourseCode,
        Integer infoCurricularCourseCode,
        InfoCurriculum infoCurriculumNew,
        String username)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Person who change all information
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(username);
            if (person == null)
            {
                throw new NonExistingServiceException("noPerson");
            }

            //inexistent new information
            if (infoCurriculumNew == null)
            {
                throw new FenixServiceException("nullCurriculum");
            }

            //Curricular Course
            if (infoCurricularCourseCode == null)
            {
                throw new FenixServiceException("nullCurricularCourseCode");
            }

            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            ICurricularCourse curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(
                    new CurricularCourse(infoCurricularCourseCode),
                    false);
            if (curricularCourse == null)
            {
                throw new NonExistingServiceException("noCurricularCourse");
            }

            //Curriculum
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);

            //information doesn't exists, so it's necessary create it
            if (curriculum == null)
            {
                curriculum = new Curriculum();

                persistentCurriculum.simpleLockWrite(curriculum);

                curriculum.setCurricularCourse(curricularCourse);

                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            }

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear currentExecutionYear = persistentExecutionYear.readCurrentExecutionYear();
            // modification of curriculum is made in context of an execution year
            if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate()))
            {
                // let's edit curriculum
                curriculum.setCurricularCourse(curricularCourse);
                curriculum.setGeneralObjectives(infoCurriculumNew.getGeneralObjectives());
                curriculum.setGeneralObjectivesEn(infoCurriculumNew.getGeneralObjectivesEn());
                curriculum.setOperacionalObjectives(infoCurriculumNew.getOperacionalObjectives());
                curriculum.setOperacionalObjectivesEn(infoCurriculumNew.getOperacionalObjectivesEn());

                curriculum.setPersonWhoAltered(person);

                Calendar today = Calendar.getInstance();
                curriculum.setLastModificationDate(today.getTime());
            } else
            {
                // creates new information
                ICurriculum newCurriculum = new Curriculum();
                persistentCurriculum.simpleLockWrite(newCurriculum);
                
                newCurriculum.setCurricularCourse(curricularCourse);
                newCurriculum.setGeneralObjectives(infoCurriculumNew.getGeneralObjectives());
                newCurriculum.setOperacionalObjectives(infoCurriculumNew.getOperacionalObjectives());
                newCurriculum.setProgram(infoCurriculumNew.getProgram());
                newCurriculum.setGeneralObjectivesEn(infoCurriculumNew.getGeneralObjectivesEn());
                newCurriculum.setOperacionalObjectivesEn(infoCurriculumNew.getOperacionalObjectivesEn());
                newCurriculum.setProgramEn(infoCurriculumNew.getProgramEn());

                newCurriculum.setPersonWhoAltered(person);
                Calendar today = Calendar.getInstance();
                newCurriculum.setLastModificationDate(today.getTime());
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return true;
    }
}