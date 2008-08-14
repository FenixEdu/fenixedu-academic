package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllGrantPartsByGrantSubsidy extends Service {

    public List run(Integer grantSubsidyId) throws FenixServiceException {
	List<InfoGrantPart> result = new ArrayList<InfoGrantPart>();

	GrantSubsidy grantSubsidy = rootDomainObject.readGrantSubsidyByOID(grantSubsidyId);
	List<GrantPart> grantParts = grantSubsidy.getAssociatedGrantParts();

	for (GrantPart grantPart : grantParts) {
	    result.add(InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity.newInfoFromDomain(grantPart));
	}

	return result;
    }

}
