/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllContractsByGrantOwner implements IService {
    public ReadAllContractsByGrantOwner() {
    }

    public List run(Integer grantOwnerId) throws FenixServiceException {
        List contracts = null;
        IPersistentGrantOrientationTeacher pgot = null;
        IPersistentGrantContractRegime persistentGrantContractRegime = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantContract pgc = sp.getIPersistentGrantContract();
            pgot = sp.getIPersistentGrantOrientationTeacher();
            persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
            contracts = pgc.readAllContractsByGrantOwner(grantOwnerId);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (contracts == null) {
            return new ArrayList();
        }

        Iterator contractIter = contracts.iterator();
        List contractList = new ArrayList();

        //gather information related to each contract
        while (contractIter.hasNext()) {
            try {
                IGrantContract grantContract = (IGrantContract) contractIter.next();
                InfoGrantContract infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
                        .newInfoFromDomain(grantContract);

                //get the GrantOrientationTeacher for each contract
                IGrantOrientationTeacher orientationTeacher = pgot
                        .readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
                InfoGrantOrientationTeacher infoOrientationTeacher = InfoGrantOrientationTeacherWithTeacherAndGrantContract
                        .newInfoFromDomain(orientationTeacher);
                infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);

                /*
                 * Verify if the contract is active or not. The contract is
                 * active if: 1- The end contract motive is not filled 2 - The
                 * actual grant contract regime is active
                 */
                if (infoGrantContract.getEndContractMotiveSet()) {
                    infoGrantContract.setActive(new Boolean(false));
                } else {
                    List grantContractRegimeActual = persistentGrantContractRegime
                            .readGrantContractRegimeByGrantContractAndState(infoGrantContract
                                    .getIdInternal(), new Integer(1));
                    if (grantContractRegimeActual.isEmpty()) {
                        throw new FenixServiceException(
                                "Grant Contract has no Grant Contract Regime (actual) Associated");
                    }
                    IGrantContractRegime grantContractRegime = (IGrantContractRegime) grantContractRegimeActual
                            .get(0);
                    infoGrantContract.setActive(new Boolean(grantContractRegime
                            .getContractRegimeActive()));
                }
                contractList.add(infoGrantContract);
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException(e.getMessage());
            }
        }
        Collections.reverse(contractList);
        return contractList;
    }
}