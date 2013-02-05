package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllGrantPartsByGrantSubsidy extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static List run(Integer grantSubsidyId) throws FenixServiceException {
        List<InfoGrantPart> result = new ArrayList<InfoGrantPart>();

        GrantSubsidy grantSubsidy = rootDomainObject.readGrantSubsidyByOID(grantSubsidyId);
        List<GrantPart> grantParts = grantSubsidy.getAssociatedGrantParts();

        for (GrantPart grantPart : grantParts) {
            result.add(InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity.newInfoFromDomain(grantPart));
        }

        return result;
    }

}