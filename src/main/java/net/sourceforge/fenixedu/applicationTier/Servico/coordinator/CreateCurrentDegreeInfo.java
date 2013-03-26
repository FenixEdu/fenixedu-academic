package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCurrentDegreeInfo extends FenixService {

    @Service
    public static DegreeInfo run(Degree degree) {
        return degree.createCurrentDegreeInfo();
    }

}