package net.sourceforge.fenixedu.applicationTier.Servico.general;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistrict;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllDistricts extends Service {

    public List<InfoDistrict> run() {
        List<InfoDistrict> result = new ArrayList<InfoDistrict>();
        
        for (District district : rootDomainObject.getDistricts()) {
            result.add(InfoDistrict.newInfoFromDomain(district));
        }
        
        return result;
    }

}
