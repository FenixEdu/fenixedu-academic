/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoCoordinator;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.UtilsTestCase;

/**
 * fenix-head ServidorAplicacao.Servicos.coordinator
 * 
 * @author João Mota 6/Nov/2003
 *  
 */
public class ReadCoordinationTeamTestCase extends CoordinatorBelongsToExecutionDegree {

    protected void setUp() {
        super.setUp();
    }

    /**
     * @param name
     */
    public ReadCoordinationTeamTestCase(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadCoordinationTeam";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        Object[] args = { new Integer(10) };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.coordinator.CoordinatorBelongsToExecutionDegree#getNonAuthorizeArguments()
     */
    protected Object[] getNonAuthorizeArguments() {
        Object[] args = { new Integer(12) };
        return args;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/coordinator/testReadCoordinatorsDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/coordinator/testReadCoordinatorsDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testReadCorrectNumberOfCoordinators() {
        Object serviceArguments[] = getAuthorizeArguments();
        List coordinators;
        try {
            coordinators = (List) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), serviceArguments);
            String result = "testReadCoordinators was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested() + "- number of coordinators expected:";
            assertEquals(result, new Integer(coordinators.size()), new Integer(2));
            System.out.println("COORDINATORS_->" + coordinators);
            Object[] ids = { new Integer(1), new Integer(3) };
            UtilsTestCase.readTestList(coordinators, ids, "idInternal", InfoCoordinator.class);
            System.out.println("testReadCoordinators was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            ex.printStackTrace();
            System.out.println("testReadCoordinators was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + ": fail testAuthorizedUser");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadCoordinators was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());
        }
    }
}