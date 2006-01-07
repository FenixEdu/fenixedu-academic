package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantSubsidyVO extends VersionedObjectsBase implements IPersistentGrantSubsidy {

    public List readAllSubsidiesByGrantContract(Integer idContract) throws ExcepcaoPersistencia {
        List<GrantSubsidy> grantSubsidies = (List<GrantSubsidy>) readAll(GrantSubsidy.class);
        List<GrantSubsidy> result = new ArrayList();

        for (GrantSubsidy subsidy : grantSubsidies) {
            if (subsidy.getKeyGrantContract().equals(idContract)) {
                result.add(subsidy);
            }
        }

        return result;
    }

    public List readAllSubsidiesByGrantContractAndState(Integer idContract, Integer state)
            throws ExcepcaoPersistencia {
        List<GrantSubsidy> grantSubsidies = (List<GrantSubsidy>) readAll(GrantSubsidy.class);
        List<GrantSubsidy> result = new ArrayList();

        for (GrantSubsidy subsidy : grantSubsidies) {
            if (subsidy.getKeyGrantContract().equals(idContract) && subsidy.getState().equals(state)) {
                result.add(subsidy);
            }
        }

        return result;
    }

}