/*
 * Created on 31/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class EditItemServiceTest extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditItemServiceTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditItem";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		InfoCurriculum oldCurriculum = new InfoCurriculum();
		InfoCurriculum newCurriculum = new InfoCurriculum();
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		InfoSection infoSection = null;
		InfoItem oldInfoItem = null;
		InfoItem newInfoItem = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();

			executionPeriod =
				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			IPersistentSite persistentSite =
				sp.getIPersistentSite();

			ISite site =
				persistentSite.readByExecutionCourse(executionCourse);
			
			IPersistentSection persistentSection =
							sp.getIPersistentSection();

			ISection section =
							persistentSection.readBySiteAndSectionAndName(site,null,"Seccao1deTFCI");
			
			IPersistentItem persistentItem=sp.getIPersistentItem();
			IItem item=persistentItem.readBySectionAndName(section,"Item1deTFCI");			
			sp.confirmarTransaccao();

			infoSection = Cloner.copyISection2InfoSection(section);
			oldInfoItem= Cloner.copyIItem2InfoItem(item);
			newInfoItem= Cloner.copyIItem2InfoItem(item);
			newInfoItem.setName("NewItemName");
			
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		Object[] args = {infoSection, oldInfoItem, newInfoItem };
		return args;
	}

}
