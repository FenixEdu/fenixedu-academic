/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadLastGrantContractCreatedByGrantOwner extends Service {

    public InfoGrantContract run(Integer grantOwnerId) throws FenixServiceException {

	GrantOrientationTeacher grantOrientationTeacher = null;
	final GrantOwner grantOwner = rootDomainObject.readGrantOwnerByOID(grantOwnerId);
	final GrantContract grantContract = grantOwner.readGrantContractWithMaximumContractNumber();
	if (grantContract == null) {
	    return new InfoGrantContract();
	}
	grantOrientationTeacher = grantContract.readActualGrantOrientationTeacher();
	if (grantOrientationTeacher == null) {
	    throw new FenixServiceException();
	}

	InfoGrantContract infoGrantContract = null;

	infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantContract);
	infoGrantContract.setGrantOrientationTeacherInfo(InfoGrantOrientationTeacherWithTeacherAndGrantContract
		.newInfoFromDomain(grantOrientationTeacher));

	return infoGrantContract;
    }
}