package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class DeleteSectionTest extends SectionBelongsExecutionCourseTest {

    /**
     * @param testName
     */
    public DeleteSectionTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteSection";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testDeleteSectionDataSet.xml";
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
        Object[] args = { new Integer(27), new Integer(6) };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected Object[] getTestSectionSuccessfullArguments() {
        Object[] args = { new Integer(27), new Integer(6) };
        return args;
    }

    protected Object[] getTestSectionUnsuccessfullArguments() {
        Object[] args = { new Integer(27), new Integer(7) };
        return args;
    }

    public void testDeleteExistingSection() {
        Object[] args = getTestSectionSuccessfullArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedDeleteSectionDataSet.xml");
            System.out.println("testDeleteExistingSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testDeleteExistingSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testDeleteExistingSection");
        }
    }

    public void testDeleteNonExistingSection() {
        Object[] args = { new Integer(27), new Integer(100) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println("testDeleteExistingSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testDeleteNonExistingSection");

        } catch (NotAuthorizedException e) {
            System.out.println("testDeleteNonExistingSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testDeleteNonExistingSection was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testDeleteNonExistingSection");
        }
    }

}