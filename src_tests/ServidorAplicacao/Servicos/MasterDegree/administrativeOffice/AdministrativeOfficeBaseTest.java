/*
 * Created on Oct 30, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public abstract class AdministrativeOfficeBaseTest extends ServiceTestCase {

    protected String dataSetFilePath;

    protected IUserView userView = null;

    protected IUserView userViewNotAuthorized = null;

    /**
     * @param name
     */
    public AdministrativeOfficeBaseTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        userView = this.authenticateUser(getAuthenticatedAndAuthorizedUser());
        userViewNotAuthorized = this.authenticateUser(getAuthenticatedAndNotAuthorizedUser());
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "f3667", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndNotAuthorizedUser() {
        String[] args = { "f3614", "pass", getApplication() };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    /**
     * @param strings
     * @return
     */
    private IUserView authenticateUser(String[] args) {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            sp.clearCache();
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("Cache cleaning failed!" + ex);
            return null;
        }
        //SuportePersistenteOJB.resetInstance();

        try {
            return (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", args);
        } catch (Exception ex) {
            fail("Authenticating User!" + ex);
            return null;
        }
    }

    public void testNotAuthenticatedExecution() {
        try {
            ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(),
                    getServiceArgumentsForNotAuthenticatedUser());
            fail("testNotAuthenticatedExecution did not throw NotAuthorizedException");

        } catch (NotAuthorizedException ex) {
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testNotAuthenticatedExecution " + ex.getMessage());
        }

    }

    public void testNotAuthorizedExecution() {
        try {
            ServiceManagerServiceFactory.executeService(userViewNotAuthorized,
                    getNameOfServiceToBeTested(), getServiceArgumentsForNotAuthorizedUser());
            fail("testNotAuthorizedExecution did not throw NotAuthorizedException");

        } catch (NotAuthorizedException ex) {
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testNotAuthenticatedExecution " + ex.getMessage());
        }

    }

    protected abstract String getNameOfServiceToBeTested();

    protected abstract Object[] getServiceArgumentsForNotAuthenticatedUser()
            throws FenixServiceException;

    protected abstract Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException;

}