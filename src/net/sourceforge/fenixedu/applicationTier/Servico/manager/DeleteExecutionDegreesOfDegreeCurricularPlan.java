/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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