/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionDegree;
import Dominio.Campus;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICampus;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.places.campus.IPersistentCampus;

/**
 * @author lmac1
 */
public class EditExecutionDegree implements IServico
{

    private static EditExecutionDegree service = new EditExecutionDegree();

    public static EditExecutionDegree getService()
    {
        return service;
    }

    private EditExecutionDegree()
    {
    }

    public final String getNome()
    {
        return "EditExecutionDegree";
    }

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException
    {

        ICursoExecucaoPersistente persistentExecutionDegree = null;
        IExecutionYear executionYear = null;

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
            CursoExecucao execDegree = new CursoExecucao();
            execDegree.setIdInternal(infoExecutionDegree.getIdInternal());

            ICursoExecucao oldExecutionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(
                    execDegree, false);

            IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();

            ICampus campus = (ICampus) campusDAO.readByOId(new Campus(infoExecutionDegree
                    .getInfoCampus().getIdInternal()), false);
            if (campus == null) { throw new NonExistingServiceException("message.nonExistingCampus",
                    null); }

            if (oldExecutionDegree == null) { throw new NonExistingServiceException(
                    "message.nonExistingExecutionDegree", null); }
            IPersistentExecutionYear persistentExecutionYear = persistentSuport
                    .getIPersistentExecutionYear();
            IExecutionYear execYear = new ExecutionYear();
            execYear.setIdInternal(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
            executionYear = (IExecutionYear) persistentExecutionYear.readByOId(execYear, false);

            if (executionYear == null) { throw new NonExistingServiceException(
                    "message.non.existing.execution.year", null); }
            persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
            oldExecutionDegree.setExecutionYear(executionYear);

            oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());

            oldExecutionDegree.setCampus(campus);

        }
        catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}