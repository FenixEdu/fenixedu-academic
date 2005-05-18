package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantPartVO extends VersionedObjectsBase implements IPersistentGrantPart {

    public List readAllGrantPartsByGrantSubsidy(Integer grantSubsidyId) throws ExcepcaoPersistencia {
        List<IGrantPart> grantParts = (List<IGrantPart>) readAll(GrantPart.class);
        List<IGrantPart> result = null;

        for (IGrantPart part : grantParts) {
            if (part.getKeyGrantSubsidy().equals(grantSubsidyId)) {
                result.add(part);
            }
        }

        return result;
    }

    public IGrantPart readGrantPartByUnique(Integer grantSubsidyId, Integer paymentEntityId)
            throws ExcepcaoPersistencia {
        List<IGrantPart> grantParts = (List<IGrantPart>) readAll(GrantPart.class);

        for (IGrantPart part : grantParts) {
            if (part.getKeyGrantSubsidy().equals(grantSubsidyId)
                    && part.getKeyGrantPaymentEntity().equals(paymentEntityId)) {
                return part;
            }
        }

        return null;
    }

}