/*
 * Created on 17/Nov/2003
 *  
 */

package ServidorAplicacao.Servicos.grant.contract;

import java.util.Calendar;
import java.util.Date;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantResponsibleTeacher;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;

/**
 * @author Barbosa
 * @author Pica
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
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditGrantContract";
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
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
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
        calendar.set(Calendar.DAY_OF_MONTH, 12);
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
	 * (non-Javadoc)
	 * 
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
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateBegin = calendar.getTime();
        calendar.getTimeZone();
        responsibleTeacherInfo.setBeginDate(dateBegin);
        orientationTeacherInfo.setBeginDate(dateBegin);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2004);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateEnd = calendar.getTime();
        calendar.getTimeZone();
        responsibleTeacherInfo.setEndDate(dateEnd);
        orientationTeacherInfo.setEndDate(dateEnd);

        responsibleTeacherInfo.setResponsibleTeacherInfo(responsibleTeacher);
        orientationTeacherInfo.setOrientationTeacherInfo(orientationTeacher);

        Object[] args = getArguments(grantType, responsibleTeacherInfo, orientationTeacherInfo);
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownType()
    {
        Object[] args = getAuthorizeArguments();
        InfoGrantContract infoGrantContract = (InfoGrantContract) args[0];
        infoGrantContract.getGrantTypeInfo().setSigla("G");
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownResponsibleTeacher()
    {
        Object[] args = getAuthorizeArguments();
        InfoGrantContract infoGrantContract = (InfoGrantContract) args[0];
        infoGrantContract.getGrantResponsibleTeacherInfo().getResponsibleTeacherInfo().setTeacherNumber(
            new Integer(69));
        return args;
    }

    protected Object[] getUnauthorizeArgumentsUnknownGrantOrientationTeacher()
    {
        Object[] args = getAuthorizeArguments();
        InfoGrantContract infoGrantContract = (InfoGrantContract) args[0];
        infoGrantContract.getGrantOrientationTeacherInfo().getOrientationTeacherInfo().setTeacherNumber(
            new Integer(69));
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit()
    {
        Object[] args = getAuthorizeArguments();
        InfoGrantContract infoGrantContract = (InfoGrantContract) args[0];
        infoGrantContract.getGrantTypeInfo().setSigla("M");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateEnd = calendar.getTime();
        infoGrantContract.setDateEndContract(dateEnd);
        infoGrantContract.setIdInternal(new Integer(1));
        infoGrantContract.getGrantResponsibleTeacherInfo().setIdInternal(new Integer(1));
        infoGrantContract.getGrantOrientationTeacherInfo().setIdInternal(new Integer(1));
        return args;
    }

    protected Object[] getUnauthorizeArgumentsEdit()
    {
        Object[] args = getAuthorizeArguments();
        InfoGrantContract infoGrantContract = (InfoGrantContract) args[0];
        infoGrantContract.getGrantOwnerInfo().setIdInternal(new Integer(2));
        infoGrantContract.setIdInternal(new Integer(1));

        Object[] args2 = { infoGrantContract };
        return args2;
    }

    /** ********** Inicio dos testes ao serviço ************* */

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

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

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
	 * Grant Contract Edition Successfull
	 */
    public void testEditGrantContractSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/grant/contract/testEditGrantContractExpectedDataSet.xml");
            System.out.println(
                getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractSuccessfull");
        } catch (FenixServiceException e)
        {
            fail("Editing a GrantContract successfull " + e);
        } catch (Exception e)
        {
            fail("Editing a GrantContract successfull " + e);
        }
    }

    //    /*
    //   	 * Grant Contract Edition Unsuccessfull: new data conflicts with existing data.
    //     */
    //    public void testEditGrantContractUnsuccessfull()
    //    {
    //        try
    //        {
    //            String[] args = getAuthenticatedAndAuthorizedUser();
    //            IUserView id = authenticateUser(args);
    //            Object[] args2 = getUnauthorizeArgumentsEdit();
    //
    //            Boolean result = (Boolean) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
    //
    //            if (!result.booleanValue())
    //            {
    //                compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
    //                System.out.println(
    //                    getNameOfServiceToBeTested()
    //                        + " was SUCCESSFULY runned by test: testEditGrantContractUnsuccessfull");
    //            } else
    //                fail("Editing a GrantContract unsuccessfull");
    //        } catch (FenixServiceException e)
    //        {
    //            fail("Editing a GrantContract unsuccessfull " + e);
    //        } catch (Exception e)
    //        {
    //            fail("Editing a GrantContract unsuccessfull " + e);
    //        }
    //    }

    /*
	 * Grant Contract Edition Unsuccessfull: unknown grant type
	 */
    public void testCreateGrantContractUnsuccessfullUnknownType()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownType();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Type: test failed!");
        } catch (GrantTypeNotFoundException e)
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
	 * Grant Contract Creation/Edition Unsuccessfull: unknown (responsible) teacher
	 */
    public void testCreateGrantContractUnsuccessfullUnknownResponsibleTeacher()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownResponsibleTeacher();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Responsible Teacher: test failed!");
        } catch (GrantResponsibleTeacherNotFoundException e)
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
	 * Grant Contract Creation/Edition Unsuccessfull: unknown (grant orientation) teacher
	 */
    public void testCreateGrantContractUnsuccessfullUnknownGrantOrientationTeacher()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsUnknownGrantOrientationTeacher();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            fail("Creating a new GrantContract with Unknown Grant Orientation Teacher: test failed!");
        } catch (GrantOrientationTeacherNotFoundException e)
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
