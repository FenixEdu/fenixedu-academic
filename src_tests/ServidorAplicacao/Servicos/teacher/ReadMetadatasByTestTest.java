/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatasByTestTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadMetadatasByTestTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadMetadatasByTest";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(25), new Integer(7)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();

		InfoExecutionCourse infoExecutionCourse = null;
		InfoMetadata infoMetadata = null;

		List infoMetadatas = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentMetadata persistentMetadata =
				sp.getIPersistentMetadata();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(25));

			IMetadata metadata = new Metadata(new Integer(1));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			metadata =
				(IMetadata) persistentMetadata.readByOId(metadata, false);
			assertNotNull("metadata null", metadata);
			sp.confirmarTransaccao();
			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
			ParseMetadata p = new ParseMetadata();
			try {
				infoMetadata =
					p.parseMetadata(metadata.getMetadataFile(), infoMetadata);
			} catch (Exception e) {
				fail("exception: ExcepcaoPersistencia ");
			}
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}

		bodyComponent.setExecutionCourse(infoExecutionCourse);
		infoMetadatas.add(infoMetadata);
		bodyComponent.setInfoMetadatas(infoMetadatas);
		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;

	}

	protected boolean needsAuthorization() {
		return true;
	}
}
