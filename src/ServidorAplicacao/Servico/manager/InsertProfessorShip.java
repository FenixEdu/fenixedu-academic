/*
 * Created on 22/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoProfessorship;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.teacher.professorship.ResponsibleForValidator;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertProfessorShip implements IService
{
    public InsertProfessorShip()
    {
    }

    public void run(InfoProfessorship infoProfessorShip, Boolean responsibleFor)
            throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            Integer executionCourseId = infoProfessorShip.getInfoExecutionCourse().getIdInternal();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(executionCourseId), false);

            if (executionCourse == null)
            {
                throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
            }

            Integer teacherNumber = infoProfessorShip.getInfoTeacher().getTeacherNumber();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);

            if (teacher == null)
            {
                throw new NonExistingServiceException("message.non.existing.teacher", null);
            }

            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();

            IProfessorship professorShip = new Professorship();
            persistentProfessorShip.simpleLockWrite(professorShip);
            professorShip.setExecutionCourse(executionCourse);
            professorShip.setTeacher(teacher);


            if (responsibleFor.booleanValue())
            {
                IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

                IResponsibleFor responsibleForTeacher = responsibleForDAO
                        .readByTeacherAndExecutionCoursePB(teacher, executionCourse);
                if (responsibleForTeacher == null)
                {
                    responsibleForTeacher = new ResponsibleFor();
                    responsibleForDAO.simpleLockWrite(responsibleForTeacher);
                    responsibleForTeacher.setExecutionCourse(executionCourse);
                    responsibleForTeacher.setTeacher(teacher);
                    ResponsibleForValidator.getInstance().validateResponsibleForList(teacher, executionCourse, responsibleForTeacher, responsibleForDAO);
                }
            }

        }
        catch (ExistingPersistentException e)
        {
            return;
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}