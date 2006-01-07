package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantInsuranceVO extends VersionedObjectsBase implements IPersistentGrantInsurance {

    public GrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia {

        List<GrantInsurance> grantInsurances = (List<GrantInsurance>) readAll(GrantInsurance.class);

        for (GrantInsurance insurance : grantInsurances) {
            if(insurance.getKeyGrantContract().equals(key_contract)) {
                return insurance;
            }
        } 

        return null;
    }

}
