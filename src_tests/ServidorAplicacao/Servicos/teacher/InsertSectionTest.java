package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import Dominio.Site;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
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
        return "etc/datasets/servicos/teacher/testInsertSectionDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "13", "pass", getApplication() };
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
        Object[] args = { new Integer(27), new Integer(6), "novaSubSeccao",
                new Integer(0) };
        return args;
    }

    protected Object[] getTestSectionUnsuccessfullArguments() {
        Object[] args = { new Integer(27), new Integer(8), "novaSubSeccao",
                new Integer(0) };
        return args;
    }

    public void testInsertExistingSection() {

        Object[] args = { new Integer(27), null, "Seccao1dePO", new Integer(0) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            System.out
                    .println("testInsertExistingSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertExistingSection");

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertExistingSectionDataSet.xml");
            System.out
                    .println("testInsertExistingSection was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertExistingSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertExistingSection");
        }
    }

    public void testInsertNonExistingSectionBeforeFirst() {

        Object[] args = { new Integer(27), null, "novaSeccao", new Integer(0) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            persistentSuport.iniciarTransaccao();

            ISite iSite = (ISite) persistentSuport.getIPersistentSite()
                    .readByOID(Site.class, new Integer(4));
            IPersistentSection persistentSection = persistentSuport
                    .getIPersistentSection();
            ISection iSection = persistentSection.readBySiteAndSectionAndName(
                    iSite, null, "novaSeccao");
            InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
            assertEquals(infoSection.getName(), "novaSeccao");
            assertEquals(infoSection.getSectionOrder(), new Integer(0));
            assertEquals(infoSection.getSuperiorInfoSection(), null);

            persistentSection.delete(iSection);
            persistentSuport.confirmarTransaccao();

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertSectionBeforeFirstDataSet.xml");
            System.out
                    .println("testInsertNonExistingSectionBeforeFirst was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertNonExistingSectionBeforeFirst was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertNonExistingSectionBeforeFirst");
        }
    }

    public void testInsertNonExistingSectionInMiddle() {

        Object[] args = { new Integer(27), null, "novaSeccao", new Integer(1) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            persistentSuport.iniciarTransaccao();

            ISite iSite = (ISite) persistentSuport.getIPersistentSite()
                    .readByOID(Site.class, new Integer(4));
            IPersistentSection persistentSection = persistentSuport
                    .getIPersistentSection();
            ISection iSection = persistentSection.readBySiteAndSectionAndName(
                    iSite, null, "novaSeccao");
            InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
            assertEquals(infoSection.getName(), "novaSeccao");
            assertEquals(infoSection.getSectionOrder(), new Integer(1));
            assertEquals(infoSection.getSuperiorInfoSection(), null);

            persistentSection.delete(iSection);
            persistentSuport.confirmarTransaccao();

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertSectionInMiddleDataSet.xml");
            System.out
                    .println("testInsertNonExistingSectionInMiddle was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertNonExistingSectionInMiddle was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertNonExistingSectionInMiddle");
        }
    }

    public void testInsertNonExistingSectionBeforeEnd() {

        Object[] args = { new Integer(27), null, "novaSeccao", new Integer(2) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            persistentSuport.iniciarTransaccao();

            ISite iSite = (ISite) persistentSuport.getIPersistentSite()
                    .readByOID(Site.class, new Integer(4));
            IPersistentSection persistentSection = persistentSuport
                    .getIPersistentSection();
            ISection iSection = persistentSection.readBySiteAndSectionAndName(
                    iSite, null, "novaSeccao");
            InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
            assertEquals(infoSection.getName(), "novaSeccao");
            assertEquals(infoSection.getSectionOrder(), new Integer(2));
            assertEquals(infoSection.getSuperiorInfoSection(), null);

            persistentSection.delete(iSection);
            persistentSuport.confirmarTransaccao();

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertSectionBeforeEndDataSet.xml");
            System.out
                    .println("testInsertNonExistingSectionBeforeEnd was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertNonExistingSectionBeforeEnd was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertNonExistingSectionBeforeEnd");
        }
    }

    public void testInsertExistingSubSection() {

        Object[] args = { new Integer(27), new Integer(6), "SubSeccao1dePO",
                new Integer(0) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            System.out
                    .println("testInsertExistingSubSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertExistingSubSection");

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertExistingSubSectionDataSet.xml");
            System.out
                    .println("testInsertExistingSubSection was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertExistingSubSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertExistingSubSection");
        }
    }

    public void testInsertNonExistingSubSection() {

        Object[] args = { new Integer(27), new Integer(6), "novaSubSeccao",
                new Integer(0) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            persistentSuport.iniciarTransaccao();

            ISite iSite = (ISite) persistentSuport.getIPersistentSite()
                    .readByOID(Site.class, new Integer(4));

            ISection iSection = (ISection) persistentSuport
                    .getIPersistentSection().readByOID(Section.class,
                            new Integer(6));
            InfoSection infoSuperiorSection = Cloner
                    .copyISection2InfoSection(iSection);

            IPersistentSection persistentSection = persistentSuport
                    .getIPersistentSection();
            ISection newISection = persistentSection
                    .readBySiteAndSectionAndName(iSite, iSection,
                            "novaSubSeccao");
            InfoSection infoSection = Cloner
                    .copyISection2InfoSection(newISection);

            assertEquals(infoSection.getName(), "novaSubSeccao");
            assertEquals(infoSection.getSectionOrder(), new Integer(0));
            assertEquals(infoSection.getSuperiorInfoSection(),
                    infoSuperiorSection);

            persistentSection.delete(newISection);
            persistentSuport.confirmarTransaccao();

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedInsertSubSectionDataSet.xml");
            System.out
                    .println("testInsertNonExistingSubSection was SUCCESSFULY runned by class: "
                            + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testInsertNonExistingSubSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testInsertNonExistingSubSection");
        }
    }

    public void testNonExistingSuperiorSection() {

        Object serviceArguments[] = { new Integer(27), new Integer(100),
                "novaSubSeccao", new Integer(0) };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), serviceArguments);

            fail(getNameOfServiceToBeTested()
                    + "fail testNonExistingSuperiorSection");

        } catch (NotAuthorizedException ex) {
            System.out
                    .println("testNonExistingSuperiorSection was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testNonExistingSuperiorSection was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }

    }

}