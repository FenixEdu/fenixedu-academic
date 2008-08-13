/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantContract extends ReadDomainObjectService {

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantContractWithGrantOwnerAndGrantType
                .newInfoFromDomain((GrantContract) domainObject);
    }

    public InfoObject run(Integer objectId) throws FenixServiceException{

        GrantContract grantContract = rootDomainObject.readGrantContractByOID(objectId);
        InfoGrantContract infoGrantContract = (InfoGrantContract) newInfoFromDomain(grantContract);

        // get the GrantOrientationTeacher for the contract
        GrantOrientationTeacher orientationTeacher = grantContract.readActualGrantOrientationTeacher();
        InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
                .newInfoFromDomain(orientationTeacher);
        infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

        return infoGrantContract;
    }

	@Override
	protected DomainObject readDomainObject(final Integer idInternal) {
		return rootDomainObject.readGrantContractByOID(idInternal);
	}

}
