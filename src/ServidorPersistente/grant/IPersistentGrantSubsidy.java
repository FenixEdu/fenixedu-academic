package ServidorPersistente.grant;

import Dominio.grant.contract.IGrantSubsidy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;



/**
 * @author Pica
 * @author Barbosa
 */
public interface IPersistentGrantSubsidy extends IPersistentObject
{
    public IGrantSubsidy readActualGrantSubsidyByContract(Integer grantContractId) throws ExcepcaoPersistencia;
}
