/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import Dominio.grant.contract.IGrantContractRegime;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContractRegime;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantContractRegimeByContractAndState implements IService {
    /**
     * The constructor of this class.
     */
    public ReadGrantContractRegimeByContractAndState() {
    }

    public List run(Integer grantContractId, Integer state) throws FenixServiceException {
        List contractRegimes = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantContractRegime persistentGrantContractRegime = sp
                    .getIPersistentGrantContractRegime();
            contractRegimes = persistentGrantContractRegime
                    .readGrantContractRegimeByGrantContractAndState(grantContractId, state);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (contractRegimes == null)
            return new ArrayList();

        ArrayList infoContractRegimeList = (ArrayList) CollectionUtils.collect(contractRegimes,
                new Transformer() {
                    public Object transform(Object input) {
                        IGrantContractRegime grantContractRegime = (IGrantContractRegime) input;
                        InfoGrantContractRegime infoGrantContractRegime = InfoGrantContractRegimeWithTeacherAndContract
                                .newInfoFromDomain(grantContractRegime);
                        return infoGrantContractRegime;
                    }
                });

        return infoContractRegimeList;

    }
}