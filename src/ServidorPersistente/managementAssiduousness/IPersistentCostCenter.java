/*
 * Created on 16/Dez/2004
 */
package ServidorPersistente.managementAssiduousness;

import java.util.List;

import Dominio.ICostCenter;
import ServidorPersistente.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentCostCenter  extends IPersistentObject {
    public Integer countAllCostCenter() throws Exception;
    public List readAll() throws Exception;
    public ICostCenter readCostCenterByCode(String code) throws Exception;
}
