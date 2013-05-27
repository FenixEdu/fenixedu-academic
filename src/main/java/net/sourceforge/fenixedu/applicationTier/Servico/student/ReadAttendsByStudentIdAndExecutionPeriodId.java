/*
 * Created on 7/Mai/2005 - 13:54:46
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadAttendsByStudentIdAndExecutionPeriodId {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers> run(Integer studentId,
            Integer executionPeriodId, Boolean onlyEnrolledCourses, Boolean onlyAttendsWithTeachers) {

        final List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers> infoAttendsList =
                new ArrayList<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers>();

        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        final Registration someRegistration = RootDomainObject.getInstance().readRegistrationByOID(studentId);
        for (final Registration registration : someRegistration.getStudent().getRegistrationsSet()) {
            for (final Attends attends : registration.getAssociatedAttendsSet()) {
                final ExecutionCourse executionCourse = attends.getExecutionCourse();
                if (executionCourse.getExecutionPeriod() == executionSemester
                        && (!(onlyEnrolledCourses.booleanValue() && (attends.getEnrolment() == null)))) {
                    if (!onlyAttendsWithTeachers) {
                        infoAttendsList.add(InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers
                                .newInfoFromDomain(attends));
                    } else if ((!attends.getExecutionCourse().getProfessorships().isEmpty())
                            || (!attends.getExecutionCourse().getNonAffiliatedTeachers().isEmpty())) {
                        infoAttendsList.add(InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers
                                .newInfoFromDomain(attends));
                    }
                }
            }
        }

        return infoAttendsList;
    }

}