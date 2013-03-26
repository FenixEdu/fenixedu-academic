package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistrict;
import net.sourceforge.fenixedu.domain.District;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllDistricts extends FenixService {

    @Service
    public static List<InfoDistrict> run() {
        List<InfoDistrict> result = new ArrayList<InfoDistrict>();

        for (District district : rootDomainObject.getDistricts()) {
            result.add(InfoDistrict.newInfoFromDomain(district));
        }

        return result;
    }

}