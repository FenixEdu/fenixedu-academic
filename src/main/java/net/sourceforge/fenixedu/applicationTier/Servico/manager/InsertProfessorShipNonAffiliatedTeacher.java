package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class InsertProfessorShipNonAffiliatedTeacher {

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static void run(Integer nonAffiliatedTeacherID, Integer executionCourseID) {

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);
        if (executionCourse == null) {
            throw new DomainException("message.nonExisting.executionCourse");
        }

        final NonAffiliatedTeacher nonAffiliatedTeacher = RootDomainObject.getInstance().readNonAffiliatedTeacherByOID(nonAffiliatedTeacherID);
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

    private static final InsertProfessorShipNonAffiliatedTeacher serviceInstance = new InsertProfessorShipNonAffiliatedTeacher();

    @Service
    public static void runInsertProfessorShipNonAffiliatedTeacher(Integer nonAffiliatedTeacherID, Integer executionCourseID)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(nonAffiliatedTeacherID, executionCourseID);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                serviceInstance.run(nonAffiliatedTeacherID, executionCourseID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}