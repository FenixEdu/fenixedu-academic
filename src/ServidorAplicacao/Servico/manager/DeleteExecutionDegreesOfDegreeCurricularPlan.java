/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlan implements IServico
{

    private static DeleteExecutionDegreesOfDegreeCurricularPlan service =
        new DeleteExecutionDegreesOfDegreeCurricularPlan();

    public static DeleteExecutionDegreesOfDegreeCurricularPlan getService()
    {
        return service;
    }

    private DeleteExecutionDegreesOfDegreeCurricularPlan()
    {
    }

    public final String getNome()
    {
        return "DeleteExecutionDegreesOfDegreeCurricularPlan";
    }

    // delete a set of executionDegrees
    public List run(List executionDegreesIds) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            ITurmaPersistente persistentClass = sp.getITurmaPersistente();

            Iterator iter = executionDegreesIds.iterator();

            List undeletedExecutionDegreesYears = new ArrayList();
            List classes;
            Integer executionDegreeId;
            ICursoExecucao executionDegree;

            while (iter.hasNext())
            {

                executionDegreeId = (Integer) iter.next();
                CursoExecucao execDegree = new CursoExecucao();
                execDegree.setIdInternal(executionDegreeId);
                executionDegree =
                    (ICursoExecucao) persistentExecutionDegree.readByOId(execDegree, false);
                if (executionDegree != null)
                {
                    classes = persistentClass.readByExecutionDegree(executionDegree);
                    if (classes.isEmpty())
                        persistentExecutionDegree.delete(executionDegree);
                    else
                        undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }

            return undeletedExecutionDegreesYears;

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

}