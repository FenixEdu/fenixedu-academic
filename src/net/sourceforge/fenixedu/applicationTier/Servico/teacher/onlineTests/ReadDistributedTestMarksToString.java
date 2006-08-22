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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarksToString extends Service {

    public String run(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException, ExcepcaoPersistencia {
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        StringBuilder result = new StringBuilder();
        result.append("Número\tNome\t");
        for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++) {
            result.append("P");
            result.append(i);
            result.append("\t");
        }
        result.append("Nota");
        Set<StudentTestQuestion> studentTestQuestionList = distributedTest.getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder();
        if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
            throw new FenixServiceException();
        int questionIndex = 0;
        Double maximumMark = distributedTest.calculateMaximumDistributedTestMark();
        if (maximumMark.doubleValue() > 0)
            result.append("\tNota (%)\n");
        else
            result.append("\n");
        Double finalMark = new Double(0);
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormat percentageFormat = new DecimalFormat("#%");
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (questionIndex == 0) {
                result.append(studentTestQuestion.getStudent().getNumber());
                result.append("\t");
                result.append(studentTestQuestion.getStudent().getPerson().getNome());
                result.append("\t");
            }
            result.append(df.format(studentTestQuestion.getTestQuestionMark()));
            result.append("\t");
            finalMark = new Double(finalMark.doubleValue()
                    + studentTestQuestion.getTestQuestionMark().doubleValue());
            questionIndex++;
            if (questionIndex == distributedTest.getNumberOfQuestions().intValue()) {
                if (finalMark.doubleValue() < 0)
                    result.append("0\t");
                else {
                    result.append(df.format(finalMark.doubleValue()));
                    result.append("\t");
                }
                if (maximumMark.doubleValue() > 0) {
                    double finalMarkPercentage = finalMark.doubleValue()
                            * java.lang.Math.pow(maximumMark.doubleValue(), -1);
                    if (finalMarkPercentage < 0) {
                        result.append("0%");
                    } else
                        result.append(percentageFormat.format(finalMarkPercentage));
                }
                result.append("\n");
                finalMark = new Double(0);
                questionIndex = 0;
            }
        }
        return result.toString();
    }

    public String run(Integer executionCourseId, String[] distributedTestCodes)
            throws FenixServiceException, ExcepcaoPersistencia {
        StringBuilder result = new StringBuilder();
        result.append("Número\tNome\t");

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        List<Registration> studentsFromAttendsList = (List) CollectionUtils.collect(executionCourse
                .getAttends(), new Transformer() {

            public Object transform(Object input) {
                return ((Attends) input).getAluno();
            }
        });

        final Set<Registration> students = new HashSet<Registration>();
        for (final String distributedTestCode : distributedTestCodes) {
            final Integer distributedTestID = Integer.valueOf(distributedTestCode);
            final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestID);
            students.addAll(distributedTest.findStudents());
        }

        List<Registration> studentList = concatStudentsLists(studentsFromAttendsList, students);
        Double[] maxValues = new Double[distributedTestCodes.length];

        for (int i = 0; i < distributedTestCodes.length; i++) {
            DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(Integer.valueOf(distributedTestCodes[i]));
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            maxValues[i] = distributedTest.calculateMaximumDistributedTestMark();
            result.append(distributedTest.getTitle());
            result.append("\t");
            if (maxValues[i].doubleValue() > 0)
                result.append("%\t");
        }

        for (Registration registration : studentList) {
            result.append("\n");
            result.append(registration.getNumber());
            result.append("\t");
            result.append(registration.getPerson().getNome());
            result.append("\t");

            for (int i = 0; i < distributedTestCodes.length; i++) {

                Double finalMark = new Double(0);
                DecimalFormat df = new DecimalFormat("#0.##");
                DecimalFormat percentageFormat = new DecimalFormat("#%");

                final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(Integer.valueOf(distributedTestCodes[i]));
                finalMark = distributedTest.calculateTestFinalMarkForStudent(registration);

                if (finalMark == null) {
                    result.append("NA\t");
                    if (maxValues[i].doubleValue() > 0)
                        result.append("NA\t");
                } else {
                    if (finalMark.doubleValue() < 0)
                        result.append("0\t");
                    else {
                        result.append(df.format(finalMark.doubleValue()));
                        result.append("\t");
                    }
                    if (maxValues[i].doubleValue() > 0) {
                        double finalMarkPercentage = finalMark.doubleValue()
                                * java.lang.Math.pow(maxValues[i].doubleValue(), -1);
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
            if (!sortedStudents.contains(registration))
                sortedStudents.add(registration);
        }

        Collections.sort(sortedStudents, new BeanComparator("number"));
        return sortedStudents;
    }
}