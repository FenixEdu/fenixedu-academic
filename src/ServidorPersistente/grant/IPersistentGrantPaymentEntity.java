/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.grant;

import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantPaymentEntity extends IPersistentObject
{
    public IGrantPaymentEntity readByNumber(Integer entityNumber) throws ExcepcaoPersistencia;
}
