/*
 * Created on 18/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *
 */
public class EditReimbursementGuideTestCase extends ServiceNeedsAuthenticationTestCase
{

    protected void setUp()
    {
        super.setUp();
    }

    /**
     * @param name
     */
    public EditReimbursementGuideTestCase(String name)
    {
        super(name);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested()
    {
        return "EditReimbursementGuide";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testEditReimbursementGuideDataSet.xml";
    }
    protected String getSuccessfullExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testSucessfullEditReimbursementGuideDataSet.xml";
    }

    protected String getUnSuccessfullExpectedDataSetFilePath1()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testEditReimbursementGuideDataSet.xml";
    }
    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "f3667", "pass", getApplication()};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "f3614", "pass", getApplication()};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
        //Integer guideId, String justification, Double value, IUserView userView
        Object[] args = { new Integer(2), "payed", "blabla", new UserView("f3667", null)};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected Object[] getUnAuthorizeArguments1()
    {
        Object[] args = { new Integer(2), "issued", "blabla", new UserView("f3667", null)};
        return args;
    }

    protected Object[] getUnAuthorizeArguments2()
    {
        Object[] args = { new Integer(1), "payed", "blabla", new UserView("f3667", null)};
        return args;
    }

    public void testEditReimbursementGuide()
    {
        Object serviceArguments[] = getAuthorizeArguments();

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArguments);
            compareDataSetUsingExceptedDataSetTablesAndColumns(getSuccessfullExpectedDataSetFilePath());
            System.out.println(
                "testEditReimbursementGuide was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (FenixServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (Exception e)
        {
            System.out.println(
                "testEditReimbursementGuide was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testEditReimbursementGuide");
        }
    }

    public void testEditReimbursementGuideWithUnauthorizedState1()
    {
        Object serviceArguments[] = getUnAuthorizeArguments1();

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println(
                "testEditReimbursementGuideWithUnauthorizedState1 was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testEditReimbursementGuideWithUnauthorizedState1");

        } catch (InvalidGuideSituationServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (FenixServiceException e)
        {
            System.out.println(
                "testEditReimbursementGuideWithUnauthorizedState1 was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testEditReimbursementGuideWithUnauthorizedState1");
        }

    }

    public void testEditReimbursementGuideWithUnauthorizedState2()
    {
        Object serviceArguments[] = getUnAuthorizeArguments2();

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println(
                "testEditReimbursementGuideWithUnauthorizedState2 was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testEditReimbursementGuideWithUnauthorizedState2");

        } catch (InvalidGuideSituationServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (FenixServiceException e)
        {
            System.out.println(
                "testEditReimbursementGuideWithUnauthorizedState1 was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testEditReimbursementGuideWithUnauthorizedState2");
        }

    }

}
