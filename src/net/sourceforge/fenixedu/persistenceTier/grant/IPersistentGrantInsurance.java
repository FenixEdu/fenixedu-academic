package net.sourceforge.fenixedu.persistenceTier.grant;

import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Barbosa
 * @author Pica
 */
public interface IPersistentGrantInsurance extends IPersistentObject {
    public GrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia;
}
