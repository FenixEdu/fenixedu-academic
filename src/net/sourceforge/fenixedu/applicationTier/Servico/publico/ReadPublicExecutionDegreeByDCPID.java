package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID implements IService {

    /**
     * @param name
     * @param infoExecutionYear
     * @return InfoExecutionDegree
     * @throws FenixDomainException
     *             This method assumes thar there's only one Execution Degree
     *             for each Degree Curricular Plan. This is the case with the
     *             Master Degrees
     */

    private static ReadPublicExecutionDegreeByDCPID service = new ReadPublicExecutionDegreeByDCPID();

    public ReadPublicExecutionDegreeByDCPID() {
    }

    public String getNome() {
        return "ReadPublicExecutionDegreeByDCPID";
    }

    public static ReadPublicExecutionDegreeByDCPID getService() {
        return service;
    }

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException {

        List executionDegrees = null;
        List result = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            executionDegrees = sp.getIPersistentExecutionDegree()
                    .readExecutionDegreesbyDegreeCurricularPlanID(degreeCurricularPlanID);

            result = (List) CollectionUtils.collect(executionDegrees, new Transformer() {

                public Object transform(Object input) {
                    IExecutionDegree executionDegree = (IExecutionDegree) input;
                    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner
                            .get(executionDegree);
                    return infoExecutionDegree;
                }
            });

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegrees == null) {
            throw new NonExistingServiceException();
        }
        return result;
    }

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionYearID)
            throws FenixServiceException {

        IExecutionDegree executionDegrees = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            executionDegrees = sp.getIPersistentExecutionDegree()
                    .readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
                            degreeCurricularPlanID, executionYearID);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegrees == null) {
            return null;
            //throw new NonExistingServiceException();
        }
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegrees);
        return infoExecutionDegree;
    }

}