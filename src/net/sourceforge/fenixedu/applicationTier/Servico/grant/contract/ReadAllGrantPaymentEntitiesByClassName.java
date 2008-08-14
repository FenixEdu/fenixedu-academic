/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantPaymentEntitiesByClassName extends Service {

    public List run(String className) throws FenixServiceException {
	final Set<GrantPaymentEntity> grantPaymentEntities = GrantPaymentEntity.findGrantPaymentEntityByConcreteClass(className);
	final List<InfoGrantPaymentEntity> infoGrantPaymentEntities = new ArrayList<InfoGrantPaymentEntity>();
	for (final GrantPaymentEntity grantPaymentEntity : grantPaymentEntities) {
	    infoGrantPaymentEntities.add(InfoGrantPaymentEntity.newInfoFromDomain(grantPaymentEntity));
	}
	return infoGrantPaymentEntities;
    }

}