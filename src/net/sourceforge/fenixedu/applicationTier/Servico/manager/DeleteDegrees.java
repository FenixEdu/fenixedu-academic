package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteDegrees implements IService {

    // delete a set of degrees
    public List run(List degreesInternalIds) throws FenixServiceException {

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();

            Iterator iter = degreesInternalIds.iterator();
            List degreeCurricularPlans;

            List undeletedDegreesNames = new ArrayList();

            while (iter.hasNext()) {

                Integer internalId = (Integer) iter.next();
                IDegree degree = (IDegree)persistentDegree.readByOID(Degree.class,internalId);
                
                if (degree != null) {
                
                    degreeCurricularPlans = degree.getDegreeCurricularPlans();
                    
                    if (degreeCurricularPlans.isEmpty() &&
                            degree.getDelegate().isEmpty() &&
                            degree.getOldInquiriesCoursesRes().isEmpty() &&
                            degree.getOldInquiriesSummary().isEmpty() &&
                            degree.getOldInquiriesSummary().isEmpty()) {
                        
                        dereferenceDegree(degree, sp);
                        
                        persistentDegree.deleteByOID(Degree.class,degree.getIdInternal());
                    }
                    else
                        undeletedDegreesNames.add(degree.getNome());
                }
            }

            return undeletedDegreesNames;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
    
    private void dereferenceDegree(IDegree degree, ISuportePersistente sp) throws ExcepcaoPersistencia {
        
        IPersistentDegreeInfo pdi = sp.getIPersistentDegreeInfo();
        
        List degreeInfos = degree.getDegreeInfos();
        for (Iterator it = degreeInfos.iterator();it.hasNext();) {
            IDegreeInfo degreeInfo = (IDegreeInfo)it.next();
            pdi.deleteByOID(DegreeInfo.class,degreeInfo.getIdInternal());
        }
        degreeInfos.clear();
    }

}