/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithInfoBranch;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadCurricularCourseGroup implements IService {

    /**
     *  
     */
    public ReadCurricularCourseGroup() {
    }

    public InfoCurricularCourseGroup run(Integer groupId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup group = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId);
            return InfoCurricularCourseGroupWithInfoBranch.newInfoFromDomain(group);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}