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

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatasTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadMetadatasTest(String testName) {
		super(testName);

	}

	protected String getNameOfServiceToBeTested() {
		return "ReadMetadatas";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26)};
		return args;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
		InfoExecutionCourse infoExecutionCourse = null;
		List infoMetadatas = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IExecutionCourse executionCourse =
				new ExecutionCourse(new Integer(26));
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull("executionCourse null", executionCourse);
			IPersistentMetadata persistentMetadata =
				sp.getIPersistentMetadata();

			IMetadata metadata1 = new Metadata(new Integer(2));
			metadata1 =
				(IMetadata) persistentMetadata.readByOId(metadata1, false);
			assertNotNull("metadata null", metadata1);
			IMetadata metadata2 = new Metadata(new Integer(3));
			metadata2 =
				(IMetadata) persistentMetadata.readByOId(metadata2, false);
			assertNotNull("metadata null", metadata2);
			IMetadata metadata3 = new Metadata(new Integer(4));
			metadata3 =
				(IMetadata) persistentMetadata.readByOId(metadata3, false);
			assertNotNull("metadata null", metadata3);
			IMetadata metadata4 = new Metadata(new Integer(5));
			metadata4 =
				(IMetadata) persistentMetadata.readByOId(metadata4, false);
			assertNotNull("metadata null", metadata4);
			IMetadata metadata5 = new Metadata(new Integer(6));
			metadata5 =
				(IMetadata) persistentMetadata.readByOId(metadata5, false);
			assertNotNull("metadata null", metadata5);
			IMetadata metadata6 = new Metadata(new Integer(7));
			metadata6 =
				(IMetadata) persistentMetadata.readByOId(metadata6, false);
			assertNotNull("metadata null", metadata6);
			IMetadata metadata7 = new Metadata(new Integer(8));
			metadata7 =
				(IMetadata) persistentMetadata.readByOId(metadata7, false);
			assertNotNull("metadata null", metadata7);

			sp.confirmarTransaccao();
			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
			InfoMetadata infoMetadata1 =
				Cloner.copyIMetadata2InfoMetadata(metadata1);
			InfoMetadata infoMetadata2 =
				Cloner.copyIMetadata2InfoMetadata(metadata2);
			InfoMetadata infoMetadata3 =
				Cloner.copyIMetadata2InfoMetadata(metadata3);
			InfoMetadata infoMetadata4 =
				Cloner.copyIMetadata2InfoMetadata(metadata4);
			InfoMetadata infoMetadata5 =
				Cloner.copyIMetadata2InfoMetadata(metadata5);
			InfoMetadata infoMetadata6 =
				Cloner.copyIMetadata2InfoMetadata(metadata6);
			InfoMetadata infoMetadata7 =
				Cloner.copyIMetadata2InfoMetadata(metadata7);

			ParseMetadata p = new ParseMetadata();
			try {
				infoMetadata1 =
					p.parseMetadata(metadata1.getMetadataFile(), infoMetadata1,"");
				infoMetadata2 =
					p.parseMetadata(metadata2.getMetadataFile(), infoMetadata2,"");
				infoMetadata3 =
					p.parseMetadata(metadata3.getMetadataFile(), infoMetadata3,"");
				infoMetadata4 =
					p.parseMetadata(metadata4.getMetadataFile(), infoMetadata4,"");
				infoMetadata5 =
					p.parseMetadata(metadata5.getMetadataFile(), infoMetadata5,"");
				infoMetadata6 =
					p.parseMetadata(metadata6.getMetadataFile(), infoMetadata6,"");
				infoMetadata7 =
					p.parseMetadata(metadata7.getMetadataFile(), infoMetadata7,"");
			} catch (Exception e) {
				fail("exception: ExcepcaoPersistencia ");
			}
			infoMetadatas.add(infoMetadata1);
			infoMetadatas.add(infoMetadata2);
			infoMetadatas.add(infoMetadata3);
			infoMetadatas.add(infoMetadata4);
			infoMetadatas.add(infoMetadata5);
			infoMetadatas.add(infoMetadata6);
			infoMetadatas.add(infoMetadata7);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		bodyComponent.setExecutionCourse(infoExecutionCourse);
		bodyComponent.setInfoMetadatas(infoMetadatas);
		SiteView siteView =
			new ExecutionCourseSiteView(bodyComponent, bodyComponent);
		return siteView;
	}
	protected boolean needsAuthorization() {
		return true;
	}
}
