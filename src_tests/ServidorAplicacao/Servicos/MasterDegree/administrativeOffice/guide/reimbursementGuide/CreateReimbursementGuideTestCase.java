/*
 * Created on 18/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide.reimbursementGuide;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueSumServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *
 */
public class CreateReimbursementGuideTestCase extends ServiceNeedsAuthenticationTestCase
{

    protected void setUp()
    {
        super.setUp();
    }

    /**
     * @param name
     */
    public CreateReimbursementGuideTestCase(String name)
    {
        super(name);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested()
    {
        return "CreateReimbursementGuide";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testCreateReimbursementGuideDataSet.xml";
    }
    protected String getSuccessfullExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testSucessfullCreateReimbursementGuideDataSet.xml";
    }

    protected String getUnSuccessfullExpectedDataSetFilePath1()
    {
        return "etc/datasets/servicos/MasterDegree/administrativeOffice/guide/reimbursementGuide/testCreateReimbursementGuideDataSet.xml";
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
        Object[] args = { new Integer(1), "whatever", new Double(10.21), new UserView("f3667", null)};
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
        //Integer guideId, String justification, Double value, IUserView userView
        Object[] args = { new Integer(1), "whatever", new Double(100.21), new UserView("f3667", null)};
        return args;
    }

    protected Object[] getUnAuthorizeArguments2()
    {
        //Integer guideId, String justification, Double value, IUserView userView
        Object[] args = { new Integer(1), "whatever", new Double(30.21), new UserView("f3667", null)};
        return args;
    }

    protected Object[] getUnAuthorizeArguments3()
    {
        //Integer guideId, String justification, Double value, IUserView userView
        Object[] args = { new Integer(3), "whatever", new Double(10.21), new UserView("f3667", null)};
        return args;
    }

    public void testCreateReimbursementGuide()
    {
        Object serviceArguments[] = getAuthorizeArguments();

        try
        {
            gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
            compareDataSetUsingExceptedDataSetTablesAndColumns(getSuccessfullExpectedDataSetFilePath());
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (FenixServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (Exception e)
        {
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");
        }
    }

    public void testCreateReimbursementGuideWithUnauthorizedValue1()
    {
        Object serviceArguments[] = getUnAuthorizeArguments1();

        try
        {
            gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");

        } catch (InvalidReimbursementValueServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (FenixServiceException e)
        {
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");
        }

    }

    public void testCreateReimbursementGuideWithUnauthorizedValue2()
    {
        Object serviceArguments[] = getUnAuthorizeArguments2();

        try
        {
            gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");

        } catch (InvalidReimbursementValueSumServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (FenixServiceException e)
        {
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");
        }

    }

    public void testCreateReimbursementGuideWithUnauthorizedGuideState()
    {
        Object serviceArguments[] = getUnAuthorizeArguments3();

        try
        {
            gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");

        } catch (InvalidGuideSituationServiceException e)
        {
            // expected exception
            compareDataSetUsingExceptedDataSetTablesAndColumns(
                getUnSuccessfullExpectedDataSetFilePath1());
        } catch (FenixServiceException e)
        {
            System.out.println(
                "testCreateReimbursementGuideWithUnauthorizedValue was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(
                getNameOfServiceToBeTested() + "fail testCreateReimbursementGuideWithUnauthorizedValue");
        }

    }
}
