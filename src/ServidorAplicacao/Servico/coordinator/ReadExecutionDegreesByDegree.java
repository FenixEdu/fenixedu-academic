package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 24/Nov/2003
 */

public class ReadExecutionDegreesByDegree implements IServico
{

    private static ReadExecutionDegreesByDegree service = new ReadExecutionDegreesByDegree();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreesByDegree getService()
    {
        return service;
    }

    /**
	 * The constructor of this class.
	 */
    private ReadExecutionDegreesByDegree()
    {
    }

    /**
	 * Service name
	 */
    public final String getNome()
    {
        return "ReadExecutionDegreesByDegree";
    }

    /**
	 * Executes the service. Returns the current collection of
	 * infoExecutionDegrees.
	 */
    public List run(Integer idDegree) throws FenixServiceException
    {
        ISuportePersistente sp;
        List allExecutionDegrees = null;
        try
        {
            ICurso degree = new Curso();
            degree.setIdInternal(idDegree);

            sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente cursoExecucaoPersistente = sp.getICursoExecucaoPersistente();

            allExecutionDegrees = cursoExecucaoPersistente.readExecutionsDegreesByDegree(degree);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty())
        {
            throw new FenixServiceException();
        }

        // build the result of this service
        Iterator iterator = allExecutionDegrees.iterator();
        List allInfoExecutionDegrees = new ArrayList(allExecutionDegrees.size());

        while (iterator.hasNext())
            allInfoExecutionDegrees.add(
                Cloner.get((ICursoExecucao) iterator.next()));

        return allInfoExecutionDegrees;
    }
}