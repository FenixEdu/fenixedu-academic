package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;

public class CreateCurrentDegreeInfo extends Service {

    public DegreeInfo run(Degree degree) {
	return degree.createCurrentDegreeInfo();
    }

}
