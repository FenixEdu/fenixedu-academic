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
public class ReadMetadatasByTestTest extends ServiceNeedsAuthenticationTestCase {

    public ReadMetadatasByTestTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadMetadatasByTestTestDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadMetadatasByTest";
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
        Integer testId = new Integer(109);
        String order = new String("blá");
        String asc = null; //new String("false");
        Object[] args = { executionCourseId, testId, order, asc };
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
            assertEquals(infoMetadatasList.size(), 1);
        } catch (FenixServiceException ex) {
            fail("Read Metadatas By Test " + ex);
        } catch (Exception ex) {
            fail("Read Metadatas By Test " + ex);
        }
    }
}