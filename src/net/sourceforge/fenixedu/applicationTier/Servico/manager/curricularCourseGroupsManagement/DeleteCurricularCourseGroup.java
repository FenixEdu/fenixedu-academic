package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;


public class DeleteCurricularCourseGroup extends Service {

    public void run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
        CurricularCourseGroup curricularCourseGroup = rootDomainObject.readCurricularCourseGroupByOID(groupId);
		if (curricularCourseGroup != null) {
			try {
				curricularCourseGroup.delete();
			} catch (DomainException e) {
				throw new InvalidArgumentsServiceException();
			}
		}
		// inexistent CurricularCourseGroup
    }

}