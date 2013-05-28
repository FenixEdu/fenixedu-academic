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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

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

    protected Boolean run(Integer executionCourseID, Integer groupNumber, Integer groupingID, Integer shiftID,
            List studentUserNames) throws FenixServiceException {
        final Grouping grouping = AbstractDomainObject.fromExternalId(groupingID);

        if (grouping == null) {
            throw new FenixServiceException();
        }

        Shift shift = AbstractDomainObject.fromExternalId(shiftID);

        List studentList = buildStudentList(studentUserNames, grouping);

        grouping.createStudentGroup(shift, groupNumber, studentList);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateStudentGroup serviceInstance = new CreateStudentGroup();

    @Service
    public static Boolean runCreateStudentGroup(Integer executionCourseID, Integer groupNumber, Integer groupingID,
            Integer shiftID, List studentUserNames) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, groupNumber, groupingID, shiftID, studentUserNames);
    }

}