/*
 * Created on 04 Mar 2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.grant.contract.InfoGrantSubsidyWithContract;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
            subsidies = persistentGrantSubsidy
                    .readAllSubsidiesByGrantContractAndState(idContract, state);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (subsidies == null)
            return new ArrayList();

        ArrayList infoSubsidyList = (ArrayList) CollectionUtils.collect(subsidies, new Transformer() {
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