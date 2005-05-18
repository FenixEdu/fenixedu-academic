package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantInsuranceVO extends VersionedObjectsBase implements IPersistentGrantInsurance {

    public IGrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia {

        List<IGrantInsurance> grantInsurances = (List<IGrantInsurance>) readAll(GrantInsurance.class);

        for (IGrantInsurance insurance : grantInsurances) {
            if(insurance.getKeyGrantContract().equals(key_contract)) {
                return insurance;
            }
        } 

        return null;
    }

}
