/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class SaveTeachersBody implements IService {

    public Boolean run(List responsibleTeachersIds, List professorShipTeachersIds,
            Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = true;
        Integer id;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {

            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        // RESPONSIBLES MODIFICATIONS

        // get the ids of the teachers that used to be responsible
        IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
        List oldResponsibles = persistentResponsibleFor.readByExecutionCourse(executionCourse
                .getIdInternal());
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
                            ((IResponsibleFor) oldResponsibles
                                    .get(oldResponsibleTeachersIds.indexOf(id))).getIdInternal());
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
                if (!professorShipTeachersIds.contains(id) && !responsibleTeachersIds.contains(id)) {

                    IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                    IProfessorship professorship2 = persistentProfessorShip
                            .readByTeacherIDandExecutionCourseID(id, executionCourseId);
                    List summaryList = persistentSummary.readByTeacher(professorship2
                            .getExecutionCourse().getIdInternal(), professorship2.getTeacher()
                            .getTeacherNumber());
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (Iterator iterator = summaryList.iterator(); iterator.hasNext();) {
                            ISummary summary = (ISummary) iterator.next();
                            persistentSummary.simpleLockWrite(summary);
                            summary.setProfessorship(null);
                            summary.setKeyProfessorship(null);
                        }
                    }

                    persistentProfessorShip.deleteByOID(Professorship.class,
                            ((IProfessorship) oldProfessorShips.get(oldProfessorShipTeachersIds
                                    .indexOf(id))).getIdInternal());
                }
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
    }
}