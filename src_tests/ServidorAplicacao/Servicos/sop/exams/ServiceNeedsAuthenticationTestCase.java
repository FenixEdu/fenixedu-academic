/*
 * Created on 21/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public abstract class ServiceNeedsAuthenticationTestCase extends ServiceTestCase {

    protected IUserView userView1 = null;

    protected IUserView userView2 = null;

    protected ServiceNeedsAuthenticationTestCase(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        userView1 = authenticateUser(getAuthorizedUser());
        userView2 = authenticateUser(getNotSOPEmployeeUser());

    }

    public void testAuthorizedUser() {

        Object serviceArguments[] = getSuccessfullArguments();

        try {
            ServiceUtils.executeService(userView1, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println("testAuthorizedUser was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            fail(getNameOfServiceToBeTested() + "fail testAuthorizedUser");

        } catch (Exception ex) {
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }

    }

    public void testNotSOPEmployeeUser() {

        Object serviceArguments[] = getSuccessfullArguments();

        try {
            ServiceUtils.executeService(userView2, getNameOfServiceToBeTested(), serviceArguments);
            fail(getNameOfServiceToBeTested() + " fail testNotSOPEmployeeUser");

        } catch (NotAuthorizedException ex) {

            System.out.println("testNotSOPEmployeeUser was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (Exception ex) {
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }
    }

    protected IUserView authenticateUser(String[] arguments) {
        SuportePersistenteOJB.resetInstance();
        String args[] = arguments;

        try {
            return (IUserView) ServiceUtils.executeService(null, "Autenticacao", args);
        } catch (Exception ex) {
            fail("Authenticating User!" + ex);
            return null;

        }
    }

    protected abstract String[] getAuthorizedUser();

    protected abstract String[] getNotSOPEmployeeUser();

    protected abstract String getNameOfServiceToBeTested();

    protected abstract Object[] getSuccessfullArguments();

    protected abstract String getDataSetFilePath();

    protected abstract String getApplication();

}