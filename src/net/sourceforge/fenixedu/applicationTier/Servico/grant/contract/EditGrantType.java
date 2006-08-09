package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditGrantType extends EditDomainObjectService {

    @Override
    protected DomainObject readObjectByUnique(InfoObject infoObject) throws ExcepcaoPersistencia {
        InfoGrantType infoGrantType = (InfoGrantType) infoObject;
        return GrantType.readBySigla(infoGrantType.getSigla());
    }

    public void run(InfoGrantType infoGrantType) throws Exception {
        super.run(Integer.valueOf(0), infoGrantType);
    }

	@Override
	protected DomainObject createNewDomainObject(InfoObject infoObject) {
		return new GrantType();
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

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readGrantTypeByOID(idInternal);
	}
    
}
