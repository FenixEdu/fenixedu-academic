package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import Dominio.Site;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class InsertSectionTest extends SectionBelongsExecutionCourseTest {

	/**
	 * @param testName
	 */
	public InsertSectionTest(java.lang.String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertSection";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/dbContentsSection.xml";
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Object[] args = { new Integer(27), null, "novaSeccao", new Integer(0) };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getTestSectionSuccessfullArguments() {
		Object[] args = { new Integer(27), new Integer(6), "novaSubSeccao", new Integer(0) };
		return args;
	}

	protected Object[] getTestSectionUnsuccessfullArguments() {
		Object[] args = { new Integer(27), new Integer(7), "novaSubSeccao", new Integer(0) };
		return args;
	}

	public void testInsertExistingSection() {

		Object[] args = { new Integer(27), null, "Seccao1dePO", new Integer(0) };
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testInsertExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertExistingSection");

		} catch (ExistingServiceException e) {
			compareDataSet(getDataSetFilePath());
			System.out.println(
				"testInsertExistingSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testInsertExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertExistingSection");
		}
	}

	public void testInsertNonExistingSection() {
		
		Object[] args = { new Integer(27), null, "novaSeccao", new Integer(0)};
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);
			
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			ISite iSite = new Site();
			iSite.setIdInternal(new Integer(4));
			iSite = (ISite) persistentSuport.getIPersistentSite().readByOId(iSite, false);
			IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
			ISection iSection = persistentSection.readBySiteAndSectionAndName(iSite, null, "novaSeccao");
			InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
			assertEquals(infoSection.getName(), "novaSeccao");
			assertEquals(infoSection.getSectionOrder(), new Integer(0));
			assertEquals(infoSection.getSuperiorInfoSection(), null);

			
			persistentSection.delete(iSection);
			compareDataSet("etc/datasets/dbContentsInsertSection.xml");
			System.out.println(
				"testInsertNonExistingSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testInsertNonExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertNonExistingSection");
		}
	}
	
	public void testInsertExistingSubSection() {

		Object[] args = { new Integer(27), new Integer(6), "SubSeccao1dePO", new Integer(0) };
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testInsertExistingSubSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertExistingSubSection");

		} catch (ExistingServiceException e) {
			compareDataSet(getDataSetFilePath());
			System.out.println(
				"testInsertExistingSubSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testInsertExistingSubSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertExistingSubSection");
		}
	}

	public void testInsertNonExistingSubSection() {
		
		Object[] args = { new Integer(27), new Integer(6), "novaSubSeccao", new Integer(0)};
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);
			
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			ISite iSite = new Site();
			iSite.setIdInternal(new Integer(4));
			iSite = (ISite) persistentSuport.getIPersistentSite().readByOId(iSite, false);
			ISection iSection = new Section();
			iSection.setIdInternal(new Integer(6));
			iSection = (ISection) persistentSuport.getIPersistentSection().readByOId(iSection, false);
			IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
			ISection newISection = persistentSection.readBySiteAndSectionAndName(iSite, iSection, "novaSeccao");
			InfoSection infoSection = Cloner.copyISection2InfoSection(newISection);
			assertEquals(infoSection.getName(), "novaSubSeccao");
			assertEquals(infoSection.getSectionOrder(), new Integer(0));
			assertEquals(infoSection.getSuperiorInfoSection(), iSection);
	
			persistentSection.delete(iSection);
			compareDataSet("etc/datasets/dbContentsInsertSubSection.xml");
			System.out.println(
				"testInsertNonExistingSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testInsertNonExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testInsertNonExistingSection");
		}
	}

}