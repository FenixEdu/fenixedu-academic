package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantContractMovement extends IPersistentObject
{
    public List readAllMovementsByContract(Integer contractId) throws ExcepcaoPersistencia;
}