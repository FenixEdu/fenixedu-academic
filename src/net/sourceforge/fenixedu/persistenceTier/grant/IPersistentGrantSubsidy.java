package net.sourceforge.fenixedu.persistenceTier.grant;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantSubsidy extends IPersistentObject {
    public List readAllSubsidiesByGrantContract(Integer idContract) throws ExcepcaoPersistencia;

    public List<GrantSubsidy> readAllSubsidiesByGrantContractAndState(Integer idContract, Integer state)
            throws ExcepcaoPersistencia;

}
