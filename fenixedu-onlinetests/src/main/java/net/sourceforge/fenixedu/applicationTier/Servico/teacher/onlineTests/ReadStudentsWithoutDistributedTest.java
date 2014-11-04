/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 17/Set/2003
 *
 */
package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.InfoStudent;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import org.fenixedu.academic.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest {

    protected List run(String executionCourseId, String distributedTestId) throws FenixServiceException {
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        final Collection<Attends> attendList = executionCourse.getAttendsSet();
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        final Set<Registration> students = distributedTest.findStudents();
        for (Attends attend : attendList) {
            if (!students.contains(attend.getRegistration())) {
                infoStudentList.add(InfoStudent.newInfoFromDomain(attend.getRegistration()));
            }
        }
        return infoStudentList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsWithoutDistributedTest serviceInstance = new ReadStudentsWithoutDistributedTest();

    @Atomic
    public static List runReadStudentsWithoutDistributedTest(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}