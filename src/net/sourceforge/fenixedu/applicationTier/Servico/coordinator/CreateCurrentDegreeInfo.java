package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;

public class CreateCurrentDegreeInfo extends FenixService {

    public DegreeInfo run(Degree degree) {
	return degree.createCurrentDegreeInfo();
    }

}
