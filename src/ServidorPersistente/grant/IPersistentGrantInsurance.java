/*
 * Created on Jun 26, 2004
 */
package ServidorPersistente.grant;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;


/**
 * @author Barbosa
 * @author Pica
 */
public interface IPersistentGrantInsurance extends IPersistentObject
{
    public List readGrantInsuranceByGrantContract(Integer key_contract) throws ExcepcaoPersistencia;
    public List readGrantInsuranceByGrantContractAndState(Integer key_contract, Integer state) throws ExcepcaoPersistencia;
}
