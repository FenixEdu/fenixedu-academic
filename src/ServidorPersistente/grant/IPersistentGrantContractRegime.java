package ServidorPersistente.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantContractRegime extends IPersistentObject {
    public List readGrantContractRegimeByGrantContract(Integer grantContractId)
            throws ExcepcaoPersistencia;

    public List readGrantContractRegimeByGrantContractAndState(Integer grantContractId, Integer state)
            throws ExcepcaoPersistencia;
}