/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.grant;

import Dominio.grant.contract.IGrantCostCenter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantCostCenter extends IPersistentObject
{
	public IGrantCostCenter readGrantCostCenterByNumber(String number) throws ExcepcaoPersistencia;
}
