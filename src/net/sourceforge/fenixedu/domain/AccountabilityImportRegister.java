package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;

public class AccountabilityImportRegister extends AccountabilityImportRegister_Base {

	public AccountabilityImportRegister(final Accountability accountability, final String remoteExternalOid) {
		super();
		setAccountability(accountability);
		setRemoteExternalOid(remoteExternalOid);
	}

}
