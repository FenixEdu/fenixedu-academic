/*
 * Created on 23/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class SaveTeachersBody implements IService {

    /**
     * Executes the service.
     */

    public Boolean run(List responsibleTeachersIds, List professorShipTeachersIds,
            Integer executionCourseId) throws FenixServiceException {

        try {
            boolean result = true;
            Integer id;

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {

                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
            }

            // RESPONSIBLES MODIFICATIONS

            // get the ids of the teachers that used to be responsible
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            List oldResponsibles = persistentResponsibleFor.readByExecutionCourse(executionCourse);
            List oldResponsibleTeachersIds = new ArrayList();
            if (oldResponsibles != null) {

                Iterator iterator = oldResponsibles.iterator();
                IResponsibleFor responsibleFor;
                while (iterator.hasNext()) {
                    responsibleFor = (IResponsibleFor) iterator.next();
                    id = responsibleFor.getTeacher().getIdInternal();
                    oldResponsibleTeachersIds.add(id);
                }

                // remove old responsibles
                Iterator oldRespIterator = oldResponsibleTeachersIds.iterator();
                while (oldRespIterator.hasNext()) {
                    id = (Integer) oldRespIterator.next();
                    if (!responsibleTeachersIds.contains(id))
                        persistentResponsibleFor.deleteByOID(ResponsibleFor.class,
                                ((IResponsibleFor) oldResponsibles.get(oldResponsibleTeachersIds
                                        .indexOf(id))).getIdInternal());
                }
            }

            // add new responsibles
            Iterator newRespIterator = responsibleTeachersIds.iterator();
            IResponsibleFor responsibleForToWrite;
            ITeacher teacher;
            while (newRespIterator.hasNext()) {
                id = (Integer) newRespIterator.next();
                if (!oldResponsibleTeachersIds.contains(id)) {
                    responsibleForToWrite = new ResponsibleFor();
                    responsibleForToWrite.setExecutionCourse(executionCourse);
                    teacher = (ITeacher) sp.getIPersistentTeacher().readByOID(Teacher.class, id);
                    if (teacher == null)
                        result = false;
                    else {
                        persistentResponsibleFor.simpleLockWrite(responsibleForToWrite);
                        responsibleForToWrite.setTeacher(teacher);

                    }
                }
            }

            // PROFESSORSHIPS MODIFICATIONS

            // get the ids of the teachers that used to teach the course
            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();
            List oldProfessorShips = persistentProfessorShip.readByExecutionCourse(executionCourse);
            List oldProfessorShipTeachersIds = new ArrayList();
            if (oldProfessorShips != null && !oldProfessorShips.isEmpty()) {

                Iterator profsIterator = oldProfessorShips.iterator();
                IProfessorship professorShip;

                while (profsIterator.hasNext()) {
                    professorShip = (IProfessorship) profsIterator.next();
                    id = professorShip.getTeacher().getIdInternal();
                    oldProfessorShipTeachersIds.add(id);
                }

                // remove old professorShips
                Iterator oldProfIterator = oldProfessorShipTeachersIds.iterator();
                while (oldProfIterator.hasNext()) {
                    id = (Integer) oldProfIterator.next();
                    if (!professorShipTeachersIds.contains(id) && !responsibleTeachersIds.contains(id))
                        persistentProfessorShip.deleteByOID(Professorship.class,
                                ((IProfessorship) oldProfessorShips.get(oldProfessorShipTeachersIds
                                        .indexOf(id))).getIdInternal());
                }
            }
            // add new professorShips
            Iterator newProfIterator = professorShipTeachersIds.iterator();
            IProfessorship professorShipToWrite;
            while (newProfIterator.hasNext()) {
                id = (Integer) newProfIterator.next();
                if (!oldProfessorShipTeachersIds.contains(id)) {
                    professorShipToWrite = new Professorship();
                    professorShipToWrite.setExecutionCourse(executionCourse);
                    teacher = (ITeacher) sp.getIPersistentTeacher().readByOID(Teacher.class, id);
                    if (teacher == null)
                        result = false;
                    else {
                        persistentProfessorShip.simpleLockWrite(professorShipToWrite);
                        professorShipToWrite.setTeacher(teacher);

                    }
                }
            }

            return new Boolean(result);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}