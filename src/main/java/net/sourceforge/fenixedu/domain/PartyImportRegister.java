package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class PartyImportRegister extends PartyImportRegister_Base {

    public PartyImportRegister(final Party party, final String remoteExternalOid) {
        super();
        setParty(party);
        setRemoteExternalOid(remoteExternalOid);
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

}
