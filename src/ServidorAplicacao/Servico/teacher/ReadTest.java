/*
 * Created on 31/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoSiteTest;
import DataBeans.InfoTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTest implements IServico {
	private static ReadTest service = new ReadTest();
	private String path = new String();

	public static ReadTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadTest";
	}

	public SiteView run(Integer executionCourseId, Integer testId, String path)
		throws FenixServiceException {
		this.path = path.replace('\\', '/');
		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse =
				new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List questions = persistentTestQuestion.readByTest(test);
			List result = new ArrayList();
			Iterator iter = questions.iterator();
			ParseQuestion parse = new ParseQuestion();
			while (iter.hasNext()) {
				ITestQuestion testQuestion = (ITestQuestion) iter.next();
				InfoTestQuestion infoTestQuestion =
					Cloner.copyITestQuestion2InfoTestQuestion(testQuestion);
				try {
					infoTestQuestion.setQuestion(
						parse.parseQuestion(
							infoTestQuestion.getQuestion().getXmlFile(),
							infoTestQuestion.getQuestion(),
							path));
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}
				result.add(infoTestQuestion);
			}
			InfoSiteTest infoSiteTest = new InfoSiteTest();
			infoSiteTest.setInfoTestQuestions(result);
			infoSiteTest.setInfoTest(Cloner.copyITest2InfoTest(test));
			infoSiteTest.setExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));
			SiteView siteView =
				new ExecutionCourseSiteView(infoSiteTest, infoSiteTest);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
