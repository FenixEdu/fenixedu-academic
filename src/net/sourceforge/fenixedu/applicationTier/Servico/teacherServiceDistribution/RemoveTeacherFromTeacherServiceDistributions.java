package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author jpmsit, amak
 */
public class RemoveTeacherFromTeacherServiceDistributions extends Service {

    public void run(Integer tsdId, Integer tsdTeacherId) throws FenixServiceException {

	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
	TSDTeacher tsdTeacher = rootDomainObject.readTSDTeacherByOID(tsdTeacherId);

	tsd.removeTSDTeacherFromAllChilds(tsdTeacher);
	if (tsd.getIsRoot()) {
	    tsdTeacher.delete();
	}
    }
}
