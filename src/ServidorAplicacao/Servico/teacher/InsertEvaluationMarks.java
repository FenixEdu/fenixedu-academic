package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.ISiteComponent;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Mark;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *  
 */
public class InsertEvaluationMarks implements IServico
{
    private static InsertEvaluationMarks _servico = new InsertEvaluationMarks();

    /**
	 * The actor of this class.
	 */
    private InsertEvaluationMarks()
    {

    }

    /**
	 * Returns Service Name
	 */
    public String getNome()
    {
        return "InsertEvaluationMarks";
    }

    /**
	 * Returns the _servico.
	 * 
	 * @return ReadExecutionCourse
	 */
    public static InsertEvaluationMarks getService()
    {
        return _servico;
    }

    public Object run(Integer executionCourseCode, Integer evaluationCode, HashMap hashMarks)
        throws ExcepcaoInexistente, FenixServiceException
    {

        ISite site = null;
        IExecutionCourse executionCourse = null;
        IEvaluation evaluation = null;
        List marksErrorsInvalidMark = null;
        List attendList = null;
		HashMap newHashMarks = new HashMap();
        try
        {
            
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentMark persistentMark = sp.getIPersistentMark();

            //Execution Course
            executionCourse = new ExecutionCourse(executionCourseCode);
            executionCourse = (IExecutionCourse)executionCourseDAO.readByOId(executionCourse, false);

            //Site
            site = persistentSite.readByExecutionCourse(executionCourse);

            //Evaluation
            evaluation = new Evaluation();
            evaluation.setIdInternal(evaluationCode);
            evaluation = (IEvaluation)persistentEvaluation.readByOId(evaluation, false);

            //Attend List
            attendList = persistentAttend.readByExecutionCourse(executionCourse);

            marksErrorsInvalidMark = new ArrayList();
            ListIterator iterAttends = attendList.listIterator();

            while (iterAttends.hasNext())
            {
                IFrequenta attend = (IFrequenta)iterAttends.next();

                String mark = (String)hashMarks.get(attend.getAluno().getNumber().toString());
				hashMarks.remove(attend.getAluno().getNumber().toString());
				
                if (mark != null && mark.length() > 0)
                { 
                    if (!isValidMark(evaluation, mark, attend.getAluno()))
                    {
                        InfoMark infoMark = new InfoMark();
                        infoMark.setMark(mark);
                        infoMark.setInfoFrequenta(Cloner.copyIFrequenta2InfoFrequenta(attend));
                        marksErrorsInvalidMark.add(infoMark);
                    }
                    else
                    {
						newHashMarks.put(attend.getAluno().getNumber().toString(), mark);
                        IMark domainMark = persistentMark.readBy(evaluation, attend);
                        //verify if the student has already a mark
                        if (domainMark == null)
                        {
                            domainMark = new Mark();

                            persistentMark.simpleLockWrite(domainMark);

                            domainMark.setAttend(attend);
                            domainMark.setEvaluation(evaluation);
                            domainMark.setMark(mark.toUpperCase());

                        }
                        else
                        {	
                            if(!domainMark.getMark().equals(mark))
                            {    
                                persistentMark.simpleLockWrite(domainMark);
                                domainMark.setMark(mark.toUpperCase());
                            }
                        }

                    }
                }
                else
                {
                    IMark domainMark = persistentMark.readBy(evaluation, attend);
                    if (domainMark != null)
                    {
                        persistentMark.delete(domainMark);
                    }
                }
            }

 

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return createSiteView(site, evaluation, newHashMarks, marksErrorsInvalidMark, attendList, hashMarks);
    }

    /**
	 * @param infoMark
	 * @return
	 */



    private Object createSiteView(
        ISite site,
        IEvaluation evaluation,
        HashMap hashMarks,
        List marksErrorsInvalidMark,
        List attendList,
		HashMap nonExistingStudents)
        throws FenixServiceException
    {
        InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
        infoSiteMarks.setInfoEvaluation(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
        infoSiteMarks.setHashMarks(hashMarks);
        infoSiteMarks.setMarksListErrors(marksErrorsInvalidMark);
        infoSiteMarks.setInfoAttends(attendList);
        
        List studentsListErrors = new ArrayList();
        Iterator iter = nonExistingStudents.keySet().iterator();
        while(iter.hasNext())
        {
            studentsListErrors.add(iter.next());
        }
        infoSiteMarks.setStudentsListErrors(studentsListErrors);
        

        TeacherAdministrationSiteComponentBuilder componentBuilder =
            new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent =
            componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

        System.out.println("ultima instrução do serviço");
        return siteView;
    }

    private boolean isValidMark(IEvaluation evaluation, String mark, IStudent student)
    {
        IStudentCurricularPlan studentCurricularPlan = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente curricularPlanPersistente =
                sp.getIStudentCurricularPlanPersistente();

            studentCurricularPlan =
                curricularPlanPersistente.readActiveStudentCurricularPlan(
                    student.getNumber(),
                    student.getDegreeType());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
            DegreeCurricularPlanStrategyFactory.getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
            degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);

        if (mark == null || mark.length() == 0)
        {
            return true;
        }
        else
        {
            return degreeCurricularPlanStrategy.checkMark(mark, Cloner.copyIEvaluation2InfoEvaluation(evaluation).getEvaluationType());
        }
    }
}
