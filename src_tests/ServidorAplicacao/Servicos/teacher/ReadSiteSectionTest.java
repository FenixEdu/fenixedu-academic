/*
 * Created on 20/Out/2003
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class ReadSiteSectionTest extends ServiceNeedsAuthenticationTestCase {

    /**
     *  
     */
    public ReadSiteSectionTest(String name) {
        super(name);
    }

    protected String getNameOfServiceToBeTested() {
        return "TeacherAdministrationSiteComponentService";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadSiteSectionDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionCourseCode = new Integer(27);
        Integer infoSiteCode = new Integer(4);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoSiteSection bodyComponent = new InfoSiteSection();
        Object obj1 = new Integer(6);
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode, obj1,
                obj2 };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testReadSiteSection() {
        TeacherAdministrationSiteView result = null;
        ISection section;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            section = (ISection) persistentSection.readByOID(Section.class, new Integer(6));
            sp.confirmarTransaccao();

            result = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

            InfoSiteSection bodyComponent = (InfoSiteSection) result.getComponent();
            InfoSection infoSection = bodyComponent.getSection();
            ISection iSection = Cloner.copyInfoSection2ISection(infoSection);

            assertTrue(section.equals(iSection));

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedReadSiteSectionDataSet.xml");

            System.out.println("testReadSiteSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadSiteSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadSiteSection");
        }
    }

    public void testReadNonSiteSection() {
        Integer infoExecutionCourseCode = new Integer(27);
        Integer infoSiteCode = new Integer(4);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoSiteSection bodyComponent = new InfoSiteSection();
        Object obj1 = new Integer(7);
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode, obj1,
                obj2 };

        try {

            //result =
            //				(TeacherAdministrationSiteView)
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testReadNonSiteSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadNonSiteSection");
        } catch (Exception ex) {
            System.out.println("testReadNonSiteSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        }
    }

    public void testReadNonExistingSection() {

        Integer infoExecutionCourseCode = new Integer(27);
        Integer infoSiteCode = new Integer(4);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoSiteSection bodyComponent = new InfoSiteSection();
        Object obj1 = new Integer(100);
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode, obj1,
                obj2 };

        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testReadNonExistingSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadNonExistingSection");
        } catch (FenixServiceException ex) {
            System.out.println("testReadNonExistingSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadNonExistingSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadNonExistingSection");
        }
    }

}