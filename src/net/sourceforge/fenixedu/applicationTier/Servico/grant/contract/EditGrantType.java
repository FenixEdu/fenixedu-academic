/*
 * Created on Jan 24, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantType extends EditDomainObjectService {

    public EditGrantType() {
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return InfoGrantType.newDomainFromInfo((InfoGrantType) infoObject);
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantType();
    }

    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantType pgs = sp.getIPersistentGrantType();
        IGrantType grantType = (IGrantType) domainObject;
        return pgs.readGrantTypeBySigla(grantType.getSigla());
    }

    public void run(InfoGrantType infoGrantType) throws FenixServiceException {
        super.run(new Integer(0), infoGrantType);
    }
}