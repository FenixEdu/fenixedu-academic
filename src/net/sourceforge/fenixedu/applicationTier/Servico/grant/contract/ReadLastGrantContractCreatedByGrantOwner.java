/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadLastGrantContractCreatedByGrantOwner extends Service {

	public InfoGrantContract run(Integer grantOwnerId) throws FenixServiceException,
			ExcepcaoPersistencia {
		Integer grantContractNumber = null;
		GrantContract grantContract = null;
		GrantOrientationTeacher grantOrientationTeacher = null;
		IPersistentGrantContract persistentGrantContract = null;
		IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		persistentGrantContract = sp.getIPersistentGrantContract();
		persistentGrantOrientationTeacher = sp.getIPersistentGrantOrientationTeacher();

		// set the contract number!
		grantContractNumber = persistentGrantContract
				.readMaxGrantContractNumberByGrantOwner(grantOwnerId);
		grantContract = persistentGrantContract.readGrantContractByNumberAndGrantOwner(
				grantContractNumber, grantOwnerId);

		if (grantContract == null) {
			return new InfoGrantContract();
		}

		grantOrientationTeacher = persistentGrantOrientationTeacher
				.readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(), new Integer(
						0));
		if (grantOrientationTeacher == null) {
			throw new FenixServiceException();
		}

		InfoGrantContract infoGrantContract = null;

		infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantContract);
		infoGrantContract
				.setGrantOrientationTeacherInfo(InfoGrantOrientationTeacherWithTeacherAndGrantContract
						.newInfoFromDomain(grantOrientationTeacher));

		return infoGrantContract;
	}
}