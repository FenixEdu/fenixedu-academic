/*
 * Created on Jan 23, 2004
 */
package ServidorPersistente.grant;

import java.util.List;

import Dominio.grant.contract.IGrantPart;
import Dominio.grant.contract.IGrantPaymentEntity;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Pica
 * @author Barbosa
 */
public interface IPersistentGrantPart extends IPersistentObject
{
	public List readAllGrantPartsByGrantSubsidy(Integer grantSubsidyId) throws ExcepcaoPersistencia;
	public IGrantPart readGrantPartByUnique(
		IGrantSubsidy grantSubsidy,
		IGrantPaymentEntity paymentEntity)
		throws ExcepcaoPersistencia;
}
