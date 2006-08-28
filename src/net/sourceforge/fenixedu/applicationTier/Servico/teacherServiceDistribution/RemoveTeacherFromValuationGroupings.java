package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author jpmsit, amak
 */
public class RemoveTeacherFromValuationGroupings extends Service {

	public void run(Integer valuationGroupingId, Integer valuationTeacherId) throws FenixServiceException, ExcepcaoPersistencia {

		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		ValuationTeacher valuationTeacher = rootDomainObject.readValuationTeacherByOID(valuationTeacherId);

		valuationGrouping.removeValuationTeacherFromAllChilds(valuationTeacher);
		if(valuationGrouping.getIsRoot()) {		
			valuationTeacher.delete();
		}
	}
}
