package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAllGrantTypes extends Service {

    public List run() {
        List<GrantType> grantTypes = rootDomainObject.getGrantTypes();
        return (List) CollectionUtils.collect(grantTypes, new Transformer() {
            public Object transform(Object input) {
                GrantType grantType = (GrantType) input;
                return InfoGrantType.newInfoFromDomain(grantType);

            }
        });

    }

}
