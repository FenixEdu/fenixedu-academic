/*
 * Created on 27/Out/2003
 *
 */

package ServidorAplicacao.Servicos.grant.owner;

import java.util.Date;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantOwnerTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public EditGrantOwnerTest(java.lang.String testName) {
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
        return "EditGrantOwner";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/owner/testCreateGrantOwnerDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/owner/testCreateGrantOwnerExpectedDataSet.xml";
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

    protected InfoPerson GenerateNewPersonToTest() {

        InfoPerson person = new InfoPerson("1818181", new TipoDocumentoIdentificacao(
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", null, null, "Grant Owen",
                new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.SOLTEIRO), null, "Father",
                "Mother", "Portuguesa", "Freguesia", "Concelho", "Distrito", "Morada", "localidade",
                "1700-200", "l200", "frequesia morada", "concelho morada", "distrito morada",
                "214443523", "96546321", "grant@spdi.ist.c", null, "1111111111", "Profissao", "17",
                "pass", new InfoCountry("Portugal", "PT", "Portuguesa"), "1111111111");
        return person;
    }

    protected InfoPerson GenerateExistingPersonToTest() {

        InfoPerson person = new InfoPerson("7712345", new TipoDocumentoIdentificacao(
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", null, null, "Joaninha",
                new Sexo(Sexo.FEMININO), new EstadoCivil(EstadoCivil.SOLTEIRO), null, null,
                "Mae da Joaninha", null, null, null, null, null, null, null, null, null, null, null,
                null, "962833109", "jccm@mega", null, null, "Eng. Informatica", "jccm",
                "1a1dc91c907325c69271ddf0c944bc72", new InfoCountry("Portugal", "PT", "Portuguesa"),
                "968763320");
        person.setIdInternal(new Integer(7));
        return person;
    }

    protected InfoGrantOwner GenerateInfoGrantOwnerToTest(boolean personExists, boolean grantOwnerExists) {
        InfoGrantOwner grantOwner = new InfoGrantOwner();
        InfoPerson person = new InfoPerson();
        Date dateSendCGD = null;
        Integer cardCopy = new Integer(1);
        Integer existingGrantOwnerNumberUpdate = new Integer(52);
        Integer keyPerson = new Integer(14);

        if (grantOwnerExists) {
            person.setIdInternal(keyPerson);
            person.setNumeroDocumentoIdentificacao("161616161");
            person.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(1));

            grantOwner.setGrantOwnerNumber(existingGrantOwnerNumberUpdate);
        } else {
            if (personExists)
                person = GenerateExistingPersonToTest();
            else
                person = GenerateNewPersonToTest();
        }
        grantOwner.setCardCopyNumber(cardCopy);
        grantOwner.setDateSendCGD(dateSendCGD);
        grantOwner.setPersonInfo(person);
        return grantOwner;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        InfoGrantOwner newInfoGrantOwner = GenerateInfoGrantOwnerToTest(false, false);

        Object[] args = { newInfoGrantOwner };
        return args;
    }

    protected Object[] getAuthorizeArgumentsWithExistingPerson() {
        InfoGrantOwner newInfoGrantOwner = GenerateInfoGrantOwnerToTest(true, false);

        Object[] args = { newInfoGrantOwner };
        return args;
    }

    protected Object[] getAuthorizeArgumentsWithExistingGrantOwner() {
        InfoGrantOwner newInfoGrantOwner = GenerateInfoGrantOwnerToTest(true, true);

        Object[] args = { newInfoGrantOwner };
        return args;
    }

    /** ********** Inicio dos testes ao serviço************* */

    /*
     * Create a Grant Owner Successfully With a New Person
     */
    public void testCreateGrantOwnerSuccessfullWithNewPerson() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (FenixServiceException e) {
            fail("Creating a new GrantOwner With a New Person " + e);
        } catch (Exception e) {
            fail("Creating a new GrantOwner With a New Person " + e);
        }
    }

    /*
     * Create a Grant Owner Successfully With an Existing Person
     */
    //    public void testCreateGrantOwnerSuccessfullWithExistingPerson() {
    //        try {
    //            String[] args = getAuthenticatedAndAuthorizedUser();
    //            IUserView id = authenticateUser(args);
    //            Object[] args2 = getAuthorizeArgumentsWithExistingPerson();
    //
    //            ServiceManagerServiceFactory.executeService(id,
    //                    getNameOfServiceToBeTested(), args2);
    //
    //            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets_templates/servicos/grant/owner/testCreateGrantOwnerWithExistingPersonExpectedDataSet.xml");
    //            System.out.println(getNameOfServiceToBeTested()
    //                    + " was SUCCESSFULY runned by class: "
    //                    + this.getClass().getName());
    //        } catch (FenixServiceException e) {
    //            fail("Creating a new GrantOwner With an Existing Person " + e);
    //        } catch (Exception e) {
    //            fail("Creating a new GrantOwner With an Existing Person " + e);
    //        }
    //    }
    /*
     * Create a GrantOwner Successfully With an Existing GrantOwner. (Edit
     * GrantOwner)
     *  
     */
    //    public void testCreateGrantOwnerSuccessfullWithExistingGrantOwner() {
    //        try {
    //            String[] args = getAuthenticatedAndAuthorizedUser();
    //            IUserView id = authenticateUser(args);
    //            Object[] args2 = getAuthorizeArgumentsWithExistingGrantOwner();
    //
    //            ServiceManagerServiceFactory.executeService(id,
    //                    getNameOfServiceToBeTested(), args2);
    //
    //            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets_templates/servicos/grant/owner/testCreateGrantOwnerWithExistingGrantOwnerExpectedDataSet.xml");
    //            System.out.println(getNameOfServiceToBeTested()
    //                    + " was UNSUCCESSFULY runned by class: "
    //                    + this.getClass().getName());
    //        } catch (ExistingServiceException e) {
    //            fail("testCreateGrantOwnerUnsuccessfull failed to run Service "
    //                    + getNameOfServiceToBeTested() + e);
    //        } catch (Exception e) {
    //            fail("testCreateGrantOwnerUnsuccessfull failed " + e);
    //        }
    //    }
}