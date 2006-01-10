package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantContractRegimeVO extends VersionedObjectsBase implements
        IPersistentGrantContractRegime {

    public List readGrantContractRegimeByGrantContract(Integer grantContractId)
            throws ExcepcaoPersistencia {

        List<GrantContractRegime> grantContractRegimes = (List<GrantContractRegime>) readAll(GrantContractRegime.class);
        List<GrantContractRegime> result = null;

        for (GrantContractRegime regime : grantContractRegimes) {
            if (regime.getKeyGrantContract().equals(grantContractId)) {
                result.add(regime);
            }
        }

        return result;
    }

    public List readGrantContractRegimeByGrantContractAndState(Integer grantContractId, Integer state)
            throws ExcepcaoPersistencia {

        List<GrantContractRegime> grantContractRegimes = (List<GrantContractRegime>) readAll(GrantContractRegime.class);
        List<GrantContractRegime> result = null;

        for (GrantContractRegime regime : grantContractRegimes) {
            if (regime.getKeyGrantContract().equals(grantContractId) && regime.getState().equals(state)) {
                result.add(regime);
            }
        }

        return result;
    }

}