/*
 * Created on 23/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantPartsByGrantSubsidy implements IService {

    public ReadAllGrantPartsByGrantSubsidy() {
    }

    public List run(Integer grantSubsidyId) throws FenixServiceException {
        List result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantPart pgp = sp.getIPersistentGrantPart();
            List grantParts = pgp.readAllGrantPartsByGrantSubsidy(grantSubsidyId);

            if (grantParts != null) {
                result = (List) CollectionUtils.collect(grantParts, new Transformer() {
                    public Object transform(Object o) {
                        IGrantPart grantPart = (IGrantPart) o;
                        return InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                                .newInfoFromDomain(grantPart);
                    }
                });
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return result;
    }
}