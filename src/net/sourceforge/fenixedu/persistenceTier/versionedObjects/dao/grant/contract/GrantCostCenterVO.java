package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author pica
 * @author barbosa
 */
public class GrantCostCenterVO extends VersionedObjectsBase implements IPersistentGrantCostCenter {

    public IGrantCostCenter readGrantCostCenterByNumber(String number) throws ExcepcaoPersistencia {
        List<IGrantCostCenter> grantCostCenters = (List<IGrantCostCenter>) readAll(GrantCostCenter.class);

        for (IGrantCostCenter center : grantCostCenters) {
            if (center.getOjbConcreteClass().equals(
                    GrantPaymentEntity.getGrantCostCenterOjbConcreteClass())
                    && center.getNumber().equals(number)) {
                return center;
            }
        }

        return null;
    }

}