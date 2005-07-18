/*
 * Created on Nov 3, 2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarksToString implements IService {

    public String run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport;

        persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        StringBuffer result = new StringBuffer();
        result.append("Número\tNome\t");
        for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++) {
            result.append("P");
            result.append(i);
            result.append("\t");
        }
        result.append("Nota");
        List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion().readByDistributedTest(
                distributedTest.getIdInternal());
        if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
            throw new FenixServiceException();
        int questionIndex = 0;
        Double maximumMark = persistentSuport.getIPersistentStudentTestQuestion().getMaximumDistributedTestMark(distributedTest.getIdInternal());
        if (maximumMark.doubleValue() > 0)
            result.append("\tNota (%)\n");
        else
            result.append("\n");
        Double finalMark = new Double(0);
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormat percentageFormat = new DecimalFormat("#%");
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (questionIndex == 0) {
                result.append(studentTestQuestion.getStudent().getNumber());
                result.append("\t");
                result.append(studentTestQuestion.getStudent().getPerson().getNome());
                result.append("\t");
            }
            result.append(df.format(studentTestQuestion.getTestQuestionMark()));
            result.append("\t");
            finalMark = new Double(finalMark.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
            questionIndex++;
            if (questionIndex == distributedTest.getNumberOfQuestions().intValue()) {
                if (finalMark.doubleValue() < 0)
                    result.append("0\t");
                else {
                    result.append(df.format(finalMark.doubleValue()));
                    result.append("\t");
                }
                if (maximumMark.doubleValue() > 0) {
                    double finalMarkPercentage = finalMark.doubleValue() * java.lang.Math.pow(maximumMark.doubleValue(), -1);
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

    public String run(Integer executionCourseId, String[] distributedTestCodes) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport;
        StringBuffer result = new StringBuffer();
        result.append("Número\tNome\t");
        persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
        IPersistentDistributedTest persistentDistributedTest = persistentSuport.getIPersistentDistributedTest();

        List<IStudent> studentsFromAttendsList = (List) CollectionUtils.collect(persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
                executionCourseId), new Transformer() {

            public Object transform(Object input) {
                return ((IAttends) input).getAluno();
            }
        });
        List<Integer> distributedTestIdsList = new ArrayList<Integer>();
        CollectionUtils.addAll(distributedTestIdsList, distributedTestCodes);
        List<IStudent> studentsFromTestsList = persistentStudentTestQuestion.readStudentsByDistributedTests(distributedTestIdsList);
        List<IStudent> studentList = concatStudentsLists(studentsFromAttendsList, studentsFromTestsList);
        Double[] maxValues = new Double[distributedTestCodes.length];

        for (int i = 0; i < distributedTestCodes.length; i++) {
            IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, new Integer(
                    distributedTestCodes[i]));
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            maxValues[i] = persistentStudentTestQuestion.getMaximumDistributedTestMark(new Integer(distributedTestCodes[i]));
            result.append(distributedTest.getTitle());
            result.append("\t");
            if (maxValues[i].doubleValue() > 0)
                result.append("%\t");
        }

        for (IStudent student : studentList) {
            result.append("\n");
            result.append(student.getNumber());
            result.append("\t");
            result.append(student.getPerson().getNome());
            result.append("\t");

            for (int i = 0; i < distributedTestCodes.length; i++) {

                Double finalMark = new Double(0);
                DecimalFormat df = new DecimalFormat("#0.##");
                DecimalFormat percentageFormat = new DecimalFormat("#%");

                finalMark = persistentStudentTestQuestion.readStudentTestFinalMark(new Integer(distributedTestCodes[i]), student.getIdInternal());

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

    private List<IStudent> concatStudentsLists(List<IStudent> list1, List<IStudent> list2) {
        for (IStudent student : list2) {
            if (!list1.contains(student))
                list1.add(student);
        }
        Collections.sort(list1, new BeanComparator("number"));
        return list1;
    }
}