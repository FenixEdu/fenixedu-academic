/*
 * Created on 20/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.DistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest implements IServico {

	private static ReadDistributedTest service = new ReadDistributedTest();

	public static ReadDistributedTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadDistributedTest";
	}
	public SiteView run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException {

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
			IPersistentDistributedTest persistentDistrubutedTest =
				persistentSuport.getIPersistentDistributedTest();

			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentDistrubutedTest.readByOId(
					distributedTest,
					false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			InfoDistributedTest infoDistributedTest =
				Cloner.copyIDistributedTest2InfoDistributedTest(
					distributedTest);
			InfoSiteDistributedTest bodyComponent =
				new InfoSiteDistributedTest();
			bodyComponent.setInfoDistributedTest(infoDistributedTest);
			bodyComponent.setExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));
			SiteView siteView =
				new ExecutionCourseSiteView(bodyComponent, bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
