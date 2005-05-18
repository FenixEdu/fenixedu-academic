package net.sourceforge.fenixedu.persistenceTier.grant;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantCostCenter extends IPersistentObject {
    public IGrantCostCenter readGrantCostCenterByNumber(String number) throws ExcepcaoPersistencia;
}