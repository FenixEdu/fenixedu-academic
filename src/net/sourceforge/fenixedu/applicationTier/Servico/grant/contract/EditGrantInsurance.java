/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsuranceWithContractAndPaymentEntity;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantInsurance extends EditDomainObjectService {

    public EditGrantInsurance() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantInsuranceWithContractAndPaymentEntity
                .newDomainFromInfo((InfoGrantInsurance) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantInsurance();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();
        IGrantInsurance grantInsurance = (IGrantInsurance) domainObject;
        return persistentGrantInsurance.readByOID(GrantInsurance.class, grantInsurance.getIdInternal());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doAfterLock(Dominio.IDomainObject,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        /*
         * In case of a new Insurance, the Contract associated needs to be set.
         */
        IGrantInsurance grantInsurance = (IGrantInsurance) domainObjectLocked;
        InfoGrantInsurance infoGrantInsurance = (InfoGrantInsurance) infoObject;

        IGrantContract grantContract = new GrantContract();
        grantContract.setIdInternal(infoGrantInsurance.getInfoGrantContract().getIdInternal());

        grantInsurance.setGrantContract(grantContract);
        domainObjectLocked = grantInsurance;

        /*
         * If the end date is not set, read the end date of the actual contract
         * regime and set the date and the total Value
         */
        try {
            if (grantInsurance.getDateEndInsurance() == null) {
                IPersistentGrantContractRegime persistentGrantContractRegime = sp
                        .getIPersistentGrantContractRegime();
                List grantContractRegimeList = persistentGrantContractRegime
                        .readGrantContractRegimeByGrantContractAndState(infoGrantInsurance
                                .getInfoGrantContract().getIdInternal(), InfoGrantContractRegime
                                .getActiveState());
                IGrantContractRegime grantContractRegime = (IGrantContractRegime) grantContractRegimeList
                        .get(0);
                grantInsurance.setDateEndInsurance(grantContractRegime.getDateEndContract());
            }
            grantInsurance.setTotalValue(InfoGrantInsurance.calculateTotalValue(grantInsurance
                    .getDateBeginInsurance(), grantInsurance.getDateEndInsurance()));
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException();
        }
    }

    public void run(InfoGrantInsurance infoGrantInsurance) throws FenixServiceException {
        super.run(new Integer(0), infoGrantInsurance);
    }
}