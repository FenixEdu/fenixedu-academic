/*
 * Created on 22/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class InsertProfessorShip implements IService {
    public InsertProfessorShip() {
    }

    public void run(InfoProfessorship infoProfessorShip, Boolean responsibleFor)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            Integer executionCourseId = infoProfessorShip.getInfoExecutionCourse().getIdInternal();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);

            if (executionCourse == null) {
                throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
            }

            Integer teacherNumber = infoProfessorShip.getInfoTeacher().getTeacherNumber();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);

            if (teacher == null) {
                throw new NonExistingServiceException("message.non.existing.teacher", null);
            }

            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();

            IProfessorship professorShip = new Professorship();
            persistentProfessorShip.simpleLockWrite(professorShip);
            professorShip.setExecutionCourse(executionCourse);
            professorShip.setTeacher(teacher);
            professorShip.setHours(infoProfessorShip.getHours());
            if (professorShip.getHours() == null) {
                professorShip.setHours(new Double(0.0));
            }

            if (responsibleFor.booleanValue()) {
                IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

                IResponsibleFor responsibleForTeacher = responsibleForDAO
                        .readByTeacherAndExecutionCoursePB(teacher, executionCourse);
                if (responsibleForTeacher == null) {
                    responsibleForTeacher = new ResponsibleFor();
                    responsibleForDAO.simpleLockWrite(responsibleForTeacher);
                    responsibleForTeacher.setExecutionCourse(executionCourse);
                    responsibleForTeacher.setTeacher(teacher);
                    ResponsibleForValidator.getInstance().validateResponsibleForList(teacher,
                            executionCourse, responsibleForTeacher, responsibleForDAO);
                }
            }

        } catch (ExistingPersistentException e) {
            return;
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}