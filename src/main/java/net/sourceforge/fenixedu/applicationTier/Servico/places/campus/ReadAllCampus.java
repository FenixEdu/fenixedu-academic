package net.sourceforge.fenixedu.applicationTier.Servico.places.campus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixframework.Atomic;

public class ReadAllCampus {

    @Atomic
    public static List run() throws FenixServiceException {
        List<InfoCampus> result = new ArrayList<InfoCampus>();

        for (Campus campus : Campus.getAllActiveCampus()) {
            result.add(InfoCampus.newInfoFromDomain(campus));
        }

        return result;
    }

}