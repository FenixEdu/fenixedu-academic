/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteTests;
import DataBeans.InfoTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadTests implements IService {

	public ReadTests() {
	}

	public SiteView run(Integer executionCourseId) throws FenixServiceException {

		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
					.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
					.readByOID(ExecutionCourse.class, executionCourseId);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTest persistentTest = persistentSuport
					.getIPersistentTest();
			List tests = persistentTest.readByTestScopeObject(executionCourse);
			List result = new ArrayList();
			Iterator iter = tests.iterator();
			while (iter.hasNext()) {
				ITest test = (ITest) iter.next();
				InfoTest infoTest = InfoTest.newInfoFromDomain(test);
				result.add(infoTest);
			}
			InfoSiteTests bodyComponent = new InfoSiteTests();
			bodyComponent.setInfoTests(result);
			bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner
					.get(executionCourse));
			SiteView siteView = new ExecutionCourseSiteView(bodyComponent,
					bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}