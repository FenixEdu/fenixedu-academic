/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.grant;

import java.util.List;

import Dominio.grant.contract.IGrantPaymentEntity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantPaymentEntity extends IPersistentObject {
    public IGrantPaymentEntity readByNumberAndClass(String entityNumber, String entityClass)
            throws ExcepcaoPersistencia;

    public List readAllPaymentEntitiesByClassName(String className) throws ExcepcaoPersistencia;
}