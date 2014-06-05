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
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByIdArray {

    public List<InfoStudent> run(String executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue()) {
                studentList = returnStudentsFromShiftsArray(null, selected);
            } else {
                studentList = returnStudentsFromStudentsArray(null, selected, executionCourseId);
            }
        }
        return studentList;
    }

    public List<InfoStudent> run(String executionCourseId, String distributedTestId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue()) {
                studentList = returnStudentsFromShiftsArray(distributedTest, selected);
            } else {
                studentList = returnStudentsFromStudentsArray(distributedTest, selected, executionCourseId);
            }
        }
        return studentList;
    }

    public List<InfoStudent> run(Integer executionCourseId, ArrayList lavelValueBeanList) throws FenixServiceException {
        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        for (LabelValueBean lvb : (ArrayList<LabelValueBean>) lavelValueBeanList) {
            if (!lvb.getLabel().equals(" (Ficha Fechada)")) {
                Integer number = Integer.parseInt(lvb.getValue());
                studentList.add(InfoStudent.newInfoFromDomain(Registration.readAllStudentsBetweenNumbers(number, number)
                        .iterator().next()));
            }
        }

        return studentList;
    }

    private List<InfoStudent> returnStudentsFromShiftsArray(DistributedTest distributedTest, String[] shifts)
            throws FenixServiceException {
        final String value = BundleUtil.getString(Bundle.APPLICATION, "label.allShifts");
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (String shift2 : shifts) {
            if (shift2.equals(value)) {
                continue;
            }
            Shift shift = FenixFramework.getDomainObject(shift2);
            Collection<Registration> studentList = shift.getStudents();
            for (Registration registration : studentList) {
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
                if (!infoStudentList.contains(infoStudent)
                        && (distributedTest == null || !StudentTestQuestion
                                .hasStudentTestQuestions(registration, distributedTest))) {
                    infoStudentList.add(infoStudent);
                }
            }

        }
        return infoStudentList;
    }

    private List<InfoStudent> returnStudentsFromStudentsArray(DistributedTest distributedTest, String[] students,
            String executionCourseId) throws FenixServiceException {
        final String value = BundleUtil.getString(Bundle.APPLICATION, "label.allStudents");
        List<InfoStudent> studentsList = new ArrayList<InfoStudent>();
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);

        for (String student : students) {
            if (student.equals(value)) {
                Collection<Attends> attendList = executionCourse.getAttends();
                for (Attends attend : attendList) {
                    InfoStudent infoStudent = InfoStudent.newInfoFromDomain(attend.getRegistration());
                    if (!studentsList.contains(infoStudent)
                            && (distributedTest == null || !StudentTestQuestion.hasStudentTestQuestions(attend.getRegistration(),
                                    distributedTest))) {
                        studentsList.add(infoStudent);
                    }
                }
                break;
            }
            Registration registration = FenixFramework.getDomainObject(student);
            InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
            if (!studentsList.contains(infoStudent)) {
                if (!studentsList.contains(infoStudent)
                        && (distributedTest == null || !StudentTestQuestion
                                .hasStudentTestQuestions(registration, distributedTest))) {
                    studentsList.add(infoStudent);
                }
            }

        }
        return studentsList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsByIdArray serviceInstance = new ReadStudentsByIdArray();

    @Atomic
    public static List<InfoStudent> runReadStudentsByIdArray(String executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, selected, insertByShifts);
    }

    @Atomic
    public static List<InfoStudent> runReadStudentsByIdArray(String executionCourseId, String distributedTestId,
            String[] selected, Boolean insertByShifts) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, selected, insertByShifts);
    }

}