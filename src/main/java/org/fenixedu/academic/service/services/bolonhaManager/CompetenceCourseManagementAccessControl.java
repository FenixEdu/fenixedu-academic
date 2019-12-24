/**
 * 
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author shezad
 *
 */
public class CompetenceCourseManagementAccessControl {

    private static Predicate<CompetenceCourse> allowedToViewCompetenceCoursePredicate = null;

    private static BiPredicate<CompetenceCourse, ExecutionInterval> allowedToViewChangeRequestsPredicate = null;
    private static BiPredicate<CompetenceCourse, ExecutionInterval> allowedToManageChangeRequestsPredicate = null;

    private static Predicate<CompetenceCourseInformation> allowedToManageCompetenceCourseInformationPredicate = null;
    private static Predicate<Department> allowedToManageDepartmentCompetenceCourseInformationsPredicate = null;

    private static Predicate<CompetenceCourseInformationChangeRequest> allowedToApproveChangeRequestsPredicate = null;

    public static void registerAllowedToViewCompetenceCoursePredicate(final Predicate<CompetenceCourse> predicate) {
        allowedToViewCompetenceCoursePredicate = predicate;
    }

    public static void registerAllowedToViewChangeRequestsPredicate(
            final BiPredicate<CompetenceCourse, ExecutionInterval> predicate) {
        allowedToViewChangeRequestsPredicate = predicate;
    }

    public static void registerAllowedToManageChangeRequestsPredicate(
            final BiPredicate<CompetenceCourse, ExecutionInterval> predicate) {
        allowedToManageChangeRequestsPredicate = predicate;
    }

    public static void registerAllowedToManageCompetenceCourseInformationPredicate(
            final Predicate<CompetenceCourseInformation> predicate) {
        allowedToManageCompetenceCourseInformationPredicate = predicate;
    }

    public static void registerAllowedToManageDepartmentCompetenceCourseInformationsPredicate(
            final Predicate<Department> predicate) {
        allowedToManageDepartmentCompetenceCourseInformationsPredicate = predicate;
    }

    public static void registerAllowedToApproveChangeRequestsPredicate(
            final Predicate<CompetenceCourseInformationChangeRequest> predicate) {
        allowedToApproveChangeRequestsPredicate = predicate;
    }

    public static boolean isLoggedPersonAllowedToViewCompetenceCourse(final CompetenceCourse competenceCourse) {

        if (allowedToViewCompetenceCoursePredicate != null) {
            return allowedToViewCompetenceCoursePredicate.test(competenceCourse);
        }

        Person person = AccessControl.getPerson();
        if (competenceCourse.isApproved()) {
            return true;
        }
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return competenceCourse.getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public static boolean isLoggedPersonAllowedToViewChangeRequests(final CompetenceCourse competenceCourse,
            final ExecutionInterval executionInterval) {

        if (allowedToViewChangeRequestsPredicate != null) {
            return allowedToViewChangeRequestsPredicate.test(competenceCourse, executionInterval);
        }

        Person person = AccessControl.getPerson();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        for (CompetenceCourseInformation information : competenceCourse.getCompetenceCourseInformationsSet()) {
            if (information.getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLoggedPersonAllowedToManageChangeRequests(final CompetenceCourse competenceCourse,
            final ExecutionInterval executionInterval) {

        if (allowedToManageChangeRequestsPredicate != null) {
            return allowedToManageChangeRequestsPredicate.test(competenceCourse, executionInterval);
        }

        Person person = AccessControl.getPerson();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return competenceCourse.getDepartmentUnit(executionInterval).getDepartment()
                .isUserMemberOfCompetenceCourseMembersGroup(person);
    }

    public static boolean isLoggedPersonAllowedToManageDepartmentCompetenceCourseInformations(final Department department) {

        if (allowedToManageDepartmentCompetenceCourseInformationsPredicate != null) {
            return allowedToManageDepartmentCompetenceCourseInformationsPredicate.test(department);
        }

        return department.getCompetenceCourseMembersGroup().isMember(Authenticate.getUser());
    }

    public static boolean isLoggedPersonAllowedToManageCompetenceCourseInformation(
            final CompetenceCourseInformation competenceCourseInformation) {

        if (allowedToManageCompetenceCourseInformationPredicate != null) {
            return allowedToManageCompetenceCourseInformationPredicate.test(competenceCourseInformation);
        }

        Person person = AccessControl.getPerson();
        if (competenceCourseInformation.isCompetenceCourseInformationChangeRequestDraftAvailable()) {
            return false;
        }
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(person.getUser())) {
            return true;
        }
        if (!RoleType.BOLONHA_MANAGER.isMember(person.getUser())) {
            return false;
        }
        return competenceCourseInformation.getDepartmentUnit().getDepartment().isUserMemberOfCompetenceCourseMembersGroup(person);

    }

    public static boolean isLoggedPersonAllowedToApproveChangeRequestsPredicate(
            final CompetenceCourseInformationChangeRequest changeRequest) {

        if (allowedToApproveChangeRequestsPredicate != null) {
            return allowedToApproveChangeRequestsPredicate.test(changeRequest);
        }

        Person loggedPerson = AccessControl.getPerson();
        return RoleType.SCIENTIFIC_COUNCIL.isMember(loggedPerson.getUser());
    }

}
