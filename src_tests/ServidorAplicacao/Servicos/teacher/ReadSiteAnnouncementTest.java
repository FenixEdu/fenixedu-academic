package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.applicationTier.Servicos.UtilsTestCase;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadSiteAnnouncementTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */

    public ReadSiteAnnouncementTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadSiteAnnouncementDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "TeacherAdministrationSiteComponentService";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "nmsn", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionCourseCode = new Integer(24);
        Integer infoSiteCode = new Integer(1);
        InfoSiteCommon commonComponent = new InfoSiteCommon();
        InfoSiteAnnouncement bodyComponent = new InfoSiteAnnouncement();
        Object obj1 = null;
        Object obj2 = null;

        Object[] args = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode, obj1,
                obj2 };
        return args;
    }

    public void testSuccessfullSiteWithAnnouncements() {

        try {
            SiteView result = null;
            String[] args1 = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args1);

            Object[] args2 = getAuthorizeArguments();

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args2);

            InfoSiteAnnouncement infoSiteAnnouncement = (InfoSiteAnnouncement) result.getComponent();
            List infoAnnouncements = infoSiteAnnouncement.getAnnouncements();
            assertEquals(infoAnnouncements.size(), 4);

            Object[] values = { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
            UtilsTestCase.readTestList(infoAnnouncements, values, "idInternal", InfoAnnouncement.class);

            //check if database hasn't changed
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Reading the Announcements of a Site with announcements" + ex);
        } catch (Exception ex) {
            fail("Reading the Announcements of a Site with announcements" + ex);
        }
    }

    public void testSuccessfullSiteWithoutAnnouncements() {

        try {
            SiteView result = null;
            Integer infoExecutionCourseCode = new Integer(25);
            Integer infoSiteCode = new Integer(2);
            InfoSiteCommon commonComponent = new InfoSiteCommon();
            InfoSiteAnnouncement bodyComponent = new InfoSiteAnnouncement();
            Object obj1 = null;
            Object obj2 = null;

            String[] args1 = getAuthenticatedAndAuthorizedUser();
            Object[] args2 = { infoExecutionCourseCode, commonComponent, bodyComponent, infoSiteCode,
                    obj1, obj2 };

            IUserView userView = authenticateUser(args1);

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args2);

            InfoSiteAnnouncement infoSiteAnnouncement = (InfoSiteAnnouncement) result.getComponent();
            List infoAnnouncements = infoSiteAnnouncement.getAnnouncements();
            assertEquals(infoAnnouncements.size(), 0);
            //check if database hasn't changed
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Reading the Announcements of a Site without announcements" + ex);
        } catch (Exception ex) {
            fail("Reading the Announcements of a Site without announcements" + ex);
        }
    }
}