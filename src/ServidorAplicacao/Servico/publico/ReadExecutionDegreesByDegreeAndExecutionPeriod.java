package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ExecutionPeriod;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 13/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriod implements IServico
{
    private static ReadExecutionDegreesByDegreeAndExecutionPeriod service =
        new ReadExecutionDegreesByDegreeAndExecutionPeriod();

    public ReadExecutionDegreesByDegreeAndExecutionPeriod()
    {
    }

    public String getNome()
    {
        return "ReadExecutionDegreesByDegreeAndExecutionPeriod";
    }

    public static ReadExecutionDegreesByDegreeAndExecutionPeriod getService()
    {
        return service;
    }

    public List run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException
    {
        List infoExecutionDegreeList = null;

        try
        {
            if (degreeId == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            //Execution Period
            IExecutionPeriod executionPeriod = new ExecutionPeriod();
            if (executionPeriodId == null)
            {
                executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            } else
            {
                executionPeriod.setIdInternal(executionPeriodId);

                executionPeriod =
                    (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);
            }

            if (executionPeriod == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }
            System.out.println(executionYear);

            //Degree
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            ICurso degree = new Curso();
            degree.setIdInternal(degreeId);

            degree = (ICurso) persistentDegree.readByOId(degree, false);
            if (degree == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Execution degrees
            ICursoExecucaoPersistente persistentExecutionDegre = sp.getICursoExecucaoPersistente();
            List executionDegreeList =
                persistentExecutionDegre.readByDegreeAndExecutionYear(degree, executionYear);
            if (executionDegreeList == null || executionDegreeList.size() <= 0)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            infoExecutionDegreeList = new ArrayList();
            ListIterator listIterator = executionDegreeList.listIterator();
            while (listIterator.hasNext())
            {
                ICursoExecucao executionDegree = (ICursoExecucao) listIterator.next();

                InfoExecutionDegree infoExecutionDegree =
                    Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);

                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoExecutionDegreeList;
    }
}
