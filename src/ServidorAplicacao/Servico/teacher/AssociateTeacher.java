package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 */
public class AssociateTeacher implements IService
{

    /**
     * The Actor of this class.
     */
    public AssociateTeacher()
    {
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode, Integer teacherNumber)
            throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            ITeacher iTeacher = persistentTeacher.readByNumber(teacherNumber);
            if (iTeacher == null) { throw new InvalidArgumentsServiceException(); }
            ExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourseCode);
            IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                    executionCourse, false);
            IProfessorship professorship = new Professorship();
            persistentProfessorship.simpleLockWrite(professorship);

            professorship.setTeacher(iTeacher);
            professorship.setExecutionCourse(iExecutionCourse);
        }
        catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }
        return true;
    }
}