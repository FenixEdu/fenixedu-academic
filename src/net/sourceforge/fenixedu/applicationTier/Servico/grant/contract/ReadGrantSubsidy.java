/*
 * Created on Jan 29, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantSubsidy extends ReadDomainObjectService {
    public ReadGrantSubsidy() {
    }

    protected Class getDomainObjectClass() {
        return GrantSubsidy.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantSubsidy();
    }

    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoGrantSubsidyWithContract.newInfoFromDomain((IGrantSubsidy) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#run(java.lang.Integer)
     */
    public InfoObject run(Integer objectId) throws FenixServiceException {
        InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) super.run(objectId);

        //TODO The ReadDomainObjectService only reads 2level depth of
        // references to other objects.
        //In this case we have InfoGrantSubsidy and its reference to
        // InfoGrantContract.
        //Now we need to get the references of InfoGrantContract, e.g.,
        // InfoGrantOwner
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGrantContract pgc = sp.getIPersistentGrantContract();

            InfoGrantContract infoGrantContract = InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain((IGrantContract) pgc.readByOID(GrantContract.class,
                            infoGrantSubsidy.getInfoGrantContract().getIdInternal()));

            //this section of code is temporary!!!! (see above the reason)
            if (infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo() == null)
                infoGrantSubsidy.getInfoGrantContract().setGrantOwnerInfo(new InfoGrantOwner());

            infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo().setIdInternal(
                    infoGrantContract.getGrantOwnerInfo().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return infoGrantSubsidy;
    }
}