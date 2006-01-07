package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentCostCenter extends IPersistentObject {
    public Integer countAllCostCenter();

    public CostCenter readCostCenterByCode(String code) throws ExcepcaoPersistencia;

}
