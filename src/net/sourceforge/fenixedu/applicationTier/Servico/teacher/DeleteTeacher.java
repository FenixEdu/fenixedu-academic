package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;

/**
 * @author Fernanda Quitério
 *  
 */
public class DeleteTeacher implements IServico {
    /**
     * @author jpvl
     */
    public class ExistingSupportLesson extends FenixServiceException {

    }

    /**
     * @author jpvl
     */
    public class ExistingShiftProfessorship extends FenixServiceException {
        /**
         *  
         */
        public ExistingShiftProfessorship() {
            super();
        }
    }

    /**
     * The Actor of this class.
     */
    public DeleteTeacher() {
    }

    /**
     * Returns service name
     */
    public final String getNome() {
        return "DeleteTeacher";
    }

    /**
     * Executes the service.
     */
    public Boolean run(Integer infoExecutionCourseCode, Integer teacherCode)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

            IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();

            ITeacher iTeacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherCode);
            if (iTeacher == null) {
                throw new InvalidArgumentsServiceException();
            }

            IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);

            //note: removed the possibility for a responsible teacher to remove
            // from himself the professorship
            //(it was a feature that didnt make sense)
            IResponsibleFor responsibleFor = persistentResponsibleFor.readByTeacherAndExecutionCourse(
                    iTeacher, iExecutionCourse);
            IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

            if (responsibleFor != null) {
                if (!canDeleteResponsibleFor()) {
                    throw new notAuthorizedServiceDeleteException();
                }
                responsibleForDAO.delete(responsibleFor);

            }

            IProfessorship professorshipToDelete = persistentProfessorship
                    .readByTeacherAndExecutionCourse(iTeacher, iExecutionCourse);

            List shiftProfessorshipList = shiftProfessorshipDAO
                    .readByProfessorship(professorshipToDelete);
            List supportLessonList = supportLessonDAO.readByProfessorship(professorshipToDelete);

            if (shiftProfessorshipList.isEmpty() && supportLessonList.isEmpty()) {
                IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                List summaryList = persistentSummary.readByTeacher(professorshipToDelete.getExecutionCourse(), professorshipToDelete.getTeacher());
                if (summaryList != null && !summaryList.isEmpty()) {
                    for (Iterator iterator = summaryList.iterator(); iterator.hasNext(); ) {
                        ISummary summary = (ISummary) iterator.next();
                        persistentSummary.simpleLockWrite(summary);
                        summary.setProfessorship(null);
                        summary.setKeyProfessorship(null);
                    }
                }

                persistentProfessorship.delete(professorshipToDelete);
            } else {
                if (!shiftProfessorshipList.isEmpty()) {
                    throw new ExistingShiftProfessorship();
                }
                throw new ExistingSupportLesson();

            }

            return Boolean.TRUE;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @return
     */
    protected boolean canDeleteResponsibleFor() {

        return false;
    }
}