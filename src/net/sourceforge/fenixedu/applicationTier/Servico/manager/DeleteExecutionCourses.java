/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.classProperties.ExecutionCourseProperty;
import net.sourceforge.fenixedu.domain.classProperties.ExecutionCourseProperty;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jdnf, mrsp and Luis Cruz
 * 
 */
public class DeleteExecutionCourses implements IService {

    public List run(final List executionCourseIDs) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final List<String> undeletedExecutionCoursesCodes = new ArrayList<String>();

        for (final Integer executionCourseID : (List<Integer>) executionCourseIDs) {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseID);

            if (!deleteExecutionCourses(persistentSupport, persistentExecutionCourse, executionCourse)) {
                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
            }
        }
        return undeletedExecutionCoursesCodes;
    }

    private boolean deleteExecutionCourses(final ISuportePersistente persistentSupport,
            final IPersistentExecutionCourse persistentExecutionCourse,
            final ExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        if (canBeDeleted(executionCourse)) {
            executionCourse.setExecutionPeriod(null);
            deleteProfessorships(persistentSupport, executionCourse);
            dereferenceCurricularCourses(executionCourse);
            deleteExecutionCourseProperties(persistentSupport, executionCourse);
            deleteNonAffiliatedTeachers(executionCourse);
            persistentExecutionCourse
                    .deleteByOID(ExecutionCourse.class, executionCourse.getIdInternal());
            return true;
        }
        return false;
    }

    private void deleteNonAffiliatedTeachers(final ExecutionCourse executionCourse) {
        final List<NonAffiliatedTeacher> nonAffiliatedTeachers = executionCourse
                .getNonAffiliatedTeachers();
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachers) {
            nonAffiliatedTeacher.getExecutionCourses().remove(executionCourse);
        }
        nonAffiliatedTeachers.clear();
    }

    private void deleteExecutionCourseProperties(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();
        final List<ExecutionCourseProperty> executionCourseProperties = executionCourse
                .getExecutionCourseProperties();
        for (final ExecutionCourseProperty executionCourseProperty : executionCourseProperties) {
            executionCourseProperty.setExecutionCourse(null);
            persistentObject.deleteByOID(ExecutionCourseProperty.class, executionCourseProperty
                    .getIdInternal());
        }
    }

    private void dereferenceCurricularCourses(final ExecutionCourse executionCourse) {
        final List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        curricularCourses.clear();
    }

    private void deleteProfessorships(final ISuportePersistente persistentSupport,
            final ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final IPersistentProfessorship persistentProfessorship = persistentSupport
                .getIPersistentProfessorship();
        final List<Professorship> professorships = executionCourse.getProfessorships();
        for (final Professorship professorship : professorships) {
            professorship.delete();
            persistentProfessorship.deleteByOID(Professorship.class, professorship.getIdInternal());
        }
        professorships.clear();
    }

    private boolean canBeDeleted(ExecutionCourse executionCourse) {
        if (!executionCourse.getAssociatedSummaries().isEmpty()) {
            return false;
        }
        if (!executionCourse.getGroupings().isEmpty()) {
            return false;
        }
        if (!executionCourse.getAssociatedBibliographicReferences().isEmpty()) {
            return false;
        }
        if (!executionCourse.getAssociatedEvaluations().isEmpty()) {
            return false;
        }
        if (!executionCourse.getAttends().isEmpty()) {
            return false;
        }
        if (executionCourse.getEvaluationMethod() != null) {
            return false;
        }
        if (!executionCourse.getAssociatedShifts().isEmpty()) {
            return false;
        }
        if (executionCourse.getCourseReport() != null) {
            return false;
        }
        final Site site = executionCourse.getSite();
        if (site != null) {
            if (!site.getAssociatedAnnouncements().isEmpty()) {
                return false;
            }
            if (!site.getAssociatedSections().isEmpty()) {
                return false;
            }
        }
        final List<Professorship> professorships = executionCourse.getProfessorships();
        for (final Professorship professorship : professorships) {
            if (!professorship.getAssociatedShiftProfessorship().isEmpty()) {
                return false;
            }
            if (!professorship.getAssociatedSummaries().isEmpty()) {
                return false;
            }
            if (!professorship.getSupportLessons().isEmpty()) {
                return false;
            }
        }

        return true;
    }

}