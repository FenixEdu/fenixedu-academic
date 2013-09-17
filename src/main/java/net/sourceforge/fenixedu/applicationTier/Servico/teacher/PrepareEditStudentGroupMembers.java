/*
 * Created on 17/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class PrepareEditStudentGroupMembers {

    protected List run(String executionCourseID, String studentGroupID) throws FenixServiceException {
        final StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Attends> groupingAttends = new ArrayList<Attends>();
        groupingAttends.addAll(studentGroup.getGrouping().getAttends());;

        final Collection<StudentGroup> studentsGroups = studentGroup.getGrouping().getStudentGroupsSet();
        for (final StudentGroup studentGroupIter : studentsGroups) {
            for (final Attends attend : studentGroupIter.getAttends()) {
                groupingAttends.remove(attend);
            }
        }
        final List<InfoStudent> infoStudents = new ArrayList<InfoStudent>();
        for (final Attends attend : groupingAttends) {
            infoStudents.add(InfoStudent.newInfoFromDomain(attend.getRegistration()));
        }
        return infoStudents;
    }

    // Service Invokers migrated from Berserk

    private static final PrepareEditStudentGroupMembers serviceInstance = new PrepareEditStudentGroupMembers();

    @Atomic
    public static List runPrepareEditStudentGroupMembers(String executionCourseID, String studentGroupID)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, studentGroupID);
    }

}