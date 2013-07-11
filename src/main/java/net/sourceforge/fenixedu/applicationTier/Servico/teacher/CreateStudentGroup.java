/*
 * Created on 8/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author ansr & scpo
 */

public class CreateStudentGroup {

    private List buildStudentList(List<String> studentUserNames, Grouping grouping) throws FenixServiceException {

        List studentList = new ArrayList();
        for (final String studantUserName : studentUserNames) {
            Attends attend = grouping.getStudentAttend(studantUserName);
            Registration registration = attend.getRegistration();
            studentList.add(registration);
        }
        return studentList;
    }

    protected Boolean run(String executionCourseID, Integer groupNumber, String groupingID, String shiftID, List studentUserNames)
            throws FenixServiceException {
        final Grouping grouping = FenixFramework.getDomainObject(groupingID);

        if (grouping == null) {
            throw new FenixServiceException();
        }

        Shift shift = FenixFramework.getDomainObject(shiftID);

        List studentList = buildStudentList(studentUserNames, grouping);

        grouping.createStudentGroup(shift, groupNumber, studentList);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateStudentGroup serviceInstance = new CreateStudentGroup();

    @Atomic
    public static Boolean runCreateStudentGroup(String executionCourseID, Integer groupNumber, String groupingID, String shiftID,
            List studentUserNames) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, groupNumber, groupingID, shiftID, studentUserNames);
    }

}