/*
 * Created on 09/01/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantTypes implements IService {

    public List run() throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List grantTypes = sp.getIPersistentGrantType().readAll();

        if (grantTypes == null)
            return new ArrayList();

        return (List) CollectionUtils.collect(grantTypes, new Transformer() {
            public Object transform(Object input) {
                GrantType grantType = (GrantType) input;
                return InfoGrantType.newInfoFromDomain(grantType);

            }
        });

    }

}