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
import ServidorAplicacao.Servico.commons.ReadExecutionDegreeByOID;
import ServidorAplicacao.Servico.manager.ReadExecutionPeriod;
import ServidorAplicacao.Servico.sop.exams.ReadFilteredExamsMap;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadFilteredExamsMapTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadFilteredExamsMapTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadFilteredExamsMap";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadFilteredExamsMapDataset.xml";
    }

    private Calendar createStartSeason1() {
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

    private Calendar createEndSeason2() {
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

    private InfoDegree createInfoDegree() {
        InfoDegree infoDegree = new InfoDegree("LEIC", "Engenharia Informática e de Computadores");
        infoDegree.setTipoCurso(new TipoCurso(new Integer(1)));

        return infoDegree;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(InfoDegree infoDegree) {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(
                "LEIC - Currículo Antigo", infoDegree);
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

    public void testSuccessfullReadFilteredExamsMap() {

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(4));

        ReadFilteredExamsMap service = new ReadFilteredExamsMap();

        ISuportePersistente sp;
        InfoExamsMap infoExamsMap = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            /////
            InfoExecutionDegree infoExecutionDegree = (new ReadExecutionDegreeByOID()).run(new Integer(
                    10));
            InfoExecutionPeriod infoExecutionPeriod = (new ReadExecutionPeriod()).run(new Integer(1));
            /////

            infoExamsMap = service.run(infoExecutionDegree, curricularYears, infoExecutionPeriod);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            List curricularYearsList = new ArrayList();
            curricularYearsList.add(new Integer(2));

            Calendar startSeason1 = createStartSeason1();
            Calendar endSeason2 = createEndSeason2();

            assertEquals("Start of exam season1 was unexpected!", startSeason1, infoExamsMap
                    .getStartSeason1());
            assertEquals("End of exam season1 was unexpected!", null, infoExamsMap.getEndSeason1());
            assertEquals("Start of exam season2 was unexpected!", null, infoExamsMap.getStartSeason2());
            assertEquals("End of exam season2 was unexpected!", endSeason2, infoExamsMap.getEndSeason2());
            assertNotNull("Curricular years list was null!", infoExamsMap.getCurricularYears());
            assertTrue("Curricular year 4 not in list!", infoExamsMap.getCurricularYears().contains(
                    new Integer(4)));
            assertNotNull("Execution course list was null!", infoExamsMap.getExecutionCourses());
            assertEquals("Unexpected number of execution courses!", 1, infoExamsMap
                    .getExecutionCourses().size());

        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullReadFilteredExamsMap - Exception " + ex);
        }

    }

    public void testUnexistentExecutionPeriod() {
        InfoDegree infoDegree = createInfoDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(infoDegree);

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2005/2006");

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan,
                infoExecutionYear);

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(3));

        ReadFilteredExamsMap service = new ReadFilteredExamsMap();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(infoExecutionDegree, curricularYears, null);
            fail("testUnexistentExecutionPeriod");
            sp.cancelarTransaccao();
        } catch (Exception ex) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }

    }

    public void testUnexistentExecutionDegree() {
        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2005/2006");

        //Criar o infoExecutionPeriod
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("1º Semestre",
                infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));
        infoExecutionPeriod.setIdInternal(new Integer(0));

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(null, null);

        //Criar os curricularYears
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(3));

        ReadFilteredExamsMap service = new ReadFilteredExamsMap();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(infoExecutionDegree, curricularYears, infoExecutionPeriod);
            fail("testUnexistentExecutionDegree");
            sp.cancelarTransaccao();
        } catch (Exception ex) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }

    }

    public void testUnexistentCurricularYears() {

        InfoDegree infoDegree = createInfoDegree();

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(infoDegree);

        //Criar o infoExecutionYear
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2003/2004");

        //Criar o infoExecutionPeriod
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("1º Semestre",
                infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));
        infoExecutionPeriod.setIdInternal(new Integer(1));

        //Criar o infoExecutionDegree
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan,
                infoExecutionYear);

        ReadFilteredExamsMap service = new ReadFilteredExamsMap();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(infoExecutionDegree, null, infoExecutionPeriod);
            fail("testUnexistentCurricularYears");
            sp.cancelarTransaccao();
        } catch (Exception ex) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }

    }
}