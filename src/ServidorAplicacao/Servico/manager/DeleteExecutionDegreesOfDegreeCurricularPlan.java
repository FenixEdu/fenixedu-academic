/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteExecutionDegreesOfDegreeCurricularPlan implements IService {

    // delete a set of executionDegrees
    public List run(List executionDegreesIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            ITurmaPersistente persistentClass = sp.getITurmaPersistente();

            Iterator iter = executionDegreesIds.iterator();

            List undeletedExecutionDegreesYears = new ArrayList();
            List classes;
            Integer executionDegreeId;
            IExecutionDegree executionDegree;

            while (iter.hasNext()) {

                executionDegreeId = (Integer) iter.next();

                executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                        ExecutionDegree.class, executionDegreeId);
                if (executionDegree != null) {
                    classes = persistentClass.readByExecutionDegree(executionDegree);
                    if (classes.isEmpty())
                        persistentExecutionDegree.delete(executionDegree);
                    else
                        undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());
                }
            }

            return undeletedExecutionDegreesYears;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}