/*
 * Created on Jan 24, 2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

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
               
                
                IGrantCostCenter grantCostCenter = new GrantCostCenter();
                grantContract.setIdInternal(infoGrantContractRegime.getInfoGrantContract()
                        .getIdInternal());
               
                
                grantCostCenter.setIdInternal(infoGrantContractRegime.getGrantCostCenterInfo().getIdInternal());
                grantContractRegime.setGrantCostCenter(grantCostCenter);

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

