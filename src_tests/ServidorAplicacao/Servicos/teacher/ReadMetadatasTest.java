/*
 * Created on 11/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMetadatas;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatasTest extends ServiceNeedsAuthenticationTestCase {

    public ReadMetadatasTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadMetadatasTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadMetadatas";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "D2543", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "L48283", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34882);
        String path = new String("e:\\eclipse\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, path };
        return args;

    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();
            SiteView siteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            InfoSiteMetadatas bodyComponent = (InfoSiteMetadatas) siteView.getComponent();

            InfoExecutionCourse infoExecutionCourse = bodyComponent.getExecutionCourse();
            assertEquals(infoExecutionCourse.getIdInternal(), args[0]);
            List infoMetadatasList = bodyComponent.getInfoMetadatas();
            assertEquals(infoMetadatasList.size(), 68);

            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Read Metadatas " + ex);
        } catch (Exception ex) {
            fail("Read Metadatas " + ex);
        }
    }
}