/*
 * Created on 7/Nov/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.grant.owner;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.UtilsTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class SearchGrantOwnerTest extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public SearchGrantOwnerTest(java.lang.String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "SearchGrantOwner";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/owner/testSearchGrantOwnerDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "16", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        String name = "Nuno";
        Object[] args = { name, null, null, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByIDAndGetOneResult() {
        String idNumber = "123456789";
        Integer idType = new Integer(1);
        Object[] args = { null, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameAndIDAndGetOneResult() {
        String name = "Jorge";
        String idNumber = "9876543210";
        Integer idType = new Integer(1);
        Object[] args = { name, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameGetOneResultGrantOwner() {
        String name = "Bolseiro1";
        Object[] args = { name, null, null, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByIDGetOneResultGrantOwner() {
        String idNumber = "17171717";
        Integer idType = new Integer(1);
        Object[] args = { null, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameAndIDGetOneResultGrantOwner() {
        String name = "Bolseiro2";
        String idNumber = "17171717";
        Integer idType = new Integer(1);
        Object[] args = { name, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameGetSeveralResults() {
        String name = "Nome da Pessoa";
        Object[] args = { name, null, null, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameAndIDGetSeveralResults() {
        String name = "Nome da Pessoa";
        String idNumber = "17171717";
        Integer idType = new Integer(2);
        Object[] args = { name, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameUnsuccessfull() {
        String name = "Mr. Phoenix";
        Object[] args = { name, null, null, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByIDUnsuccessfull() {
        String idNumber = "69";
        Integer idType = new Integer(3);
        Object[] args = { null, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    protected Object[] getArgumentsToSearchByNameAndIDUnsuccessfull() {
        String name = "Mr.Phoenix";
        String idNumber = "1024";
        Integer idType = new Integer(1);
        Object[] args = { name, idNumber, idType, null, new Boolean(false) };
        return args;
    }

    /*
     * Search by Name successfull with one result: a person not grantOwner.
     */
    public void testSearchByNameSuccessfullPersonNotGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by name a person that is NOT a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(6);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameSuccessfullPersonNotGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name a person that is NOT a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by name a person that is NOT a grantOwner " + e);
        }
    }

    /*
     * Search by IDnumber and IDtype successfull with one result: a person not
     * grantOwner.
     */
    public void testSearchByIDNumberAndIDTypeSuccessfullPersonNotGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByIDAndGetOneResult();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by ID a person that is NOT a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(1);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByIDNumberAndIDTypeSuccessfullPersonNotGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by ID a person that is NOT a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by ID a person that is NOT a grantOwner " + e);
        }
    }

    /*
     * Search by Name, IDnumber and IDtype successfull with one result: a person
     * not grantOwner.
     */
    public void testSearchByNameAndIDNumberAndIDTypeSuccessfullPersonNotGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameAndIDAndGetOneResult();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by name and ID a person that is NOT a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(2);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameAndIDNumberAndIDTypeSuccessfullPersonNotGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name and ID a person that is NOT a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by name and ID a person that is NOT a grantOwner " + e);
        }
    }

    /*
     * Search by Name successfull with one result: a person that is also a
     * grantOwner.
     */
    public void testSearchByNameSuccessfullPersonIsGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameGetOneResultGrantOwner();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by name a person that IS ALSO a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(14);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameSuccessfullPersonIsGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name a person that IS ALSO a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by name a person that ID ALSO a grantOwner " + e);
        }
    }

    /*
     * Search by IDnumber and IDtype successfull with one result: a person that
     * is also a grantOwner.
     */
    public void testSearchByIDNumberAndIDTypeSuccessfullPersonIsGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByIDGetOneResultGrantOwner();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by ID a person that IS ALSO a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(15);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByIDNumberAndIDTypeSuccessfullPersonIsGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by ID a person that IS ALSO a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by ID a person that ID ALSO a grantOwner " + e);
        }
    }

    /*
     * Search by Name, IDnumber and IDtype successfull with one result: a person
     * that is also a grantOwner.
     */
    public void testSearchByNameAndIDNumberAndIDTypeSuccessfullPersonIsGrantOwner() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameAndIDGetOneResultGrantOwner();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() > 1)
                fail("Searching by name and ID a person that IS ALSO a grantOwner: more than ONE result!!");
            //Check the search result
            Integer personId = new Integer(15);
            Object[] values = { personId };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameAndIDNumberAndIDTypeSuccessfullPersonIsGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            fail("Searching by name and ID a person that IS ALSO a grantOwner " + e);
        } catch (Exception e) {
            fail("Searching by name and ID a person that ID ALSO a grantOwner " + e);
        }
    }

    /*
     * Search by Name successfull with several results.
     */
    public void testSearchByNameSuccessfullSeveralPerson() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameGetSeveralResults();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() <= 1)
                fail("Searching by name expecting several results: ONE or NO results!!");
            //Check the search result
            Object[] values = { new Integer(1), new Integer(3), new Integer(13) };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testSearchByNameSuccessfullSeveralPerson was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name expecting several results " + e);
        } catch (Exception e) {
            fail("Searching by name expecting several results " + e);
        }
    }

    /*
     * Search by Name, IDnumber and IDtype successfull with several results.
     */
    public void testSearchByNameAndIDNumberAndIDTypeSuccessfullSeveralPerson() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameAndIDGetSeveralResults();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns only ONE result
            if (result.size() <= 1)
                fail("Searching by name and ID expecting several results: ONE or NO results!!");
            //Check the search result
            Object[] values = { new Integer(1), new Integer(3), new Integer(13) };
            UtilsTestCase.readTestList(result, values, "personInfo.idInternal", InfoGrantOwner.class);
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameAndIDNumberAndIDTypeSuccessfullSeveralPerson was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name and ID expecting several results " + e);
        } catch (Exception e) {
            fail("Searching by name and ID expecting several results " + e);
        }
    }

    /*
     * Search by Name unsuccessfull: no person found.
     */
    public void testSearchByNameUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameUnsuccessfull();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns ZERO results
            if (result != null && result.size() != 0)
                fail("Searching by name unsuccessfull: should retrieve NO result ");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testSearchByNameUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name unsuccessfull " + e);
        } catch (Exception e) {
            fail("Searching by name unsuccessfull " + e);
        }
    }

    /*
     * Search by IDnumber and IDtype unsuccessfull: no person found.
     */
    public void testSearchByIDNumberAndIDTypeUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByIDUnsuccessfull();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns ZERO results
            if (result != null && result.size() != 0)
                fail("Searching by ID unsuccessfull: should retrieve NO result ");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testSearchByIDNumberAndIDTypeUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by ID unsuccessfull " + e);
        } catch (Exception e) {
            fail("Searching by ID unsuccessfull " + e);
        }
    }

    /*
     * Search by Name and IDnumber and IDtype unsuccessfull: no person found.
     */
    public void testSearchByNameAndIDNumberAndIDTypeUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getArgumentsToSearchByNameAndIDUnsuccessfull();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check that service returns ZERO results
            if (result != null && result.size() != 0)
                fail("Searching by name and ID unsuccessfull: should retrieve NO result ");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testSearchByNameAndIDNumberAndIDTypeUnsuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Searching by name and ID unsuccessfull " + e);
        } catch (Exception e) {
            fail("Searching by name andID unsuccessfull " + e);
        }
    }
}