/*
 * Created on Jan 24, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;

/**
 * @author pica
 * @author barbosa
 */
public class EditGrantType extends EditDomainObjectService {

    @Override
    protected IPersistentObject getIPersistentObject() {
        return persistentSupport.getIPersistentGrantType();
    }

    @Override
    protected DomainObject readObjectByUnique(InfoObject infoObject) throws ExcepcaoPersistencia {
        IPersistentGrantType pgs = persistentSupport.getIPersistentGrantType();
        InfoGrantType infoGrantType = (InfoGrantType) infoObject;
        return pgs.readGrantTypeBySigla(infoGrantType.getSigla());
    }

    public void run(InfoGrantType infoGrantType) throws Exception {
        super.run(new Integer(0), infoGrantType);
    }

	@Override
	protected DomainObject createNewDomainObject(InfoObject infoObject) {
		return DomainFactory.makeGrantType();
	}

	@Override
	protected Class getDomainObjectClass() {
		return GrantType.class;
	}

	@Override
	protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject) {
		InfoGrantType infoGrantType = (InfoGrantType) infoObject;
		GrantType grantType = (GrantType) domainObject;

		grantType.setIndicativeValue(infoGrantType.getIndicativeValue());
		grantType.setMaxPeriodDays(infoGrantType.getMaxPeriodDays());
		grantType.setMinPeriodDays(infoGrantType.getMinPeriodDays());
		grantType.setName(infoGrantType.getName());
		grantType.setSigla(infoGrantType.getSigla());
		grantType.setSource(infoGrantType.getSource());
		grantType.setState(infoGrantType.getState());
	}
}