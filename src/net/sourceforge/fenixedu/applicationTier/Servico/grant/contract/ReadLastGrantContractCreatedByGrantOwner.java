/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadLastGrantContractCreatedByGrantOwner implements IService {

    public ReadLastGrantContractCreatedByGrantOwner() {
    }

    public InfoGrantContract run(Integer grantOwnerId) throws FenixServiceException {
        Integer grantContractNumber = null;
        IGrantContract grantContract = null;
        IGrantOrientationTeacher grantOrientationTeacher = null;
        IPersistentGrantContract persistentGrantContract = null;
        IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = null;
        try {
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
                    .readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
            if (grantOrientationTeacher == null) {
                throw new FenixServiceException();
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        InfoGrantContract infoGrantContract = null;
        try {
            infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantContract);
            infoGrantContract
                    .setGrantOrientationTeacherInfo(InfoGrantOrientationTeacherWithTeacherAndGrantContract
                            .newInfoFromDomain(grantOrientationTeacher));
        } catch (Exception e) {
            throw new FenixServiceException(e.getMessage());
        }
        return infoGrantContract;
    }
}