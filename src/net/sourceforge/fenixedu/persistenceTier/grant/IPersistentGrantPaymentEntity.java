/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.grant;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantPaymentEntity extends IPersistentObject {
    public IGrantPaymentEntity readByNumberAndClass(String entityNumber, String entityClass)
            throws ExcepcaoPersistencia;

    public List readAllPaymentEntitiesByClassName(String className) throws ExcepcaoPersistencia;
}