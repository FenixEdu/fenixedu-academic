/*
 * Created on 09/01/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantTypes implements IService {
    public ReadAllGrantTypes() {
    }

    public List run() throws FenixServiceException {
        List grantTypes = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGrantType pgt = sp.getIPersistentGrantType();
            grantTypes = pgt.readAll();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (grantTypes == null)
            return new ArrayList();

        List grantTypeList = (ArrayList) CollectionUtils.collect(grantTypes, new Transformer() {
            public Object transform(Object input) {
                IGrantType grantType = (IGrantType) input;
                InfoGrantType infoGrantType = InfoGrantType.newInfoFromDomain(grantType);
                return infoGrantType;
            }
        });

        return grantTypeList;
    }

}