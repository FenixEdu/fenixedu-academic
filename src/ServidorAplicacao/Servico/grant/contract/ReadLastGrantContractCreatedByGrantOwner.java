/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import DataBeans.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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