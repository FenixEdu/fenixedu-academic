package ServidorPersistente.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.Date;
import java.util.List;

import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantContract extends IPersistentObject {

    public IGrantContract readGrantContractByNumberAndGrantOwner(Integer grantContractNumber,
            Integer grantOwnerId) throws ExcepcaoPersistencia;

    public List readAllContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;

    public Integer readMaxGrantContractNumberByGrantOwner(Integer grantOwnerId)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllActiveContractsByGrantOwner(Integer grantOwnerId) throws ExcepcaoPersistencia;

    public List readAllContractsByCriteria(String orderBy, Boolean justActiveContracts,
            Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract,
            Integer spanNumber, Integer numberOfElementsInSpan, Integer grantTypeId)
            throws ExcepcaoPersistencia;

    public Integer countAll();

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId)
            throws ExcepcaoPersistencia;
}