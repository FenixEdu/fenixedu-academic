package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantType extends GrantType_Base {

	public GrantType() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public static GrantType readBySigla(String sigla) {
        for (GrantType grantType : RootDomainObject.getInstance().getGrantTypes()) {
            if (grantType.getSigla().equals(sigla)) {
                return grantType;
            }
        }
        return null;
    }
    
}
