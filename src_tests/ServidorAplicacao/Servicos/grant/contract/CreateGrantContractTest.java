/*
 * Created on 17/Nov/2003
 *
 */

package ServidorAplicacao.Servicos.grant.contract;

import java.util.Calendar;
import java.util.Date;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantResponsibleTeacher;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author  Barbosa
 * @author  Pica
 *
 */

public class CreateGrantContractTest
    extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    /**
     * @param testName
     */
    public CreateGrantContractTest(String testName)
    {
        super(testName);
    }

    /*
    *  (non-Javadoc)
    * @see ServiceNeedsAuthenticationTestCase#getApplication()
    */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "CreateGrantContract";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testCreateGrantContractDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/contract/testCreateGrantContractExpectedDataSet.xml";
    }

    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    protected Object[] getArguments(
        InfoGrantType grantType,
        InfoGrantResponsibleTeacher responsibleTeacher,
        InfoGrantOrientationTeacher orientationTeacher)
    {
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2003);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateBeginContract = calendar.getTime();

        calendar.set(Calendar.YEAR, 2004);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateEndContract = calendar.getTime();

        infoGrantOwner.setGrantOwnerNumber(new Integer(629));
        infoGrantOwner.setIdInternal(new Integer(2));

        infoGrantContract.setContractNumber(new Integer(2));
        infoGrantContract.setDateBeginContract(dateBeginContract);
        infoGrantContract.setDateEndContract(dateEndContract);

        infoGrantContract.setGrantOwnerInfo(infoGrantOwner);
        infoGrantContract.setGrantTypeInfo(grantType);
        responsibleTeacher.setGrantContractInfo(infoGrantContract);
        orientationTeacher.setGrantContractInfo(infoGrantContract);
        infoGrantContract.setGrantResponsibleTeacherInfo(responsibleTeacher);
        infoGrantContract.setGrantOrientationTeacherInfo(orientationTeacher);

        Object[] args = { infoGrantContract };
        return args;
    }

    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
        InfoGrantType grantType = new InfoGrantType();
        InfoGrantResponsibleTeacher responsibleTeacherInfo = new InfoGrantResponsibleTeacher();
        InfoGrantOrientationTeacher orientationTeacherInfo = new InfoGrantOrientationTeacher();
        InfoTeacher responsibleTeacher = new InfoTeacher();
        InfoTeacher orientationTeacher = new InfoTeacher();

        grantType.setSigla("M");

        responsibleTeacher.setIdInternal(new Integer(1));
        responsibleTeacher.setTeacherNumber(new Integer(1));

        orientationTeacher.setIdInternal(new Integer(2));
        orientationTeacher.setTeacherNumber(new Integer(6));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2003);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateBegin = calendar.getTime();
        responsibleTeacherInfo.setBeginDate(dateBegin);
        orientationTeacherInfo.setBeginDate(dateBegin);

        responsibleTeacherInfo.setResponsibleTeacherInfo(responsibleTeacher);
        orientationTeacherInfo.setOrientationTeacherInfo(orientationTeacher);

        Object[] args = getArguments(grantType, responsibleTeacherInfo, orientationTeacherInfo);
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownType()
    {

        Object[] args = {
        };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownResponsibleTeacher()
    {

        Object[] args = {
        };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownGrantOrientationTeacher()
    {

        Object[] args = {
        };
        return args;
    }

    /************  Inicio dos testes ao serviço **************/

    /*
     * Grant Contract Creation Successfull
     */
    public void testCreateGrantContractSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            gestor.executar(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractSuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Creating a new GrantContract " + e);
        } catch (Exception e)
        {
            fail("Creating a new GrantContract " + e);
        }
    }

    /*
     * Grant Contract Creation Unsuccessfull: unknown grant type
     */
    public void testCreateGrantContractUnsuccessfullUnknownType()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownType();

            gestor.executar(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Type: test failed!");
        } catch (FenixServiceException e)
        {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractUnsuccessfullUnknownType");
        } catch (Exception e)
        {
            fail("Creating a new GrantContract with Unknown Type" + e);
        }
    }

    /*
     * Grant Contract Creation Unsuccessfull: unknown (responsible) teacher
     */
    public void testCreateGrantContractUnsuccessfullUnknownResponsibleTeacher()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownResponsibleTeacher();

            gestor.executar(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Responsible Teacher: test failed!");
        } catch (FenixServiceException e)
        {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractUnsuccessfullUnknownResponsibleTeacher");
        } catch (Exception e)
        {
            fail("Creating a new GrantContract with Unknown Responsible Teacher " + e);
        }
    }

    /*
     * Grant Contract Creation Unsuccessfull: unknown (grant orientation) teacher
     */
    public void testCreateGrantContractUnsuccessfullUnknownGrantOrientationTeacher()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownGrantOrientationTeacher();

            gestor.executar(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Grant Orientation Teacher: test failed!");
        } catch (FenixServiceException e)
        {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractUnsuccessfullUnknownGrantOrientationTeacher");
        } catch (Exception e)
        {
            fail("Creating a new GrantContract with Unknown Grant Orientation Teacher " + e);
        }
    }
}
