package ServidorAplicacao.Servicos.coordinator;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Tânia Pousão Create on 7/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegreeTest extends ServiceTestCase
{
    public ReadDegreeInfoByExecutionDegreeTest(String testName)
    {
        super(testName);
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadDegreeInfoByExecutionDegree";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/coordinator/testDataSetDegreeSite.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC", "pass", getApplication()};
        return args;
    }

    protected String[] getSecondAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC2", "pass", getApplication()};
        return args;
    }
    protected String[] getThirdAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC3", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndAlreadyAuthorizedUser()
    {
        String[] args = { "userT", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "userE", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    public void testSuccessfullWithOnlyOneDegreeInfo()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(10);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            //Service
            InfoDegreeInfo infoDegreeInfo = null;
            try
            {
                infoDegreeInfo =
                    (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //Returned anything?
            if (infoDegreeInfo == null)
            {
                fail("Reading a Degree Info.");
            }

            //Verify if read the correct data was read
            assertEquals(new String("Obj"), infoDegreeInfo.getObjectives());
            assertEquals(new String("His"), infoDegreeInfo.getHistory());
            assertEquals(new String("Prof"), infoDegreeInfo.getProfessionalExits());
            assertEquals(new String("addInfo"), infoDegreeInfo.getAdditionalInfo());
            assertEquals(new String("Links"), infoDegreeInfo.getLinks());
            assertEquals(new String("Test"), infoDegreeInfo.getTestIngression());
            assertEquals(new Integer(50), infoDegreeInfo.getDriftsInitial());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsFirst());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsSecond());
            assertEquals(new String("Class"), infoDegreeInfo.getClassifications());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMin());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMax());
            assertEquals(new Double(12.0), infoDegreeInfo.getMarkAverage());
            if(infoDegreeInfo.getInfoDegree() != null) {
                assertEquals(new Integer(10), infoDegreeInfo.getInfoDegree().getIdInternal());
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfullWithOnlyOneDegreeInfo");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testSuccessfullWithMoreThanOneDegreeInfo()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(100);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            //Service
            InfoDegreeInfo infoDegreeInfo = null;
            try
            {
                infoDegreeInfo =
                    (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //Returned anything?
            if (infoDegreeInfo == null)
            {
                fail("Reading a Degree Info.");
            }

            //Verify if read the correct data was read, the most recently
            assertEquals(new String("Obj2"), infoDegreeInfo.getObjectives());
            assertEquals(new String("His2"), infoDegreeInfo.getHistory());
            assertEquals(new String("Prof2"), infoDegreeInfo.getProfessionalExits());
            assertEquals(new String("addInfo2"), infoDegreeInfo.getAdditionalInfo());
            assertEquals(new String("Links2"), infoDegreeInfo.getLinks());
            assertEquals(new String("Test2"), infoDegreeInfo.getTestIngression());
            assertEquals(new Integer(60), infoDegreeInfo.getDriftsInitial());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsFirst());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsSecond());
            assertEquals(new String("Class2"), infoDegreeInfo.getClassifications());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMin());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMax());
            assertEquals(new Double(10.0), infoDegreeInfo.getMarkAverage());
            if(infoDegreeInfo.getInfoDegree() != null) {
                assertEquals(new Integer(100), infoDegreeInfo.getInfoDegree().getIdInternal());
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfullWithMoreThanOneDegreeInfo");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testSuccessfullWithDegreeInfoLastYear()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(2003);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            //Service
            InfoDegreeInfo infoDegreeInfo = null;
            try
            {
                infoDegreeInfo =
                    (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //In spite of this degree info doen't belong to the execution period used
            //it's returned
            assertNotNull(infoDegreeInfo);
            assertEquals(new String("Obj2002"), infoDegreeInfo.getObjectives());
            assertEquals(new String("His2002"), infoDegreeInfo.getHistory());
            assertEquals(new String("Prof2002"), infoDegreeInfo.getProfessionalExits());
            assertEquals(new String("addInfo2002"), infoDegreeInfo.getAdditionalInfo());
            assertEquals(new String("Links2002"), infoDegreeInfo.getLinks());
            assertEquals(new String("Test2002"), infoDegreeInfo.getTestIngression());
            assertEquals(new Integer(60), infoDegreeInfo.getDriftsInitial());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsFirst());
            assertEquals(new Integer(0), infoDegreeInfo.getDriftsSecond());
            assertEquals(new String("Class2002"), infoDegreeInfo.getClassifications());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMin());
            assertEquals(new Double(0.0), infoDegreeInfo.getMarkMax());
            assertEquals(new Double(10.0), infoDegreeInfo.getMarkAverage());
            if(infoDegreeInfo.getInfoDegree() != null) {
                assertEquals(new Integer(2002), infoDegreeInfo.getInfoDegree().getIdInternal());
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSuccessfullWithDegreeInfoLastYear");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNULLArg()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = null;

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            try
            {
                ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                //this returned a NotAuthorizedException
                e.printStackTrace();
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNULLArg");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNoDegreeCurricularPlan()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(30);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            try
            {
                ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 23);
                assertEquals(new String("invalidExecutionDegree"), msg);
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoDegreeCurricularPlan");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNoDegree()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(20);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            try
            {
                ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                //				this returned a NotAuthorizedException
                e.printStackTrace();
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
				assertEquals(new String("impossibleDegreeInfo"), msg);
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoDegree");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNoExecutionDegree()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(1);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getSecondAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            try
            {
                ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                //				this returned a NotAuthorizedException
                e.printStackTrace();
                /*String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 23);
				assertEquals(new String("invalidExecutionDegree"), msg);*/                
            }

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoExecutionDegree");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }
    public void testNoDegreeInfo()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(1000);

            Object[] args = { infoExecutionDegreeCode };

            //Valid user
            String[] argsUser = getThirdAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            //Service
            InfoDegreeInfo infoDegreeInfo = null;
            try
            {
                infoDegreeInfo =
                    (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //this degree info isn't a null object
            //but a "empty" object
            assertNotNull(infoDegreeInfo);
            assertNull(infoDegreeInfo.getDescription());

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testNoDegreeInfo");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    /*public void testUserUnsuccessfull()
    {
        try
        {
            //Valid user
            String[] argsUser = getAuthenticatedAndUnauthorizedUser();
			try
			{
            ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            fail("Reading a degree site with invalid user");

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testUserUnsuccessfull");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testSecondUserUnsuccessfull()
    {
        try
        {
            //Valid user
            String[] argsUser = getNotAuthenticatedUser();
            ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

            fail("Reading a degree site with invalid user");

            System.out.println(
                "ReadDegreeInfoByExecutionDegreeTest was SUCCESSFULY in test: testSecondUserUnsuccessfull");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }*/

}
