/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantPaymentEntitiesByClassName implements IService {
    public ReadAllGrantPaymentEntitiesByClassName() {
    }

    public List run(String className) throws FenixServiceException {
        List result = null;
        IPersistentGrantPaymentEntity pgpe = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            pgpe = sp.getIPersistentGrantPaymentEntity();
            List grantPaymentEntities = pgpe.readAllPaymentEntitiesByClassName(className);

            result = (List) CollectionUtils.collect(grantPaymentEntities, new Transformer() {
                public Object transform(Object o) {
                    IGrantPaymentEntity grantPaymentEntity = (IGrantPaymentEntity) o;
                    return InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity);
                }
            });

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        return result;
    }
}