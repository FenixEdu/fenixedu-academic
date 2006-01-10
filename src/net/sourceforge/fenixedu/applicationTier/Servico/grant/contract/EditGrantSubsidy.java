package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

public class EditGrantSubsidy extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            DomainObject domainObject) throws ExcepcaoPersistencia {
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) infoObject;
        GrantSubsidy grantSubsidy = (GrantSubsidy) domainObject;
        grantSubsidy.setDateBeginSubsidy(infoGrantSubsidy.getDateBeginSubsidy());
        grantSubsidy.setDateEndSubsidy(infoGrantSubsidy.getDateEndSubsidy());

        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        GrantContract grantContract = (GrantContract) persistentGrantContract.readByOID(
                GrantContract.class, infoGrantSubsidy.getInfoGrantContract().getIdInternal());
        grantSubsidy.setGrantContract(grantContract);

        grantSubsidy.setState(infoGrantSubsidy.getState());
        grantSubsidy.setTotalCost(infoGrantSubsidy.getTotalCost());
        grantSubsidy.setValue(infoGrantSubsidy.getValue());
    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeGrantSubsidy();
    }

    @Override
    protected Class getDomainObjectClass() {
        return GrantSubsidy.class;
    }

    @Override
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantSubsidy();
    }

    @Override
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException, ExcepcaoPersistencia {
        /*
         * In case of a new Subsidy, the Contract associated needs to be set.
         */
        GrantSubsidy grantSubsidy = (GrantSubsidy) domainObjectLocked;
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) infoObject;
        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        GrantContract grantContract = (GrantContract) persistentGrantContract.readByOID(
                GrantContract.class, infoGrantSubsidy.getInfoGrantContract().getIdInternal());
        grantSubsidy.setGrantContract(grantContract);
        domainObjectLocked = grantSubsidy;

        /*
         * If this is a active subsidy, set all others to state 0 (Desactive)
         */
        if (grantSubsidy.getState().equals(InfoGrantSubsidy.getActiveStateValue())) {
            IPersistentGrantSubsidy persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
            List<GrantSubsidy> activeSubsidy = persistentGrantSubsidy
                    .readAllSubsidiesByGrantContractAndState(grantSubsidy.getGrantContract()
                            .getIdInternal(), InfoGrantSubsidy.getActiveStateValue());
            if (activeSubsidy != null && !activeSubsidy.isEmpty()) {
                // Desactivate the Subsidy
                for (GrantSubsidy grantSubsidyTemp : activeSubsidy) {
                    if (!grantSubsidyTemp.equals(grantSubsidy)) {
                        grantSubsidyTemp.setState(InfoGrantSubsidy.getInactiveStateValue());
                    }
                }
            }
        }
    }

    public void run(InfoGrantSubsidy infoGrantSubsidy) throws Exception {
        super.run(new Integer(0), infoGrantSubsidy);
    }

}
