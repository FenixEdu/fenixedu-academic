package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentCostCenter extends IPersistentObject {
    public Integer countAllCostCenter();

    public ICostCenter readCostCenterByCode(String code) throws ExcepcaoPersistencia;

}
