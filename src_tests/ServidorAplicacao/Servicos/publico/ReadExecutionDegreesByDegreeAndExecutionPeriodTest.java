package ServidorAplicacao.Servicos.publico;

import java.util.List;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriodTest extends ServiceTestCase
{
    public ReadExecutionDegreesByDegreeAndExecutionPeriodTest(String name)
    {
        super(name);
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/coordinator/testDataSetDegreeSite.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadExecutionDegreesByDegreeAndExecutionPeriod";
    }

    public void testSuccessfull()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = new Integer(10);
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            List infoExecutionDegrees = null;
            try
            {
                infoExecutionDegrees = (List) gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            assertEquals(new Integer(1), new Integer(infoExecutionDegrees.size()));

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(0);

            assertEquals(new Integer(10), infoExecutionDegree.getIdInternal());
            assertEquals(new String("Alameda"), infoExecutionDegree.getInfoCampus().getName());
            assertEquals(new Integer(111), infoExecutionDegree.getInfoCoordinator().getTeacherNumber());
            assertEquals(
                new Integer(10),
                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testSuccessfull");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testSuccessfullWithMoreThanOneExecutionDegree()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = new Integer(1000);
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            List infoExecutionDegrees = null;
            try
            {
                infoExecutionDegrees = (List) gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            assertEquals(new Integer(2), new Integer(infoExecutionDegrees.size()));

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(0);

            assertEquals(new Integer(1000), infoExecutionDegree.getIdInternal());
            assertEquals(new String("Alameda"), infoExecutionDegree.getInfoCampus().getName());
            assertEquals(
                new Integer(100100100),
                infoExecutionDegree.getInfoCoordinator().getTeacherNumber());
            assertEquals(
                new Integer(1000),
                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());

            infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(1);

            assertEquals(new Integer(1001), infoExecutionDegree.getIdInternal());
            assertEquals(new String("TagusPark"), infoExecutionDegree.getInfoCampus().getName());
            assertEquals(
                new Integer(101101101),
                infoExecutionDegree.getInfoCoordinator().getTeacherNumber());
            assertEquals(
                new Integer(1000),
                infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testSuccessfullWithMoreThanOneExecutionDegree");

        }
        catch (Exception e)
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
            Integer infoDegreeCode = new Integer(2002);
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testSuccessfullWithDegreeInfoLastYear");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNULLArg1()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = new Integer(10);
            Integer infoExecutionPeriodCode = null;

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNULLArg1");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNULLArg2()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = null;
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNULLArg2");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNoExecutionPeriod()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = new Integer(10);
            Integer infoExecutionPeriodCode = new Integer(1000);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNoExecutionPeriod");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }

    public void testNoExecutionYear()
    {
        try
        {
            //Service Argument
            Integer infoDegreeCode = new Integer(10);
            Integer infoExecutionPeriodCode = new Integer(5);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNoExecutionYear");

        }
        catch (Exception e)
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
            Integer infoDegreeCode = new Integer(99);
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNoDegree");

        }
        catch (Exception e)
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
            Integer infoDegreeCode = new Integer(9999);
            Integer infoExecutionPeriodCode = new Integer(3);

            Object[] args = { infoExecutionPeriodCode, infoDegreeCode };

            //Service
            try
            {
                gestor.executar(null, getNameOfServiceToBeTested(), args);
            }
            catch (FenixServiceException e)
            {
                String msg =
                    e.getMessage().substring(
                        e.getMessage().lastIndexOf(".") + 1,
                        e.getMessage().lastIndexOf(".") + 21);
                if (!msg.equals(new String("impossibleDegreeSite")))
                {
                    e.printStackTrace();
                    fail("Reading a degree information");
                }
            }

            System.out.println(
                "ReadExecutionDegreesByDegreeAndExecutionPeriodTest was SUCCESSFULY in test: testNoDegree");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }
}
