/*
 * Created on 16/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentMoneyCostCenter  extends IPersistentObject{
    public List readAllByYear(Integer year) throws Exception;
    public IMoneyCostCenter readByCostCenterAndYear(ICostCenter costCenter, Integer year) throws Exception;
}
