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
public class ReadAllGrantSubsidiesByGrantContract implements IService {
    /**
     * The constructor of this class.
     */
    public ReadAllGrantSubsidiesByGrantContract() {
    }

    public List run(Integer idContract) throws FenixServiceException {
        List subsidies = null;
        IPersistentGrantSubsidy pgs = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgs = sp.getIPersistentGrantSubsidy();
            subsidies = pgs.readAllSubsidiesByGrantContract(idContract);
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