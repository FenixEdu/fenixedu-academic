/*
 * Created on Jun 26, 2004
 */
package ServidorPersistente.grant;

import Dominio.grant.contract.IGrantInsurance;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Barbosa
 * @author Pica
 */
public interface IPersistentGrantInsurance extends IPersistentObject {
    public IGrantInsurance readGrantInsuranceByGrantContract(Integer key_contract)
            throws ExcepcaoPersistencia;
}