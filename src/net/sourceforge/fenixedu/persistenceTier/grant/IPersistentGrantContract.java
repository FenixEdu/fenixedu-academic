package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

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