/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithInfoBranch;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;

/**
 * @author João Mota
 * 
 */
public class ReadCurricularCourseGroup extends Service {

	public InfoCurricularCourseGroup run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSupport
				.getIPersistentCurricularCourseGroup();
		CurricularCourseGroup group = (CurricularCourseGroup) persistentCurricularCourseGroup
				.readByOID(CurricularCourseGroup.class, groupId);
		return InfoCurricularCourseGroupWithInfoBranch.newInfoFromDomain(group);
	}

}