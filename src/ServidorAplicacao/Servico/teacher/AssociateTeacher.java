package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
public class AssociateTeacher implements IService {

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode, Integer teacherNumber)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            ITeacher iTeacher = persistentTeacher.readByNumber(teacherNumber);
            if (iTeacher == null) {
                throw new InvalidArgumentsServiceException();
            }

            IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);

            if (lectures(iTeacher, iExecutionCourse.getProfessorships())) {
                throw new ExistingServiceException();
            }

            IProfessorship professorship = new Professorship();
            persistentProfessorship.simpleLockWrite(professorship);

            professorship.setTeacher(iTeacher);
            professorship.setExecutionCourse(iExecutionCourse);
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return true;
    }

    protected boolean lectures(final ITeacher teacher, final List professorships) {
        return CollectionUtils.find(professorships, new Predicate() {

            public boolean evaluate(Object arg0) {
                IProfessorship professorship = (IProfessorship) arg0;
                return professorship.getTeacher().getIdInternal().equals(teacher.getIdInternal());
            }}) != null;
    }
}