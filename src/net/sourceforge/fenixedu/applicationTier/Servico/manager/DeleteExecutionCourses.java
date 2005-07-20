/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.classProperties.ExecutionCourseProperty;
import net.sourceforge.fenixedu.domain.classProperties.IExecutionCourseProperty;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jdnf	and Luis Cruz
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
            final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseID);

            if (!deleteExecutionCourses(persistentSupport, persistentExecutionCourse, executionCourse)) {
                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
            }
        }

        return undeletedExecutionCoursesCodes;
    }

    private boolean deleteExecutionCourses(final ISuportePersistente persistentSupport,
            final IPersistentExecutionCourse persistentExecutionCourse,
            final IExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        if (canBeDeleted(executionCourse)) {
            deleteProfessorships(persistentSupport, executionCourse);
            
            executionCourse.getExecutionPeriod().getAssociatedExecutionCourses().remove(executionCourse);
            executionCourse.setExecutionPeriod(null);

            dereferenceCurricularCourses(executionCourse);

            deleteExecutionCourseProperties(persistentSupport, executionCourse);

            deleteNonAffiliatedTeachers(executionCourse);

            persistentExecutionCourse
                    .deleteByOID(ExecutionCourse.class, executionCourse.getIdInternal());

            return true;
        }
        return false;
    }

    private void deleteNonAffiliatedTeachers(final IExecutionCourse executionCourse) {
        final List<INonAffiliatedTeacher> nonAffiliatedTeachers = executionCourse
                .getNonAffiliatedTeachers();
        for (final INonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachers) {
            nonAffiliatedTeacher.getExecutionCourses().remove(executionCourse);
        }
        nonAffiliatedTeachers.clear();
    }

    private void deleteExecutionCourseProperties(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();
        final List<IExecutionCourseProperty> executionCourseProperties = executionCourse
                .getExecutionCourseProperties();
        for (final IExecutionCourseProperty executionCourseProperty : executionCourseProperties) {
            executionCourseProperty.setExecutionCourse(null);
            persistentObject.deleteByOID(ExecutionCourseProperty.class, executionCourseProperty
                    .getIdInternal());
        }
        executionCourseProperties.clear();
    }

    private void dereferenceCurricularCourses(final IExecutionCourse executionCourse) {
        final List<ICurricularCourse> curricularCourses = executionCourse
                .getAssociatedCurricularCourses();
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            curricularCourse.getAssociatedExecutionCourses().remove(executionCourse);
        }
        curricularCourses.clear();
    }

    private void deleteProfessorships(final ISuportePersistente persistentSupport,
            final IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final IPersistentProfessorship persistentProfessorship = persistentSupport
                .getIPersistentProfessorship();
        final List<IProfessorship> professorships = executionCourse.getProfessorships();
        for (final IProfessorship professorship : professorships) {
            professorship.setExecutionCourse(null);
            professorship.setTeacher(null);
            persistentProfessorship.deleteByOID(Professorship.class, professorship.getIdInternal());
        }
        professorships.clear();
    }

    private boolean canBeDeleted(IExecutionCourse executionCourse) {
        if (!executionCourse.getAssociatedSummaries().isEmpty()) {
            return false;
        }
        if (!executionCourse.getGroupProperties().isEmpty()) {
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
        final ISite site = executionCourse.getSite();
        if (site != null) {
            if (!site.getAssociatedAnnouncements().isEmpty()) {
                return false;
            }
            if (!site.getAssociatedSections().isEmpty()) {
                return false;
            }
        }
        final List<IProfessorship> professorships = executionCourse.getProfessorships();
        for (final IProfessorship professorship : professorships) {
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