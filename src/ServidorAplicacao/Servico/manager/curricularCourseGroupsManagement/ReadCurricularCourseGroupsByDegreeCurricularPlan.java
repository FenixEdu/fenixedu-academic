/*
 * Created on Jul 27, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseGroupWithInfoBranch;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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

                    return InfoCurricularCourseGroupWithInfoBranch
                            .newInfoFromDomain((ICurricularCourseGroup) arg0);
                }
            });

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}