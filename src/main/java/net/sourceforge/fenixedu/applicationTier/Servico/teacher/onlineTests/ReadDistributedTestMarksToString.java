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
 * Created on Nov 3, 2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarksToString {

    protected String run(String executionCourseId, String distributedTestId) throws FenixServiceException {
        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        StringBuilder result = new StringBuilder();
        result.append("Número\tNome\t");
        for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++) {
            result.append("P").append(i).append("\t");
        }
        result.append("Nota");

        Double maximumMark = distributedTest.calculateMaximumDistributedTestMark();
        if (maximumMark.doubleValue() > 0) {
            result.append("\tNota (%)");
        }

        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormat percentageFormat = new DecimalFormat("#%");

        for (Registration registration : distributedTest.findStudents()) {
            result.append("\n").append(registration.getNumber());
            result.append("\t");
            result.append(registration.getStudent().getPerson().getName());
            result.append("\t");
            Double finalMark = new Double(0);
            for (StudentTestQuestion studentTestQuestion : distributedTest.findStudentTestQuestions(registration)) {
                result.append(df.format(studentTestQuestion.getTestQuestionMark()));
                result.append("\t");
                finalMark = new Double(finalMark.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
            }
            if (finalMark.doubleValue() < 0) {
                result.append("0\t");
            } else {
                result.append(df.format(finalMark.doubleValue()));
                result.append("\t");
            }
            if (maximumMark.doubleValue() > 0) {
                double finalMarkPercentage = finalMark.doubleValue() * java.lang.Math.pow(maximumMark.doubleValue(), -1);
                if (finalMarkPercentage < 0) {
                    result.append("0%");
                } else {
                    result.append(percentageFormat.format(finalMarkPercentage));
                }
            }
        }
        return result.toString();
    }

    protected String run(String executionCourseId, String[] distributedTestCodes) throws FenixServiceException {
        StringBuilder result = new StringBuilder();
        result.append("Número\tNome\t");

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        List<Registration> studentsFromAttendsList =
                (List) CollectionUtils.collect(executionCourse.getAttends(), new Transformer() {

                    @Override
                    public Object transform(Object input) {
                        return ((Attends) input).getRegistration();
                    }
                });

        final Set<Registration> students = new HashSet<Registration>();
        for (final String distributedTestCode : distributedTestCodes) {
            final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
            students.addAll(distributedTest.findStudents());
        }

        List<Registration> studentList = concatStudentsLists(studentsFromAttendsList, students);
        Double[] maxValues = new Double[distributedTestCodes.length];

        for (int i = 0; i < distributedTestCodes.length; i++) {
            DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCodes[i]);
            if (distributedTest == null) {
                throw new InvalidArgumentsServiceException();
            }
            maxValues[i] = distributedTest.calculateMaximumDistributedTestMark();
            result.append(distributedTest.getTitle());
            result.append("\t");
            if (maxValues[i].doubleValue() > 0) {
                result.append("%\t");
            }
        }

        for (Registration registration : studentList) {
            result.append("\n");
            result.append(registration.getNumber());
            result.append("\t");
            result.append(registration.getPerson().getName());
            result.append("\t");

            for (int i = 0; i < distributedTestCodes.length; i++) {

                Double finalMark = new Double(0);
                DecimalFormat df = new DecimalFormat("#0.##");
                DecimalFormat percentageFormat = new DecimalFormat("#%");

                final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCodes[i]);
                finalMark = distributedTest.calculateTestFinalMarkForStudent(registration);

                if (finalMark == null) {
                    result.append("NA\t");
                    if (maxValues[i].doubleValue() > 0) {
                        result.append("NA\t");
                    }
                } else {
                    if (finalMark.doubleValue() < 0) {
                        result.append("0\t");
                    } else {
                        result.append(df.format(finalMark.doubleValue()));
                        result.append("\t");
                    }
                    if (maxValues[i].doubleValue() > 0) {
                        double finalMarkPercentage = finalMark.doubleValue() * java.lang.Math.pow(maxValues[i].doubleValue(), -1);
                        if (finalMarkPercentage < 0) {
                            result.append("0%\t");
                        } else {
                            result.append(percentageFormat.format(finalMarkPercentage));
                            result.append("\t");
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    private List<Registration> concatStudentsLists(Collection<Registration> list1, Collection<Registration> list2) {

        List<Registration> sortedStudents = new ArrayList<Registration>();
        sortedStudents.addAll(list1);

        for (Registration registration : list2) {
            if (!sortedStudents.contains(registration)) {
                sortedStudents.add(registration);
            }
        }

        Collections.sort(sortedStudents, new BeanComparator("number"));
        return sortedStudents;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTestMarksToString serviceInstance = new ReadDistributedTestMarksToString();

    @Atomic
    public static String runReadDistributedTestMarksToString(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

    @Atomic
    public static String runReadDistributedTestMarksToString(String executionCourseId, String[] distributedTestCodes)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestCodes);
    }

}