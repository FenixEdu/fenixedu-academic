/*
 * Created on Nov 4, 2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadFilteredExamsMapTest extends ServiceTestCase
{

    /**
     * @param name
     */
    public ReadFilteredExamsMapTest(java.lang.String testName)
    {
        super(testName);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
     */
    protected String[] getAuthorizedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNotSOPEmployeeUser()
     */
    protected String[] getNonUser()
    {
        String[] args = { "76879", "pass", getApplication()};
        return args;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested()
    {
        return "ReadFilteredExamsMap";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/sop/testReadFilteredExamsMapDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/sop/testExpectedReadFilteredExamsMapDataSet.xml";
    }

    protected IUserView authenticateUser(String[] arguments)
    {
        SuportePersistenteOJB.resetInstance();
        String args[] = arguments;

        try
        {
            return (IUserView) ServiceUtils.executeService(null, "Autenticacao", args);
        }
        catch (Exception ex)
        {
            fail("Authenticating User!" + ex);
            return null;

        }
    }

    protected IUserView authenticateNonUser(String[] arguments) throws Exception
    {
        SuportePersistenteOJB.resetInstance();
        String args[] = arguments;

        return (IUserView) ServiceUtils.executeService(null, "Autenticacao", args);
    }

    protected Calendar createStartSeason1()
    {
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2004);
        startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
        startSeason1.set(Calendar.DAY_OF_MONTH, 5);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);

        return startSeason1;
    }

    protected Calendar createEndSeason2()
    {
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2004);
        endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 14);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

        return endSeason2;
    }

    protected InfoDegree createInfoDegree()
    {
        InfoDegree infoDegree = new InfoDegree("LEIC", "Engenharia Informatica e de Computadores");
        infoDegree.setTipoCurso(new TipoCurso(new Integer(1)));

        return infoDegree;
    }

    protected InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(InfoDegree infoDegree)
    {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan =
            new InfoDegreeCurricularPlan("plano1", infoDegree);
        infoDegreeCurricularPlan.setDegreeDuration(new Integer(2));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(0));
        infoDegreeCurricularPlan.setState(new DegreeCurricularPlanState(new Integer(1)));

        InfoCurricularCourseScope infoScope = new InfoCurricularCourseScope();
        InfoCurricularSemester infoSemester = new InfoCurricularSemester();
        InfoCurricularYear infoYear = new InfoCurricularYear(new Integer(2));

        infoSemester.setInfoCurricularYear(infoYear);
        infoSemester.setSemester(new Integer(1));

        infoScope.setInfoCurricularSemester(infoSemester);

        List listInfoScope = new ArrayList();
        listInfoScope.add(infoScope);

        InfoCurricularCourse infoCC1 = new InfoCurricularCourse();
        infoCC1.setName("Trabalho Final de Curso I");
        infoCC1.setCode("TFCI");
        infoCC1.setInfoScopes(listInfoScope);

        InfoCurricularCourse infoCC2 = new InfoCurricularCourse();
        infoCC2.setName("Arquitectura de Computadores");
        infoCC2.setCode("AC");
        infoCC2.setInfoScopes(listInfoScope);

        infoCC2.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        InfoCurricularCourse infoCC3 = new InfoCurricularCourse();
        infoCC3.setName("Aprendizagem");
        infoCC3.setCode("APR");
        infoCC3.setInfoScopes(listInfoScope);

        List curricularCourses = new ArrayList();
        curricularCourses.add(infoCC1);
        curricularCourses.add(infoCC2);
        curricularCourses.add(infoCC3);

        infoDegreeCurricularPlan.setCurricularCourses(curricularCourses);

        return infoDegreeCurricularPlan;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getSuccessfullArguments()
    {

        InfoDegree infoDegree = createInfoDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(infoDegree);

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2003/2004");

        //Criar o infoExecutionPeriod
        InfoExecutionPeriod infoExecutionPeriod =
            new InfoExecutionPeriod("1º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));
        infoExecutionPeriod.setIdInternal(new Integer(1));

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree =
            new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(2));

        Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        return args;
    }

    protected Object[] getUnsuccessfullArgumentsWithUnexistentExecutionPeriod()
    {

        InfoDegree infoDegree = createInfoDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(infoDegree);

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2005/2006");

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree =
            new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(3));

        Object[] args = { infoExecutionDegree, curricularYears, null };
        return args;
    }

    protected Object[] getUnsuccessfullArgumentsWithUnexistentExecutionDegree()
    {

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2005/2006");

        //Criar o infoExecutionPeriod
        InfoExecutionPeriod infoExecutionPeriod =
            new InfoExecutionPeriod("1º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));
        infoExecutionPeriod.setIdInternal(new Integer(0));

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(null, null);

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(3));

        Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
        return args;
    }

    protected Object[] getUnsuccessfullArgumentsWithUnexistentCurricularYears()
    {

        InfoDegree infoDegree = createInfoDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(infoDegree);

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2003/2004");

        //Criar o infoExecutionPeriod
        InfoExecutionPeriod infoExecutionPeriod =
            new InfoExecutionPeriod("1º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));
        infoExecutionPeriod.setIdInternal(new Integer(1));

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree =
            new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);

        Object[] args = { infoExecutionDegree, null, infoExecutionPeriod };
        return args;
    }

    public void testSuccessfullReadFilteredExamsMap()
    {

        try
        {
            String[] args = getAuthorizedUser();
            IUserView userView = authenticateUser(args);

            InfoExamsMap infoExamsMap =
                (InfoExamsMap) ServiceUtils.executeService(
                    userView,
                    getNameOfServiceToBeTested(),
                    getSuccessfullArguments());

            compareDataSet(getExpectedDataSetFilePath());

            List curricularYears = new ArrayList();
            curricularYears.add(new Integer(2));

            Calendar startSeason1 = createStartSeason1();
            Calendar endSeason2 = createEndSeason2();

            assertEquals(
                "Start of exam season1 was unexpected!",
                startSeason1,
                infoExamsMap.getStartSeason1());
            assertEquals("End of exam season1 was unexpected!", null, infoExamsMap.getEndSeason1());
            assertEquals("Start of exam season2 was unexpected!", null, infoExamsMap.getStartSeason2());
            assertEquals(
                "End of exam season2 was unexpected!",
                endSeason2,
                infoExamsMap.getEndSeason2());
            assertNotNull("Curricular years list was null!", infoExamsMap.getCurricularYears());
            assertTrue(
                "Curricular year 2 not in list!",
                infoExamsMap.getCurricularYears().contains(new Integer(2)));
            assertNotNull("Execution course list was null!", infoExamsMap.getExecutionCourses());
            assertEquals(
                "Unexpected number of execution courses!",
                3,
                infoExamsMap.getExecutionCourses().size());
        }
        catch (FenixServiceException ex)
        {
            fail("testSuccessfullReadFilteredExamsMap" + ex);
        }
        catch (Exception ex)
        {
            fail("testSuccessfullReadFilteredExamsMap error on compareDataSet" + ex);
        }
    }

    public void testUnsuccessfullReadFilteredExamsMapWithNonUser()
    {

        try
        {
            String[] args = getNonUser();
            IUserView userView = authenticateNonUser(args);

            ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), getSuccessfullArguments());

            fail("testUnsuccessfullReadFilteredExamsMapWithNonUser");
        }
        catch (FenixServiceException ex)
        {

        }
        catch (Exception ex)
        {

        }
    }

    public void testUnexistentExecutionPeriod()
    {

        try
        {
            String[] args = getAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceUtils.executeService(
                userView,
                getNameOfServiceToBeTested(),
                getUnsuccessfullArgumentsWithUnexistentExecutionPeriod());

            fail("testUnexistentExecutionPeriod");
        }
        catch (FenixServiceException ex)
        {
            //correu bem
        }
    }

    public void testUnexistentExecutionDegree()
    {

        try
        {
            String[] args = getAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceUtils.executeService(
                userView,
                getNameOfServiceToBeTested(),
                getUnsuccessfullArgumentsWithUnexistentExecutionDegree());

            fail("testUnexistentExecutionDegree");
        }
        catch (FenixServiceException ex)
        {
            //correu bem
        }
    }

    public void testUnexistentCurricularYears()
    {

        try
        {
            String[] args = getAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceUtils.executeService(
                userView,
                getNameOfServiceToBeTested(),
                getUnsuccessfullArgumentsWithUnexistentCurricularYears());

            fail("testUnexistentCurricularYears");
        }
        catch (FenixServiceException ex)
        {
            //correu bem
        }
    }
}
