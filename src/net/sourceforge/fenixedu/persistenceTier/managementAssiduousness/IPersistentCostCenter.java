/*
 * Created on 16/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentCostCenter  extends IPersistentObject {
    public Integer countAllCostCenter() throws Exception;
    public List readAll() throws Exception;
    public ICostCenter readCostCenterByCode(String code) throws Exception;
}
