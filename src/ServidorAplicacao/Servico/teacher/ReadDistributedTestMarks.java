/*
 * Created on Oct 14, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarks implements IServico {

	private static ReadDistributedTestMarks service =
		new ReadDistributedTestMarks();

	public static ReadDistributedTestMarks getService() {
		return service;
	}

	public String getNome() {
		return "ReadDistributedTestMarks";
	}

	public List run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		List resultList = new ArrayList();
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(distributedTest,	false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			List studentList =(List) persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTest);
			if (studentList == null || studentList.size() == 0)
				throw new FenixServiceException();
			Collections.sort(studentList, new BeanComparator("number"));

			Iterator studentIt = studentList.iterator();
			while (studentIt.hasNext()) {
				IStudent student = (Student) studentIt.next();
				List studentTestQuestionList =(List) persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(student, distributedTest);
				if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
					throw new FenixServiceException();
				Collections.sort(studentTestQuestionList, new BeanComparator("testQuestionOrder"));
				List infoStudentTestQuestionList = new ArrayList();
				Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
				while (studentTestQuestionIt.hasNext()) {
					IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentTestQuestionIt.next();
					InfoStudentTestQuestion infoStudentTestQuestion = Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
					ParseQuestion parse = new ParseQuestion();
					try {
						infoStudentTestQuestion.setQuestion(parse.parseQuestion(infoStudentTestQuestion.getQuestion().getXmlFile(), infoStudentTestQuestion.getQuestion()));
						infoStudentTestQuestion = parse.getOptionsShuffle(infoStudentTestQuestion);
					} catch (Exception e) {
						throw new FenixServiceException(e);
					}
					infoStudentTestQuestionList.add(infoStudentTestQuestion);
				}
				resultList.add(infoStudentTestQuestionList);	
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return resultList;
	}
}
