package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.MoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentMoneyCostCenter extends IPersistentObject {
    public List readAllByYear(Integer year) throws ExcepcaoPersistencia;

    public MoneyCostCenter readByCostCenterAndYear(CostCenter costCenter, Integer year)
            throws ExcepcaoPersistencia;

}
