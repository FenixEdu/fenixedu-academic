package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import java.util.List;

import DataBeans.grant.list.InfoSpanByCriteriaListGrantOwner;
import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantContract extends IPersistentObject
{

    public IGrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,Integer grantOwnerId)
        throws ExcepcaoPersistencia;
	public List readAllContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;
	public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;    
	public List readAllActiveContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;
	public List readAllContractsByGrantOwnerAndCriteria(Integer grantOwnerId, InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner) throws ExcepcaoPersistencia;
}