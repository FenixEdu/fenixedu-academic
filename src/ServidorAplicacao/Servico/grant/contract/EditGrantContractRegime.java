/*
 * Created on Jan 24, 2004
 */

package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import DataBeans.InfoObject;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import DataBeans.grant.contract.InfoGrantInsurance;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.GrantContractRegime;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantInsurance;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantInsurance;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantContractRegime extends EditDomainObjectService {

    public EditGrantContractRegime() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantContractRegimeWithTeacherAndContract
                .newDomainFromInfo((InfoGrantContractRegime) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractRegime();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime persistentGrantContractRegime = sp
                .getIPersistentGrantContractRegime();
        IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObject;
        return persistentGrantContractRegime.readByOID(GrantContractRegime.class, grantContractRegime
                .getIdInternal());
    }

    public void run(InfoGrantContractRegime infoGrantContractRegime) throws FenixServiceException {
        super.run(new Integer(0), infoGrantContractRegime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      DataBeans.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        if (infoGrantContractRegime.getState().equals(InfoGrantContractRegime.getActiveState())) {
            //Active Contract Regime

            IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObjectLocked;
            //Set the correct grant orientation teacher
            try {
                IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp
                        .getIPersistentGrantOrientationTeacher();
                IGrantContract grantContract = new GrantContract();
                grantContract.setIdInternal(infoGrantContractRegime.getInfoGrantContract()
                        .getIdInternal());
                IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher
                        .readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
                persistentGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
                //If grantOrientationTeacher is filled in grantContractRegime
                if (infoGrantContractRegime.getInfoTeacher() != null) {
                    //Update grant orientation teacher of contract
                    grantOrientationTeacher.setOrientationTeacher(InfoTeacherWithPerson
                            .newDomainFromInfo(infoGrantContractRegime.getInfoTeacher()));
                } else
                //if grantOrientationTeacher is not filled in
                // grantContractRegime
                {
                    //Copy old values from grant orientation teacher do grant
                    // contract regime
                    grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());
                }
                grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
                grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());
            } catch (ExcepcaoPersistencia persistentException) {
                throw new FenixServiceException(persistentException.getMessage());
            }

            //Set all the others GrantContractRegime that are active to state
            // inactive
            try {
                IPersistentGrantContractRegime persistentGrantContractRegime = sp
                        .getIPersistentGrantContractRegime();
                List activeContractRegime = persistentGrantContractRegime
                        .readGrantContractRegimeByGrantContractAndState(grantContractRegime
                                .getGrantContract().getIdInternal(), new Integer(1));
                if (activeContractRegime != null && !activeContractRegime.isEmpty()) {
                    //Desactivate the contracts
                    for (int i = 0; i < activeContractRegime.size(); i++) {
                        IGrantContractRegime grantContractRegimeTemp = (IGrantContractRegime) activeContractRegime
                                .get(i);
                        if (!grantContractRegimeTemp.equals(grantContractRegime)) {
                            persistentGrantContractRegime.simpleLockWrite(grantContractRegimeTemp);
                            grantContractRegimeTemp.setState(InfoGrantContractRegime.getInactiveState());
                        }
                    }
                }
            } catch (ExcepcaoPersistencia persistentException) {
                throw new FenixServiceException(persistentException.getMessage());
            }

            try {
                //Change the data from the insurance
                IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
                IGrantInsurance grantInsurance = persistentGrantInsurance
                        .readGrantInsuranceByGrantContract(grantContractRegime.getGrantContract()
                                .getIdInternal());
                if (grantInsurance != null) {
                    persistentGrantInsurance.simpleLockWrite(grantInsurance);
                    grantInsurance.setDateEndInsurance(infoGrantContractRegime.getDateEndContract());
                    grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                            .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
                }
            } catch (ExcepcaoPersistencia e) {
                throw new FenixServiceException();
            }

        }
    }

}

