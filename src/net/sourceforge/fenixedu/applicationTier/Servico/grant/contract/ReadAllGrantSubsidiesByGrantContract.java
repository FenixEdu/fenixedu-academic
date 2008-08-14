/*
 * Created on 04 Mar 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadAllGrantSubsidiesByGrantContract extends Service {

    public List run(Integer idContract) throws FenixServiceException {
	GrantContract grantContract = rootDomainObject.readGrantContractByOID(idContract);
	List<GrantSubsidy> subsidies = grantContract.getAssociatedGrantSubsidies();

	if (subsidies == null)
	    return new ArrayList();

	final List infoSubsidyList = new ArrayList();
	for (final GrantSubsidy grantSubsidy : grantContract.getAssociatedGrantSubsidiesSet()) {
	    InfoGrantSubsidy infoGrantSubsidy = InfoGrantSubsidyWithContract.newInfoFromDomain(grantSubsidy);
	    infoSubsidyList.add(infoGrantSubsidy);
	}
	return infoSubsidyList;
    }
}