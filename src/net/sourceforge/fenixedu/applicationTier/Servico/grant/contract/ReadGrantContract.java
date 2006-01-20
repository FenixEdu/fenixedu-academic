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
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadGrantContract extends ReadDomainObjectService {

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
		return persistentSupport.getIPersistentGrantContract();
	}

	protected InfoObject newInfoFromDomain(DomainObject domainObject) {
		return InfoGrantContractWithGrantOwnerAndGrantType
				.newInfoFromDomain((GrantContract) domainObject);
	}

	protected Class getDomainObjectClass() {
		return GrantContract.class;
	}

	public InfoObject run(Integer objectId) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentGrantOrientationTeacher pgot = persistentSupport.getIPersistentGrantOrientationTeacher();

		InfoGrantContract infoGrantContract = (InfoGrantContract) super.run(objectId);

		// get the GrantOrientationTeacher for the contract
		GrantOrientationTeacher orientationTeacher = pgot.readActualGrantOrientationTeacherByContract(
				infoGrantContract.getIdInternal(), new Integer(0));
		InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
				.newInfoFromDomain(orientationTeacher);
		infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

		return infoGrantContract;
	}

}
