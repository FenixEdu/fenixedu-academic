/*
 * Created on 23/Out/2003
 *  
 */
package ServidorAplicacao.Servicos.publico;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteExamMap;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.Exam;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class ExamSiteComponentServiceTest extends ServiceTestCase
{

    /**
     * @param testName
     */
    public ExamSiteComponentServiceTest(String nome)
    {
        super(nome);
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ExamSiteComponentService";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/publico/testExamSiteComponentServiceDataSet.xml";
    }

    protected Object[] getArguments()
    {

        InfoSiteExamMap bodyComponent = new InfoSiteExamMap();
        String executionYearName = "2003/2004";
        String executionPeriodName = "1 Semestre";
        String degreeInitials = "LEIC";
        String nameDegreeCurricularPlan = "LEIC2003/2004";
        List curricularYears = new ArrayList();
        curricularYears.add(new Integer(1));
        curricularYears.add(new Integer(2));

        Object[] args =
            {
                bodyComponent,
                executionYearName,
                executionPeriodName,
                degreeInitials,
                nameDegreeCurricularPlan,
                curricularYears };

        return args;
    }

    public void testExamSiteComponentService()
    {
        SiteView result = null;
        Object[] args = getArguments();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            IPersistentExecutionCourse disciplinaExecucaoPersistente =
                sp.getIPersistentExecutionCourse();
            IPersistentExam examePersistente = sp.getIPersistentExam();
            sp.iniciarTransaccao();

            IExecutionYear executionYear =
                persistentExecutionYear.readExecutionYearByName((String) args[1]);

            IExecutionPeriod executionPeriod =
                persistentExecutionPeriod.readByNameAndExecutionYear((String) args[2], executionYear);

            ICursoExecucao executionDegree =
                executionDegreeDAO.readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                    (String) args[3],
                    (String) args[4],
                    executionYear);

            List executionCourses1ano =
                disciplinaExecucaoPersistente.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                    new Integer(1),
                    executionPeriod,
                    executionDegree);

            List executionCourses2ano =
                disciplinaExecucaoPersistente.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
                    new Integer(2),
                    executionPeriod,
                    executionDegree);

            IExam exam1 =
                new Exam(
                    new GregorianCalendar(2004, 1, 27),
                    new GregorianCalendar(2004, 1, 27, 10, 0),
                    new GregorianCalendar(2004, 1, 27, 11, 30),
                    new Season(1));
            IExam exam2 =
                new Exam(
                    new GregorianCalendar(2004, 2, 17),
                    new GregorianCalendar(2004, 1, 27, 10, 0),
                    new GregorianCalendar(2004, 1, 27, 11, 30),
                    new Season(1));

            examePersistente.simpleLockWrite(exam1);
            examePersistente.simpleLockWrite(exam2);

            sp.confirmarTransaccao();

            sp.iniciarTransaccao();

            IExecutionCourse executionCourse2 = (ExecutionCourse) executionCourses1ano.get(0);
            disciplinaExecucaoPersistente.simpleLockWrite(executionCourse2);

            List list2 = new ArrayList();
            list2.clear();
            list2.add(exam1);
            list2.add(exam2);
            executionCourse2.setAssociatedExams(list2);

            IExecutionCourse executionCourse1 = (ExecutionCourse) executionCourses1ano.get(1);

            disciplinaExecucaoPersistente.simpleLockWrite(executionCourse1);

            List list1 = new ArrayList();
            list1.clear();
            list1.add(exam1);
            executionCourse1.setAssociatedExams(list1);

            IExecutionCourse executionCourse3 = (ExecutionCourse) executionCourses1ano.get(2);

            disciplinaExecucaoPersistente.simpleLockWrite(executionCourse3);

            List list3 = new ArrayList();
            executionCourse3.setAssociatedExams(list3);
            IExecutionCourse executionCourse4 = null;

            for (int i = 3; i < executionCourses1ano.size(); i++)
            {
                executionCourse4 = (ExecutionCourse) executionCourses1ano.get(i);

                disciplinaExecucaoPersistente.simpleLockWrite(executionCourse4);

                List list4 = new ArrayList();
                list4.add(exam1);
                executionCourse4.setAssociatedExams(list4);
            }

            for (int i = 0; i < executionCourses2ano.size(); i++)
            {
                IExecutionCourse executionCourse5 = (ExecutionCourse) executionCourses2ano.get(i);

                disciplinaExecucaoPersistente.simpleLockWrite(executionCourse5);

                List list5 = new ArrayList();
                list5.add(exam2);
                executionCourse5.setAssociatedExams(list5);
            }

            sp.confirmarTransaccao();

            executionCourses1ano.addAll(executionCourses2ano);

            result = (SiteView) ServiceManagerServiceFactory.executeService(null, getNameOfServiceToBeTested(), getArguments());

            InfoSiteExamMap siteExamMap = (InfoSiteExamMap) result.getComponent();

            InfoExamsMap examsMap = siteExamMap.getInfoExamsMap();

            List executionCourses = examsMap.getExecutionCourses();
            int size = executionCourses1ano.size();

            int n_exams = 0;
            for (int i = 0; i < size; i++)
            {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) executionCourses.get(i);

                List examsService = infoExecutionCourse.getAssociatedInfoExams();

                List exams = ((IExecutionCourse) executionCourses1ano.get(i)).getAssociatedExams();

                assertEquals(exams.size(), examsService.size());
                n_exams = exams.size();
                for (int j = 0; j < n_exams; j++)
                {
                    InfoExam infoExam = (InfoExam) examsService.get(j);
                    IExam oldExam = (IExam) exams.get(j);
                    InfoExam newInfoExam = Cloner.copyIExam2InfoExam(oldExam);
                    assertEquals(infoExam, newInfoExam);
                }
            }

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/publico/testExamSiteComponentServiceExpectedDataSet.xml");

            System.out.println(
                "testExamSiteComponentService was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (FenixServiceException ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testExamSiteComponentService was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testExamSiteComponentService");
        } catch (OutOfMemoryError ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testExamSiteComponentService was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testExamSiteComponentService");
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testExamSiteComponentService was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testExamSiteComponentService");
        }

    }

}
