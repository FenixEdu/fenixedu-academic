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
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.util.Cloner;
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
    /**
     * The constructor of this class.
     */
    public ReadAllContractsByGrantOwner() {
    }

    public List run(Integer grantOwnerId) throws FenixServiceException {
        List contracts = null;
        IPersistentGrantOrientationTeacher pgot = null;
        IPersistentGrantContract pgc = null;
        IPersistentGrantContractRegime persistentGrantContractRegime = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgc = sp.getIPersistentGrantContract();
            pgot = sp.getIPersistentGrantOrientationTeacher();
            persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
            contracts = pgc.readAllContractsByGrantOwner(grantOwnerId);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (contracts == null)
            return new ArrayList();

        Iterator contractIter = contracts.iterator();
        ArrayList contractList = new ArrayList();

        //gather information related to each contract
        while (contractIter.hasNext()) {
            try {
                IGrantContract grantContract = (IGrantContract) contractIter
                        .next();
                InfoGrantContract infoGrantContract = Cloner
                        .copyIGrantContract2InfoGrantContract(grantContract);

                //get the GrantOrientationTeacher for each contract
                IGrantOrientationTeacher orientationTeacher = pgot
                        .readActualGrantOrientationTeacherByContract(
                                grantContract, new Integer(0));
                InfoGrantOrientationTeacher infoOrientationTeacher = Cloner
                        .copyIGrantOrientationTeacher2InfoGrantOrientationTeacher(orientationTeacher);
                infoGrantContract
                        .setGrantOrientationTeacherInfo(infoOrientationTeacher);

                /*
                 * Verify if the contract is active or not. The contract is
                 * active if: 
                 * 	 1- The end contract motive is not filled
                 *   2 - The actual grant contract regime is active
                 */
                if (infoGrantContract.getEndContractMotive() != null
                        && !infoGrantContract.getEndContractMotive().equals("")) {
                    infoGrantContract.setActive(new Boolean(false));
                } else {
                    List grantContractRegimeActual = persistentGrantContractRegime.readGrantContractRegimeByGrantContractAndState(infoGrantContract.getIdInternal(), new Integer(1));
                    //It should only have one result!!
                    IGrantContractRegime grantContractRegime = (IGrantContractRegime)grantContractRegimeActual.get(0); 
                    infoGrantContract.setActive(new Boolean(grantContractRegime.getContractRegimeActive()));
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