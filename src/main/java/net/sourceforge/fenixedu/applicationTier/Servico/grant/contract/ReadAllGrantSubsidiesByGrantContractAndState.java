/*
 * Created on 04 Mar 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantSubsidiesByGrantContractAndState extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static List run(Integer idContract, Integer state) throws FenixServiceException {
        final GrantContract grantContract = rootDomainObject.readGrantContractByOID(idContract);
        final Set<GrantSubsidy> subsidies = grantContract.findGrantSubsidiesByState(state);
        if (subsidies == null) {
            return new ArrayList();
        }

        List infoSubsidyList = (List) CollectionUtils.collect(subsidies, new Transformer() {
            @Override
            public Object transform(Object input) {
                GrantSubsidy grantSubsidy = (GrantSubsidy) input;
                InfoGrantSubsidy infoGrantSubsidy = InfoGrantSubsidyWithContract.newInfoFromDomain(grantSubsidy);
                return infoGrantSubsidy;
            }
        });
        return infoSubsidyList;
    }
}