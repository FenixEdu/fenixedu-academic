/*
 * Created on 13/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.person.qualification;

import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.UtilsTestCase;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualificationsTest extends QualificationServiceNeedsAuthenticationTestCase {

    public ReadQualificationsTest(java.lang.String testName) {
        super(testName);
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadQualifications";
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/person/qualification/testReadQualificationsDataSet.xml";
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_GrantOwnerManager()
     */
    protected String[] getAuthorizedUserGrantOwnerManager() {
        String[] args = { "user_gom", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_Teacher()
     */
    protected String[] getAuthorizedUserTeacher() {
        String[] args = { "user_t", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
     */
    protected String[] getUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_GrantOwnerManager()
     */
    protected Object[] getAuthorizeArgumentsGrantOwnerManager() {
        //Grant Owner qualification
        InfoQualification info = new InfoQualification();
        info.setInfoPerson(getInfoPersonGO());

        Object[] args = { null, info };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_Teacher()
     */
    protected Object[] getAuthorizeArgumentsTeacher() {
        //Teacher qualification
        InfoQualification info = new InfoQualification();
        info.setInfoPerson(getInfoPersonT());

        Object[] args = { null, info };
        return args;
    }

    /**
     * 
     * Start of the tests
     *  
     */

    /**
     * A Grant Owner Manager reads all qualifications of a grant owner
     */
    public void testReadQualificationsGOMSuccessfull() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

            List result = null;
            result = (List) ServiceManagerServiceFactory.executeService(user,
                    getNameOfServiceToBeTested(), argserv);

            if (result == null) {
                fail("Reading Qualifications of a GrantOwner: ");
            }

            //Is the data read correct
            Object[] expectedresult = { new Integer(3), new Integer(4) };
            UtilsTestCase.readTestList(result, expectedresult, "idInternal", Qualification.class);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationsDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Reading Qualifications of a GrantOwner: " + e);
        } catch (Exception e) {
            fail("Reading Qualifications of a GrantOwner: " + e);
        }
    }

    /**
     * A Teacher reads all his qualifications
     */
    public void testReadQualificationsTSuccessfull() {
        try {
            String[] args = getAuthorizedUserTeacher();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsTeacher();

            List result = null;
            result = (List) ServiceManagerServiceFactory.executeService(user,
                    getNameOfServiceToBeTested(), argserv);

            //Is the data read correct
            Object[] expectedresult = { new Integer(1), new Integer(2), new Integer(6) };
            UtilsTestCase.readTestList(result, expectedresult, "idInternal", Qualification.class);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationsDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Reading Qualifications of a Teacher: " + e);
        } catch (Exception e) {
            fail("Reading Qualifications of aTeacher: " + e);
        }
    }

    /**
     * Valid user(teacher), but wrong arguments(Grant Owner)
     */
    public void testReadQualificationsUnsuccessfull1() {

        try {
            String[] args = getAuthorizedUserTeacher();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("ReadPersonQualificationsUnsuccessfull.");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationsDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        }
    }

    /**
     * Valid user(Grant Owner), but wrong arguments(Teacher)
     */
    public void testReadQualificationsQualificationUnsuccessfull2() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsTeacher();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("ReadPersonQualificationsUnsuccessfull.");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationsDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        }
    }

    /**
     * Valid user, but wrong arguments (person key id is null)
     */
    public void testQualificationsQualificationUnsuccessfull3() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

            //Invalid qualification
            ((InfoQualification) argserv[1]).getInfoPerson().setIdInternal(null);

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("ReadPersonQualificationsUnsuccessfull");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationsDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("ReadPersonQualificationsUnsuccessfull: " + e);
        }
    }

    /**
     * 
     * End of the tests
     *  
     */

    //Returns a GrantOwnerManager user
    protected InfoPerson getInfoPersonGO() {
        InfoPerson info = new InfoPerson();
        info.setIdInternal(new Integer(14));
        info.setUsername("user_gom");
        return info;
    }

    //Returns a Teacher user
    protected InfoPerson getInfoPersonT() {
        InfoPerson info = new InfoPerson();
        info.setIdInternal(new Integer(18));
        info.setUsername("user_t");
        return info;
    }

}