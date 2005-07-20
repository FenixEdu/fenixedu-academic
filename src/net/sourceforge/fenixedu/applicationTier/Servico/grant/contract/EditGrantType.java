/*
 * Created on Jan 24, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
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

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantType();
    }

    protected IDomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantType pgs = sp.getIPersistentGrantType();
        InfoGrantType infoGrantType = (InfoGrantType) infoObject;
        return pgs.readGrantTypeBySigla(infoGrantType.getSigla());
    }

    public void run(InfoGrantType infoGrantType) throws FenixServiceException {
        super.run(new Integer(0), infoGrantType);
    }

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		return new GrantType();
	}

	@Override
	protected Class getDomainObjectClass() {
		return GrantType.class;
	}

	@Override
	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) {
		InfoGrantType infoGrantType = (InfoGrantType) infoObject;
		IGrantType grantType = (IGrantType) domainObject;

		grantType.setIndicativeValue(infoGrantType.getIndicativeValue());
		grantType.setMaxPeriodDays(infoGrantType.getMaxPeriodDays());
		grantType.setMinPeriodDays(infoGrantType.getMinPeriodDays());
		grantType.setName(infoGrantType.getName());
		grantType.setSigla(infoGrantType.getSigla());
		grantType.setSource(infoGrantType.getSource());
		grantType.setState(infoGrantType.getState());
	}
}