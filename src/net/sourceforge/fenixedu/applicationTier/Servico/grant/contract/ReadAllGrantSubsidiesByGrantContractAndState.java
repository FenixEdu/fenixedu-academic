/*
 * Created on 04 Mar 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantSubsidiesByGrantContractAndState implements IService {
    public ReadAllGrantSubsidiesByGrantContractAndState() {
    }

    public List run(Integer idContract, Integer state) throws FenixServiceException {
        List subsidies = null;
        IPersistentGrantSubsidy persistentGrantSubsidy = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
            subsidies = persistentGrantSubsidy
                    .readAllSubsidiesByGrantContractAndState(idContract, state);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (subsidies == null)
            return new ArrayList();

        List infoSubsidyList = (List) CollectionUtils.collect(subsidies, new Transformer() {
            public Object transform(Object input) {
                IGrantSubsidy grantSubsidy = (IGrantSubsidy) input;
                InfoGrantSubsidy infoGrantSubsidy = InfoGrantSubsidyWithContract
                        .newInfoFromDomain(grantSubsidy);
                return infoGrantSubsidy;
            }
        });
        return infoSubsidyList;
    }
}