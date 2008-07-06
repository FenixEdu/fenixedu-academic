/*
 * Created on Jun 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.list;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoListGrantContract extends InfoObject {

    private final DomainReference<GrantContract> domainReference;

    public InfoListGrantContract(final GrantContract domainObject) {
	domainReference = new DomainReference<GrantContract>(domainObject);
    }

    private GrantContract getGrantContract() {
	return domainReference == null ? null : domainReference.getObject();
    }

    @Override
    public boolean equals(Object obj) {
	return obj != null && getGrantContract() == ((InfoListGrantContract) obj).getGrantContract();
    }

    public InfoGrantContract getInfoGrantContract() {
	final InfoGrantContract result = InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(getGrantContract());
	result.setGrantOrientationTeacherInfo(InfoGrantOrientationTeacherWithTeacherAndGrantContract
		.newInfoFromDomain(getGrantContract().readActualGrantOrientationTeacher()));

	return result;
    }

    public List<InfoGrantContractRegime> getInfoGrantContractRegimes() {
	// The actual regime is in the first position

	final List<InfoGrantContractRegime> result = new ArrayList<InfoGrantContractRegime>();
	for (final GrantContractRegime grantContractRegime : getGrantContract().readGrantContractRegimeByGrantContract()) {
	    final InfoGrantContractRegime info = InfoGrantContractRegimeWithTeacherAndContract
		    .newInfoFromDomain(grantContractRegime);
	    result.add(info);
	}

	return result;
    }

    public List<InfoListGrantSubsidy> getInfoListGrantSubsidys() {
	// The actual subsidy is in the first position

	final List<InfoListGrantSubsidy> result = new ArrayList<InfoListGrantSubsidy>();
	for (GrantSubsidy grantSubsidy : getGrantContract().getAssociatedGrantSubsidies()) {
	    final InfoListGrantSubsidy info = new InfoListGrantSubsidy();
	    info.setInfoGrantSubsidy(InfoGrantSubsidyWithContract.newInfoFromDomain(grantSubsidy));

	    final List<InfoGrantPart> infoSubsidyParts = new ArrayList<InfoGrantPart>();
	    for (final GrantPart grantPart : grantSubsidy.getAssociatedGrantParts()) {
		infoSubsidyParts.add(InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity.newInfoFromDomain(grantPart));
	    }
	    info.setInfoGrantParts(infoSubsidyParts);

	    result.add(info);
	}

	return result;
    }

}
