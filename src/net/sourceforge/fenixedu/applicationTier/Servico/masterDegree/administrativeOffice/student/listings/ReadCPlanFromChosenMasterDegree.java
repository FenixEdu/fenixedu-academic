package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCPlanFromChosenMasterDegree implements IService {

    public List run(Integer idInternal) throws FenixServiceException {
        ISuportePersistente sp = null;
        List degreeCurricularPlansList = new ArrayList();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Get the Master Degree
            IDegree degree = null;
            degree = sp.getICursoPersistente().readByIdInternal(idInternal);

            // Get the List of Degree Curricular Plans
            degreeCurricularPlansList = degree.getDegreeCurricularPlans();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        Iterator iterator = degreeCurricularPlansList.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            result.add(Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan((IDegreeCurricularPlan) iterator
                            .next()));
        }

        return result;
    }

}