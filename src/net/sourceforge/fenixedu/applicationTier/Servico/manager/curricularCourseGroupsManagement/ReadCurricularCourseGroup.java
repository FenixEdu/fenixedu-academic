/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithInfoBranch;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author João Mota
 * 
 */
public class ReadCurricularCourseGroup implements IService {

	public InfoCurricularCourseGroup run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
				.getIPersistentCurricularCourseGroup();
		CurricularCourseGroup group = (CurricularCourseGroup) persistentCurricularCourseGroup
				.readByOID(CurricularCourseGroup.class, groupId);
		return InfoCurricularCourseGroupWithInfoBranch.newInfoFromDomain(group);
	}

}