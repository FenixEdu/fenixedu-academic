package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertProfessorShipNonAffiliatedTeacher {

    @Atomic
    public static void run(String nonAffiliatedTeacherID, String executionCourseID) {
        check(RolePredicates.GEP_PREDICATE);

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        if (executionCourse == null) {
            throw new DomainException("message.nonExisting.executionCourse");
        }

        final NonAffiliatedTeacher nonAffiliatedTeacher = FenixFramework.getDomainObject(nonAffiliatedTeacherID);
        if (nonAffiliatedTeacher == null) {
            throw new DomainException("message.non.existing.nonAffiliatedTeacher");
        }

        if (nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
            throw new DomainException("error.invalid.executionCourse");
        } else {
            nonAffiliatedTeacher.addExecutionCourses(executionCourse);
        }
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static void runInsertProfessorShipNonAffiliatedTeacher(String nonAffiliatedTeacherID, String executionCourseID)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            run(nonAffiliatedTeacherID, executionCourseID);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                run(nonAffiliatedTeacherID, executionCourseID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}