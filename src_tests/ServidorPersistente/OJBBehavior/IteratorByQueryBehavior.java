/*
 * Created on 11/Fev/2004
 */
package ServidorPersistente.OJBBehavior;

import java.util.Iterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class IteratorByQueryBehavior
{

    /**
     * 
     */
    public IteratorByQueryBehavior()
    {
    }

    public static void main(String[] args)
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            System.out.println("Test 1");
            System.out.println("Query with null result");
            persistentSuport.iniciarTransaccao();
            Criteria crit1 = new Criteria();
            crit1.addEqualTo("sigla", "12345678");
            Iterator iter1 = persistentExecutionCourse.readIteratorByCriteria(
                    ExecutionCourse.class, crit1);
            if (iter1 == null)
            {
                System.out.println("the iterator of a null result set is null");
            }
            else
            {
                System.out
                        .println("the iterator of a null result set is not null");
                System.out.println("Has more elements?->" + iter1.hasNext());
            }
            persistentSuport.confirmarTransaccao();

            System.out.println("Test 2");
            System.out
                    .println("not null result set- behavior between transactions");
            persistentSuport.iniciarTransaccao();
            Criteria crit2 = new Criteria();
          //  crit2.addEqualTo("sigla", "AMI%");
            Iterator iter2 = persistentExecutionCourse.readIteratorByCriteria(
                    ExecutionCourse.class, crit2);
            if (iter2 == null)
            {
                System.out.println("the iterator is null");
            }
            else
            {
                System.out.println("the iterator is not null");
                System.out.println("Has more elements?->" + iter2.hasNext());
            }
            persistentSuport.confirmarTransaccao();
            persistentSuport.iniciarTransaccao();
            if (iter2 == null)
            {
                System.out.println("the iterator in new transaction is null");
            }
            else
            {
                System.out
                        .println("the iterator is in new transaction not null");
                System.out.println("Has more elements?->" + iter2.hasNext());
                try
                {
                    IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                            .lockIteratorNextObj(iter2);
                    System.out.println("first element>" + executionCourse);
                }
                catch (RuntimeException e1)
                {
                    System.out.println("the elements are ruined");

                    e1.printStackTrace();
                }
            }
            persistentSuport.confirmarTransaccao();

        }
        catch (ExcepcaoPersistencia e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
