package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;

public class SetExtraCreditsToTSDTeacher extends FenixService {
    public void run(Integer tsdTeacherId, String extraCreditsName, Double extraCreditsValue, Boolean usingExtraCredits) {

        TSDTeacher tsdTeacher = rootDomainObject.readTSDTeacherByOID(tsdTeacherId);

        tsdTeacher.setExtraCreditsName(extraCreditsName);
        tsdTeacher.setExtraCreditsValue(extraCreditsValue);
        tsdTeacher.setUsingExtraCredits(usingExtraCredits);
    }
}
