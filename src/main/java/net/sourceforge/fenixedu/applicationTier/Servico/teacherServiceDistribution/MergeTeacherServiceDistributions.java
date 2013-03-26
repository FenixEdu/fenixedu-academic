package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class MergeTeacherServiceDistributions extends FenixService {
    public void run(Integer tsdId, Integer otherGroupingId) {

        TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
        TeacherServiceDistribution otherGrouping = rootDomainObject.readTeacherServiceDistributionByOID(otherGroupingId);

        tsd.mergeWithGrouping(otherGrouping);
    }
}
