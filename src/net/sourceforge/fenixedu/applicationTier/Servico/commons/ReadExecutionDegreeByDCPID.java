package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByDCPID implements IService {

    /**
     * @param name
     * @param infoExecutionYear
     * @return InfoExecutionDegree
     * @throws FenixServiceException
     *             This method assumes thar there's only one Execution Degree
     *             for each Degree Curricular Plan. This is the case with the
     *             Master Degrees
     */
    public InfoExecutionDegree run(Integer degreeCurricularPlanID) throws FenixServiceException {

        IExecutionDegree executionDegree = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            executionDegree = sp.getIPersistentExecutionDegree().readbyDegreeCurricularPlanID(
                    degreeCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return (InfoExecutionDegree) Cloner.get(executionDegree);
    }

}