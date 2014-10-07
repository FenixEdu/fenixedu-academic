package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.MergeExecutionCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduISTTeacherServiceContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel()
                .registerDeletionBlockerListener(
                        Professorship.class,
                        (professorship, blockers) -> {
                            if (!professorship.getSupportLessonsSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnySupportLessons"));
                            }
                            if (!professorship.getDegreeTeachingServicesSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyDegreeTeachingServices"));
                            }
                            if (!professorship.getTeacherMasterDegreeServicesSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyTeacherMasterDegreeServices"));
                            }
                            if (!professorship.getDegreeProjectTutorialServicesSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyDegreeProjectTutorialServices"));
                            }
                        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Shift.class,
                (shift, blockers) -> {
                    if (!shift.getDegreeTeachingServicesSet().isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                                "error.deleteShift.with.degreeTeachingServices", shift.getNome()));
                    }
                });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Attends.class,
                (attends, blockers) -> {
                    if (!attends.getDegreeProjectTutorialServicesSet().isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                "error.attends.cant.delete.has.degree.project.tutorial.services"));
                    }
                });

        MergeExecutionCourses.registerMergeHandler(FenixEduISTTeacherServiceContextListener::copyProfessorships);
    }

    private static void copyProfessorships(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws MaxResponsibleForExceed, InvalidCategory {
        for (Professorship professorship : executionCourseFrom.getProfessorshipsSet()) {
            Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship.getPerson());

            for (; !professorship.getSupportLessonsSet().isEmpty(); otherProfessorship.addSupportLessons(professorship
                    .getSupportLessonsSet().iterator().next())) {
                ;
            }
            for (; !professorship.getDegreeTeachingServicesSet().isEmpty(); otherProfessorship
                    .addDegreeTeachingServices(professorship.getDegreeTeachingServicesSet().iterator().next())) {
                ;
            }
            for (; !professorship.getTeacherMasterDegreeServicesSet().isEmpty(); otherProfessorship
                    .addTeacherMasterDegreeServices(professorship.getTeacherMasterDegreeServicesSet().iterator().next())) {
                ;
            }
        }
    }

    private static Professorship findProfessorShip(final ExecutionCourse executionCourseTo, final Person person) {
        for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
