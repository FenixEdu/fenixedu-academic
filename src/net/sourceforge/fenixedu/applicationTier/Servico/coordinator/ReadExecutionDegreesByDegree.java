package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            IDegree degree = new Degree();
            degree.setIdInternal(idDegree);

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
            allInfoExecutionDegrees.add(Cloner.get((IExecutionDegree) iterator.next()));

        return allInfoExecutionDegrees;
    }
}