/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionYearResponsabilities implements IService {

    /**
     *  
     */
    public UpdateTeacherExecutionYearResponsabilities() {
        super();
    }

    public Boolean run(Integer teacherId, Integer executionYearId,
            final List executionCourseResponsabilities) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherId);
            IExecutionYear executionYear = (IExecutionYear) executionYearDAO.readByOID(
                    ExecutionYear.class, executionYearId);

            List responsibleFors = responsibleForDAO.readByTeacherAndExecutionYear(teacher,
                    executionYear);

            final List executionCourseIds = (List) CollectionUtils.collect(responsibleFors,
                    new Transformer() {

                        public Object transform(Object input) {
                            IResponsibleFor responsibleFor = (IResponsibleFor) input;
                            Integer executionCourseId = responsibleFor.getExecutionCourse()
                                    .getIdInternal();
                            return executionCourseId;
                        }
                    });

            List responsabilitiesToAdd = (List) CollectionUtils.select(executionCourseResponsabilities,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            Integer executionCourseToAdd = (Integer) input;
                            return !executionCourseIds.contains(executionCourseToAdd);
                        }
                    });

            List responsabilitiesToRemove = (List) CollectionUtils.select(executionCourseIds,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            Integer executionCourseToRemove = (Integer) input;
                            return !executionCourseResponsabilities.contains(executionCourseToRemove);
                        }
                    });

            addResponsibleFors(teacher, responsabilitiesToAdd, responsibleForDAO, executionCourseDAO);

            removeResponsibleFors(teacher, responsabilitiesToRemove, responsibleForDAO,
                    executionCourseDAO);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Problems on database!", e);
        }

        return Boolean.TRUE;
    }

    /**
     * @param teacher
     * @param responsabilitiesToAdd
     * @param responsibleForDAO
     * @param executionCourseDAO
     */
    private void removeResponsibleFors(ITeacher teacher, List responsabilitiesToRemove,
            IPersistentResponsibleFor responsibleForDAO, IPersistentExecutionCourse executionCourseDAO)
            throws ExcepcaoPersistencia {
        if (!responsabilitiesToRemove.isEmpty()) {
            List responsibleFors = responsibleForDAO.readByTeacherAndExecutionCourseIds(teacher,
                    responsabilitiesToRemove);
            for (int i = 0; i < responsibleFors.size(); i++) {
                IResponsibleFor responsibleFor = (IResponsibleFor) responsibleFors.get(i);
                responsibleForDAO.delete(responsibleFor);
            }
        }
    }

    /**
     * @param teacher
     * @param responsabilitiesToAdd
     * @param responsibleForDAO
     * @param sp
     */
    private void addResponsibleFors(ITeacher teacher, List responsabilitiesToAdd,
            IPersistentResponsibleFor responsibleForDAO, IPersistentExecutionCourse executionCourseDAO)
            throws MaxResponsibleForExceed, InvalidCategory, ExcepcaoPersistencia {
        if (!responsabilitiesToAdd.isEmpty()) {
            List executionCourses = executionCourseDAO.readByExecutionCourseIds(responsabilitiesToAdd);
            for (int i = 0; i < executionCourses.size(); i++) {
                IExecutionCourse executionCourse = (IExecutionCourse) executionCourses.get(i);
                IResponsibleFor responsibleFor = new ResponsibleFor();
                responsibleForDAO.simpleLockWrite(responsibleFor);
                responsibleFor.setTeacher(teacher);
                responsibleFor.setExecutionCourse(executionCourse);
                ResponsibleForValidator.getInstance().validateResponsibleForList(teacher,
                        executionCourse, responsibleFor, responsibleForDAO);
            }
        }
    }
}