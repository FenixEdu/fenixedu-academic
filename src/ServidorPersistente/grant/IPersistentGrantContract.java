package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantContract extends IPersistentObject
{

    public IGrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,Integer grantOwnerId)
        throws ExcepcaoPersistencia;
	public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;
        
}