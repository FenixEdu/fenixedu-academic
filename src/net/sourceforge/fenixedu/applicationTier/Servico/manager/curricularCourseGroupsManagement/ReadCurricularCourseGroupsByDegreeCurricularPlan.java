/*
 * Created on Jul 27, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithInfoBranch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadCurricularCourseGroupsByDegreeCurricularPlan implements IService {

    /**
     *  
     */
    public ReadCurricularCourseGroupsByDegreeCurricularPlan() {

    }

    public List run(Integer degreeCurricularPlanId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);
            List groups = new ArrayList();
            List areas = degreeCurricularPlan.getAreas();
            Iterator iter = areas.iterator();
            while (iter.hasNext()) {
                IBranch branch = (IBranch) iter.next();
                groups.addAll(branch.getAreaCurricularCourseGroups());
                groups.addAll(branch.getOptionalCurricularCourseGroups());
            }

            return (List) CollectionUtils.collect(groups, new Transformer() {

                public Object transform(Object arg0) {
                    return InfoCurricularCourseGroupWithInfoBranch.newInfoFromDomain((ICurricularCourseGroup) arg0);
                }
            });

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}