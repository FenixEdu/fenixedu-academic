package net.sourceforge.fenixedu.persistenceTier.grant;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Barbosa
 * @author Pica
 */
public interface IPersistentGrantInsurance extends IPersistentObject {
    public IGrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia;
}
