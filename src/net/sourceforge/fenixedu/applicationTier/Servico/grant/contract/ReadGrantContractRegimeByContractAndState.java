/*
 * Created on 18/12/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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

        List infoContractRegimeList = (ArrayList) CollectionUtils.collect(contractRegimes,
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