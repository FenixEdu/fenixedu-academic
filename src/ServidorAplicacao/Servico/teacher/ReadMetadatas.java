/*
 * Created on 23/Jul/2003
 *  
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.comparators.QuestionDifficultyTypeComparatorByAscendingOrder;
import DataBeans.comparators.QuestionDifficultyTypeComparatorByDescendingOrder;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatas implements IService {
	private String path = new String();

	public ReadMetadatas() {
	}

	public SiteView run(Integer executionCourseId, String order, String asc,
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
			List metadatas = new ArrayList();
			if (order == null
					|| !(order.equals("description")
							|| order.equals("mainSubject")
							|| order.equals("difficulty") || order
							.equals("numberOfMembers")))
				order = new String("description");
			metadatas = persistentMetadata
					.readByExecutionCourseAndVisibilityAndOrder(
							executionCourse, order, asc);
			List result = new ArrayList();
			Iterator iter = metadatas.iterator();
			while (iter.hasNext())
				result.add(Cloner.copyIMetadata2InfoMetadata((IMetadata) iter
						.next()));
			if (order.equals("difficulty")) {
				if (asc != null && asc.equals("false"))
					Collections
							.sort(
									result,
									new QuestionDifficultyTypeComparatorByDescendingOrder());
				else
					Collections
							.sort(
									result,
									new QuestionDifficultyTypeComparatorByAscendingOrder());
			}
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