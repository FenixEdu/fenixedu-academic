/*
 * Created on Nov 3, 2003
 *  
 */

package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarksToString implements IService {

	public ReadDistributedTestMarksToString() {
	}

	public String run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException {
		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = (IDistributedTest) persistentSuport
					.getIPersistentDistributedTest().readByOID(
							DistributedTest.class, distributedTestId);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();
//			int numberOfQuestions = distributedTest.getNumberOfQuestions()
//					.intValue();
			String result = new String("Número\tNome\t");
			for (int i = 1; i <= distributedTest.getNumberOfQuestions()
					.intValue(); i++) {
				result = result.concat("P" + i + "\t");
			}
			result = result.concat("Nota");
			List studentTestQuestionList = persistentSuport
					.getIPersistentStudentTestQuestion().readByDistributedTest(
							distributedTest);
			if (studentTestQuestionList == null
					|| studentTestQuestionList.size() == 0)
				throw new FenixServiceException();
			Iterator it = studentTestQuestionList.iterator();
			int questionIndex = 0;
			Double maximumMark = persistentSuport
					.getIPersistentStudentTestQuestion()
					.getMaximumDistributedTestMark(distributedTest);
			if (maximumMark.doubleValue() > 0)
				result = result.concat("\tNota (%)\n");
			else
				result = result.concat("\n");
			Double finalMark = new Double(0);
			DecimalFormat df = new DecimalFormat("#0.##");
			DecimalFormat percentageFormat = new DecimalFormat("#%");
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it
						.next();
				if (questionIndex == 0) {
					result = result.concat(studentTestQuestion.getStudent()
							.getNumber()
							+ "\t"
							+ studentTestQuestion.getStudent().getPerson()
									.getNome() + "\t");
				}
				result = result.concat(df.format(studentTestQuestion
						.getTestQuestionMark())
						+ "\t");
				finalMark = new Double(finalMark.doubleValue()
						+ studentTestQuestion.getTestQuestionMark()
								.doubleValue());
				questionIndex++;
				if (questionIndex == distributedTest.getNumberOfQuestions()
						.intValue()) {
					if (finalMark.doubleValue() < 0)
						result = result.concat("0\t");
					else
						result = result.concat(df.format(finalMark
								.doubleValue())
								+ "\t");
					if (maximumMark.doubleValue() > 0) {
						double finalMarkPercentage = finalMark.doubleValue()
								* java.lang.Math.pow(maximumMark.doubleValue(),
										-1);
						if (finalMarkPercentage < 0) {
							result = result.concat("0%");
						} else
							result = result.concat(percentageFormat
									.format(finalMarkPercentage));
					}
					result = result.concat("\n");
					finalMark = new Double(0);
					questionIndex = 0;
				}
			}
			return result;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	public String run(Integer executionCourseId, String[] distributedTestCodes)
			throws FenixServiceException {
		ISuportePersistente persistentSuport;
		try {
			String result = new String("Número\tNome\t");
			persistentSuport = SuportePersistenteOJB.getInstance();
			IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
					.getIPersistentExecutionCourse().readByOID(
							ExecutionCourse.class, executionCourseId);
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();
			IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
					.getIPersistentStudentTestQuestion();
			IPersistentDistributedTest persistentDistributedTest = persistentSuport
					.getIPersistentDistributedTest();

			List studentsFromAttendsList = (List) CollectionUtils.collect(
					persistentSuport.getIFrequentaPersistente()
							.readByExecutionCourse(executionCourse),
					new Transformer() {

						public Object transform(Object input) {
							return ((IFrequenta) input).getAluno();
						}
					});
			List distributedTestIdsList = new ArrayList();
			CollectionUtils
					.addAll(distributedTestIdsList, distributedTestCodes);
			List studentsFromTestsList = persistentStudentTestQuestion
					.readStudentsByDistributedTests(distributedTestIdsList);
			List studentList = concatStudentsLists(studentsFromAttendsList,
					studentsFromTestsList);
			Double[] maxValues = new Double[distributedTestCodes.length];

			for (int i = 0; i < distributedTestCodes.length; i++) {
				IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest
						.readByOID(DistributedTest.class, new Integer(
								distributedTestCodes[i]));
				if (distributedTest == null)
					throw new InvalidArgumentsServiceException();
				maxValues[i] = persistentStudentTestQuestion
						.getMaximumDistributedTestMark(new Integer(
								distributedTestCodes[i]));
				result = result.concat(distributedTest.getTitle() + "\t");
				if (maxValues[i].doubleValue() > 0)
					result = result.concat("%\t");
			}

			Iterator it = studentList.iterator();

			while (it.hasNext()) {
				result = result.concat("\n");
				IStudent student = (IStudent) it.next();
				result = result.concat(student.getNumber() + "\t"
						+ student.getPerson().getNome() + "\t");

				for (int i = 0; i < distributedTestCodes.length; i++) {

					Double finalMark = new Double(0);
					DecimalFormat df = new DecimalFormat("#0.##");
					DecimalFormat percentageFormat = new DecimalFormat("#%");

					finalMark = persistentStudentTestQuestion
							.readStudentTestFinalMark(new Integer(
									distributedTestCodes[i]), student
									.getIdInternal());

					if (finalMark == null) {
						result = result.concat("NA\t");
						if (maxValues[i].doubleValue() > 0)
							result = result.concat("NA\t");
					} else {
						if (finalMark.doubleValue() < 0)
							result = result.concat("0\t");
						else {
							result = result.concat(df.format(finalMark
									.doubleValue())
									+ "\t");
						}
						if (maxValues[i].doubleValue() > 0) {
							double finalMarkPercentage = finalMark
									.doubleValue()
									* java.lang.Math.pow(maxValues[i]
											.doubleValue(), -1);
							if (finalMarkPercentage < 0)
								result = result.concat("0%\t");
							else
								result = result.concat(percentageFormat
										.format(finalMarkPercentage)
										+ "\t");
						}
					}
				}
			}
			return result;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private List concatStudentsLists(List list1, List list2) {
		Iterator it = list2.iterator();
		while (it.hasNext()) {
			IStudent student = (IStudent) it.next();
			if (!list1.contains(student))
				list1.add(student);
		}
		Collections.sort(list1, new BeanComparator("number"));
		return list1;
	}
}