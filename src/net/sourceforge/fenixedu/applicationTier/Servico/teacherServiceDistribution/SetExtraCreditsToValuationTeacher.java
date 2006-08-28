package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;

public class SetExtraCreditsToValuationTeacher extends Service {
	public void run(
			Integer valuationTeacherId,
			String extraCreditsName,
			Double extraCreditsValue,
			Boolean usingExtraCredits) {
		
		ValuationTeacher valuationTeacher = rootDomainObject.readValuationTeacherByOID(valuationTeacherId);
		
		valuationTeacher.setExtraCreditsName(extraCreditsName);
		valuationTeacher.setExtraCreditsValue(extraCreditsValue);
		valuationTeacher.setUsingExtraCredits(usingExtraCredits);
	}
}
