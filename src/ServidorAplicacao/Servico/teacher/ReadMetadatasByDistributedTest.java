/*
 * Created on Oct 22, 2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadMetadatasByDistributedTest implements IService {

	private String path = new String();

	public ReadMetadatasByDistributedTest() {
	}

	public SiteView run(Integer executionCourseId, Integer distributedTestId,
			String path) throws FenixServiceException {
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB
					.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
					.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
					.readByOID(ExecutionCourse.class, executionCourseId);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentMetadata persistentMetadata = persistentSuport
					.getIPersistentMetadata();

			IPersistentDistributedTest persistentDistributedTest = persistentSuport
					.getIPersistentDistributedTest();

			IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest
					.readByOID(DistributedTest.class, distributedTestId);

			if (distributedTest == null) {
				throw new InvalidArgumentsServiceException();
			}
			List metadatas = new ArrayList();
			metadatas = persistentSuport
					.getIPersistentMetadata()
					.readByExecutionCourseAndNotDistributedTest(distributedTest);
			List result = new ArrayList();
			Iterator iter = metadatas.iterator();
			while (iter.hasNext())
				result.add(Cloner.copyIMetadata2InfoMetadata((IMetadata) iter
						.next()));

			InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
			bodyComponent.setInfoMetadatas(result);
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