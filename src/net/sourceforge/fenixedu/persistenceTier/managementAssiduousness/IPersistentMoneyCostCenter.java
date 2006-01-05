package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentMoneyCostCenter extends IPersistentObject {
    public List readAllByYear(Integer year) throws ExcepcaoPersistencia;

    public IMoneyCostCenter readByCostCenterAndYear(ICostCenter costCenter, Integer year)
            throws ExcepcaoPersistencia;

}
