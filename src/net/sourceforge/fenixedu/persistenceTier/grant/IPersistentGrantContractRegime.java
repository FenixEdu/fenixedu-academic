package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantContractRegime extends IPersistentObject {
    public List readGrantContractRegimeByGrantContract(Integer grantContractId)
            throws ExcepcaoPersistencia;

    public List<GrantContractRegime> readGrantContractRegimeByGrantContractAndState(Integer grantContractId, Integer state)
            throws ExcepcaoPersistencia;
}