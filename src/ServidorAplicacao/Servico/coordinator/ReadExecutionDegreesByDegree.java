package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 24/Nov/2003
 */

public class ReadExecutionDegreesByDegree implements IService {

    /**
     * Executes the service. Returns the current collection of
     * infoExecutionDegrees.
     */
    public List run(Integer idDegree) throws FenixServiceException {
        ISuportePersistente sp;
        List allExecutionDegrees = null;
        try {
            ICurso degree = new Curso();
            degree.setIdInternal(idDegree);

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree cursoExecucaoPersistente = sp.getIPersistentExecutionDegree();

            allExecutionDegrees = cursoExecucaoPersistente.readExecutionsDegreesByDegree(degree);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty()) {
            throw new FenixServiceException();
        }

        // build the result of this service
        Iterator iterator = allExecutionDegrees.iterator();
        List allInfoExecutionDegrees = new ArrayList(allExecutionDegrees.size());

        while (iterator.hasNext())
            allInfoExecutionDegrees.add(Cloner.get((ICursoExecucao) iterator.next()));

        return allInfoExecutionDegrees;
    }
}