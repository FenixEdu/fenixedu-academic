/*
 * Created on Jan 23, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.grant;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Pica
 * @author Barbosa
 */
public interface IPersistentGrantPart extends IPersistentObject {
    public List readAllGrantPartsByGrantSubsidy(Integer grantSubsidyId) throws ExcepcaoPersistencia;

    public IGrantPart readGrantPartByUnique(IGrantSubsidy grantSubsidy, IGrantPaymentEntity paymentEntity)
            throws ExcepcaoPersistencia;
}