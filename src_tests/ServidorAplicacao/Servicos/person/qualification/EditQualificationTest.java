/*
 * Created on 05/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.person.qualification;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditQualificationTest extends QualificationServiceNeedsAuthenticationTestCase {

    public EditQualificationTest(java.lang.String testName) {
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
        return "EditQualification";
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/person/qualification/testEditQualificationDataSet.xml";
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
        //Grant Owner Qualification
        InfoQualification info = new InfoQualification();
        info.setIdInternal(new Integer(2));
        info.setMark("mark");
        info.setSchool("tagus");
        info.setTitle("title");
        info.setInfoPerson(getInfoPersonGO());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_Teacher()
     */
    protected Object[] getAuthorizeArgumentsTeacher() {
        //Teacher qualification
        InfoQualification info = new InfoQualification();
        info.setIdInternal(new Integer(1));
        info.setMark("mark");
        info.setSchool("tagus");
        info.setTitle("Sr. Dr. Eng.");
        info.setInfoPerson(getInfoPersonT());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    protected Object[] getAuthorizeArgumentsCreateQualificationGrantOwner() {
        //Grant Owner qualification
        InfoQualification info = new InfoQualification();
        info.setMark("mark");
        info.setSchool("tagus");
        info.setTitle("title");
        info.setInfoPerson(getInfoPersonGO());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    protected Object[] getAuthorizeArgumentsCreateQualificationTeacher() {
        //Teacher qualification
        InfoQualification info = new InfoQualification();
        info.setMark("mark");
        info.setSchool("tagus");
        info.setTitle("Sr. Dr. Eng.");
        info.setInfoPerson(getInfoPersonT());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEditQualificationGrantOwner() {
        //Grant Owner Qualification that already exists
        InfoQualification info = new InfoQualification();
        info.setIdInternal(new Integer(2));
        info.setSchool("NewSchool");
        info.setMark("mark");
        info.setTitle("title");
        info.setInfoPerson(getInfoPersonGO());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEditQualificationTeacher() {
        //Teacher qualification that already exists
        InfoQualification info = new InfoQualification();
        info.setIdInternal(new Integer(1));
        info.setSchool("tagus");
        info.setMark("mark");
        info.setTitle("title");
        info.setInfoPerson(getInfoPersonT());

        Object[] args = { info.getIdInternal(), info };
        return args;
    }

    /**
     * 
     * Start of the tests
     *  
     */

    /**
     * A Grant Owner Manager creates a qualification of a Grant Owner
     */
    public void testCreateQualificationGOMSuccessfull() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsCreateQualificationGrantOwner();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditCreateGOMQualificationSuccesfullDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Creating a new Qualification for GrantOwner: " + e);
        } catch (Exception e) {
            fail("Creating a new Qualification for GrantOwner: " + e);
        }
    }

    /**
     * A Grant Owner Manager edits a qualification of a Grant Owner
     */
    public void testEditQualificationGOMSuccessfull() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsEditQualificationGrantOwner();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationGOMSuccesfullDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Editing a Qualification for a GrantOwner: " + e);
        } catch (Exception e) {
            fail("Editing a Qualification for a GrantOwner: " + e);
        }
    }

    /**
     * A Teacher creates a own qualification
     */
    public void testCreateQualificationTSuccessfull() {
        try {
            String[] args = getAuthorizedUserTeacher();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsCreateQualificationTeacher();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditCreateTQualificationSuccesfullDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Creating a new Qualification for Teacher: " + e);
        } catch (Exception e) {
            fail("Creating a new Qualification for Teacher: " + e);
        }
    }

    /**
     * A Teacher edits a own qualification
     */
    public void testEditQualificationTSuccessfull() {
        try {
            String[] args = getAuthorizedUserTeacher();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsEditQualificationTeacher();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationTSuccesfullDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException e) {
            fail("Editing a Qualification for a teacher: " + e);
        } catch (Exception e) {
            fail("Editing a Qualification for a teacher: " + e);
        }
    }

    /**
     * Valid user(teacher), but wrong arguments(Grant Owner)
     */
    public void testCreateQualificationUnsuccessfull1() {

        try {
            String[] args = getAuthorizedUserTeacher();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsCreateQualificationGrantOwner();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("CreateQualificationUnsuccessfull.");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        }
    }

    /**
     * Valid user(Grant Owner Manager), but wrong arguments(Teacher)
     */
    public void testCreateQualificationUnsuccessfull2() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsCreateQualificationTeacher();

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("CreateQualificationUnsuccessfull.");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        }
    }

    /**
     * Valid user, but wrong arguments (editing a qualification that does't
     * exists)
     */
    public void testCreateQualificationUnsuccessfull3() {
        try {
            String[] args = getAuthorizedUserGrantOwnerManager();
            IUserView user = authenticateUser(args);
            Object[] argserv = getAuthorizeArgumentsEditQualificationGrantOwner();

            //Invalid qualification
            argserv[0] = new Integer(1220);
            ((InfoQualification) argserv[1]).setIdInternal(new Integer(1220));

            ServiceManagerServiceFactory.executeService(user, getNameOfServiceToBeTested(), argserv);

            fail("CreateQualificationUnsuccessfull.");

        } catch (NotAuthorizedException e) {
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        } catch (Exception e) {
            fail("CreateQualificationUnsuccessfull: " + e);
        }

    }

    /**
     * 
     * End of the tests
     *  
     */

    //Return a valid GrantOwner Manager user
    protected InfoPerson getInfoPersonGO() {
        InfoPerson info = new InfoPerson();
        info.setIdInternal(new Integer(14));
        info.setUsername("user_gom");
        return info;
    }

    //Return a valid Teacher user
    protected InfoPerson getInfoPersonT() {
        InfoPerson info = new InfoPerson();
        info.setIdInternal(new Integer(18));
        info.setUsername("user_t");
        return info;
    }

}