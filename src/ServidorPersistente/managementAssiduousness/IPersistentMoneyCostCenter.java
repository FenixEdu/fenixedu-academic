/*
 * Created on 16/Dez/2004
 */
package ServidorPersistente.managementAssiduousness;

import java.util.List;

import Dominio.ICostCenter;
import Dominio.managementAssiduousness.IMoneyCostCenter;
import ServidorPersistente.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentMoneyCostCenter  extends IPersistentObject{
    public List readAllByYear(Integer year) throws Exception;
    public IMoneyCostCenter readByCostCenterAndYear(ICostCenter costCenter, Integer year) throws Exception;
}
