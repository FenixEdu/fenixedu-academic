/*
 * Created on Oct 30, 2003
 *
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public abstract class AdministrativeOfficeBaseTest extends ServiceTestCase
{

    protected String dataSetFilePath;
    protected GestorServicos serviceManager = GestorServicos.manager();
    protected IUserView userView = null;
    protected IUserView userViewNotAuthorized = null;

    /**
     * @param name
     */
    public AdministrativeOfficeBaseTest(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        super.setUp();
        userView = this.authenticateUser(getAuthenticatedAndAuthorizedUser());
        userViewNotAuthorized = this.authenticateUser(getAuthenticatedAndNotAuthorizedUser());
    }

    protected String getDataSetFilePath()
    {
        return this.dataSetFilePath;
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "f3667", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndNotAuthorizedUser()
    {
        String[] args = { "f3614", "pass", getApplication()};
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    /**
     * @param strings
     * @return
     */
    private IUserView authenticateUser(String[] args)
    {
        SuportePersistenteOJB.resetInstance();

        try
        {
            return (IUserView) gestor.executar(null, "Autenticacao", args);
        } catch (Exception ex)
        {
            fail("Authenticating User!" + ex);
            return null;
        }
    }

    public void testNotAuthenticatedExecution()
    {
        try
        {
            serviceManager.executar(
                null,
                getNameOfServiceToBeTested(),
                getServiceArgumentsForNotAuthenticatedUser());
            fail("testNotAuthenticatedExecution did not throw NotAuthorizedException");

        } catch (NotAuthorizedException ex)
        {
            //ok

        } catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testNotAuthenticatedExecution " + ex.getMessage());
        }

    }

    public void testNotAuthorizedExecution()
    {
        try
        {
            serviceManager.executar(
                userViewNotAuthorized,
                getNameOfServiceToBeTested(),
                getServiceArgumentsForNotAuthorizedUser());
            fail("testNotAuthorizedExecution did not throw NotAuthorizedException");

        } catch (NotAuthorizedException ex)
        {
            //ok

        } catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testNotAuthenticatedExecution " + ex.getMessage());
        }

    }

    protected abstract String getNameOfServiceToBeTested();
    protected abstract Object[] getServiceArgumentsForNotAuthenticatedUser()
        throws FenixServiceException;
    protected abstract Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException;

}
