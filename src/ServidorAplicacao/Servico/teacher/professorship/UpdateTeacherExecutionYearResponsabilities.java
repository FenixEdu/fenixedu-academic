/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ExecutionYear;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import ServidorAplicacao.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            final List executionCourseResponsabilities)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionYear executionYearDAO = sp
                    .getIPersistentExecutionYear();
            IPersistentResponsibleFor responsibleForDAO = sp
                    .getIPersistentResponsibleFor();
            IPersistentExecutionCourse executionCourseDAO = sp
                    .getIPersistentExecutionCourse();
            ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                    teacherId);
            IExecutionYear executionYear = (IExecutionYear) executionYearDAO
                    .readByOID(ExecutionYear.class, executionYearId);

            List responsibleFors = responsibleForDAO
                    .readByTeacherAndExecutionYear(teacher, executionYear);

            final List executionCourseIds = (List) CollectionUtils.collect(
                    responsibleFors, new Transformer() {

                        public Object transform(Object input) {
                            IResponsibleFor responsibleFor = (IResponsibleFor) input;
                            Integer executionCourseId = responsibleFor
                                    .getExecutionCourse().getIdInternal();
                            return executionCourseId;
                        }
                    });

            List responsabilitiesToAdd = (List) CollectionUtils.select(
                    executionCourseResponsabilities, new Predicate() {

                        public boolean evaluate(Object input) {
                            Integer executionCourseToAdd = (Integer) input;
                            return !executionCourseIds
                                    .contains(executionCourseToAdd);
                        }
                    });

            List responsabilitiesToRemove = (List) CollectionUtils.select(
                    executionCourseIds, new Predicate() {

                        public boolean evaluate(Object input) {
                            Integer executionCourseToRemove = (Integer) input;
                            return !executionCourseResponsabilities
                                    .contains(executionCourseToRemove);
                        }
                    });

            addResponsibleFors(teacher, responsabilitiesToAdd,
                    responsibleForDAO, executionCourseDAO);

            removeResponsibleFors(teacher, responsabilitiesToRemove,
                    responsibleForDAO, executionCourseDAO);
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
    private void removeResponsibleFors(ITeacher teacher,
            List responsabilitiesToRemove,
            IPersistentResponsibleFor responsibleForDAO,
            IPersistentExecutionCourse executionCourseDAO)
            throws ExcepcaoPersistencia {
        if (!responsabilitiesToRemove.isEmpty()) {
            List responsibleFors = responsibleForDAO
                    .readByTeacherAndExecutionCourseIds(teacher,
                            responsabilitiesToRemove);
            for (int i = 0; i < responsibleFors.size(); i++) {
                IResponsibleFor responsibleFor = (IResponsibleFor) responsibleFors
                        .get(i);
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
    private void addResponsibleFors(ITeacher teacher,
            List responsabilitiesToAdd,
            IPersistentResponsibleFor responsibleForDAO,
            IPersistentExecutionCourse executionCourseDAO)
            throws MaxResponsibleForExceed, InvalidCategory,
            ExcepcaoPersistencia {
        if (!responsabilitiesToAdd.isEmpty()) {
            List executionCourses = executionCourseDAO
                    .readByExecutionCourseIds(responsabilitiesToAdd);
            for (int i = 0; i < executionCourses.size(); i++) {
                IExecutionCourse executionCourse = (IExecutionCourse) executionCourses
                        .get(i);
                IResponsibleFor responsibleFor = new ResponsibleFor();
                responsibleForDAO.simpleLockWrite(responsibleFor);
                responsibleFor.setTeacher(teacher);
                responsibleFor.setExecutionCourse(executionCourse);
                ResponsibleForValidator.getInstance()
                        .validateResponsibleForList(teacher, executionCourse,
                                responsibleFor, responsibleForDAO);
            }
        }
    }
}