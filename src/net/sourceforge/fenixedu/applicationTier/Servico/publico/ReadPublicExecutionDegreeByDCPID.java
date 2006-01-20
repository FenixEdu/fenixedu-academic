package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID extends Service {

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException, ExcepcaoPersistencia {

        List executionDegrees = null;
        List result = new ArrayList();
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        executionDegrees = persistentSupport.getIPersistentExecutionDegree()
                .readByDegreeCurricularPlan(degreeCurricularPlanID);

        result = (List) CollectionUtils.collect(executionDegrees, new Transformer() {

            public Object transform(Object input) {
                return copyExecutionDegree2InfoExecutionDegree((ExecutionDegree) input);
            }
        });

        if (executionDegrees == null) {
            throw new NonExistingServiceException();
        }
        return result;
    }

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionYearID)
            throws ExcepcaoPersistencia {

        ExecutionDegree executionDegree = null;
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        executionDegree = persistentSupport.getIPersistentExecutionDegree()
                .readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(degreeCurricularPlanID,
                        executionYearID);

        if (executionDegree == null) {
            return null;
        }

        return copyExecutionDegree2InfoExecutionDegree(executionDegree);
    }

    protected InfoExecutionDegree copyExecutionDegree2InfoExecutionDegree(
            ExecutionDegree executionDegree) {
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree
                .getExecutionYear());
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        Degree degree = degreeCurricularPlan.getDegree();
        InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

        return infoExecutionDegree;
    }

}