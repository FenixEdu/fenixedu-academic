/*
 * Created on Oct 28, 2003
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
 *  
 */
public class ReadMetadatasByDistributedTestTest extends ServiceNeedsAuthenticationTestCase {

    public ReadMetadatasByDistributedTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadMetadatasByDistributedTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadMetadatasByDistributedTest";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34033);
        Integer distributedTestId = new Integer(254);
        String path = new String("e:\\eclipse-m8\\workspace\\fenix\\build\\standalone\\");
        Object[] args = { executionCourseId, distributedTestId, path };
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
            assertEquals(infoMetadatasList.size(), 3);
        } catch (FenixServiceException ex) {
            fail("Read Metadatas By Distributed Test" + ex);
        } catch (Exception ex) {
            fail("Read Metadatas By Distributed Test" + ex);
        }
    }
}