/*
 * Created on 2/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class EditSectionServiceTest extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditSectionServiceTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditSection";
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

		ISuportePersistente sp = null;
		IDisciplinaExecucao executionCourse = null;
		InfoSection infoSection = null;
		InfoSection oldInfoSection = null;
		InfoSection newInfoSection = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();

			executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"PO",
					"2002/2003",
					"LEEC");

			IPersistentSite persistentSite = sp.getIPersistentSite();
			ISite site = persistentSite.readByExecutionCourse(executionCourse);

			IPersistentSection persistentSection = sp.getIPersistentSection();
			ISection fatherSection =
				persistentSection.readBySiteAndSectionAndName(
					site,
					null,
					"Seccao1dePO");
					
			ISection section =
							persistentSection.readBySiteAndSectionAndName(
								site,
								fatherSection,
								"SubSeccao2dePO");

			sp.confirmarTransaccao();
			
			oldInfoSection = Cloner.copyISection2InfoSection(section);
			newInfoSection = Cloner.copyISection2InfoSection(section);
			newInfoSection.setName("NewSectionName");
			newInfoSection.setSectionOrder(new Integer(0));

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		Object[] args = { oldInfoSection, newInfoSection };
		return args;
	}

}
