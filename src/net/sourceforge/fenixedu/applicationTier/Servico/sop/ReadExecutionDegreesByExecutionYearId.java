package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByExecutionYearId implements IService {

    /**
     * The actor of this class.
     */
    public ReadExecutionDegreesByExecutionYearId() {

    }

    public List run(Integer executionYearId) throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionYear executionYear = null;
            if (executionYearId == null) {
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            } else {
                executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                        executionYearId);
            }

            List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());

            if (executionDegrees != null && executionDegrees.size() > 0) {
                Iterator iterator = executionDegrees.iterator();
                infoExecutionDegreeList = new ArrayList();

                while (iterator.hasNext()) {
                    IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
                    infoExecutionDegreeList.add(Cloner.get(executionDegree));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }

}