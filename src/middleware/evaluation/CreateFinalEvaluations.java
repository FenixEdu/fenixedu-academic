/*
 * Created on Apr 23, 2004
 */
package middleware.evaluation;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.EvaluationExecutionCourse;
import Dominio.FinalEvaluation;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IFinalEvaluation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class CreateFinalEvaluations
{

    public static void main(String[] args)
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IPersistentEvaluationExecutionCourse persistentEvaluationExecutionCourse = persistentSuport
                    .getIPersistentEvaluationExecutionCourse();
            IPersistentEvaluation persistentEvaluation = persistentSuport.getIPersistentEvaluation();
            persistentSuport.iniciarTransaccao();
            long startTime = Calendar.getInstance().getTimeInMillis();
            List executionCourses = persistentExecutionCourse.readAll();
            long endTime = Calendar.getInstance().getTimeInMillis();
            long duration = endTime - startTime;
            System.out.println("Time in milis->" + duration);
            persistentSuport.confirmarTransaccao();
            Iterator iter = executionCourses.iterator();
            while (iter.hasNext())
            {
                persistentSuport.iniciarTransaccao();
                IExecutionCourse executionCourse = (IExecutionCourse) iter.next();
                List evaluations = executionCourse.getAssociatedEvaluations();
                boolean hasFinalEvaluation = CollectionUtils.exists(evaluations, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        IEvaluation evaluation = (IEvaluation) arg0;
                        return evaluation instanceof FinalEvaluation;
                    }
                });

                if (!hasFinalEvaluation)
                {
                    System.out.println(executionCourse.getIdInternal() + "-" + executionCourse.getNome()
                            + "-" + executionCourse.getExecutionPeriod().getIdInternal());
                    IFinalEvaluation finalEvaluation = new FinalEvaluation();
                    IEvalutionExecutionCourse evalutionExecutionCourse = new EvaluationExecutionCourse();
                    persistentEvaluation.simpleLockWrite(finalEvaluation);
                    persistentEvaluationExecutionCourse.simpleLockWrite(evalutionExecutionCourse);
                    evalutionExecutionCourse.setEvaluation(finalEvaluation);
                    evalutionExecutionCourse.setExecutionCourse(executionCourse);
                }
                persistentSuport.confirmarTransaccao();
            }

            endMethod();

        }
        catch (ExcepcaoPersistencia e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private static void endMethod()
    {
        System.out.println("The end");
        System.exit(0);
    }
}