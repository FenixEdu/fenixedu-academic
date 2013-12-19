package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PartyType extends PartyType_Base {

    public PartyType(final PartyTypeEnum partyTypeEnum) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setType(partyTypeEnum);
    }

    @Override
    public void setType(final PartyTypeEnum type) {
        if (type == null) {
            throw new DomainException("error.PartyType.empty.type");
        }
        super.setType(type);
    }

    public static PartyType readPartyTypeByType(final PartyTypeEnum partyTypeEnum) {
        for (final PartyType partyType : Bennu.getInstance().getPartyTypesSet()) {
            if (partyType.getType() == partyTypeEnum) {
                return partyType;
            }
        }
        return null;
    }

    public static Set<Party> getPartiesSet(final PartyTypeEnum partyTypeEnum) {
        final PartyType partyType = readPartyTypeByType(partyTypeEnum);
        return partyType == null ? Collections.EMPTY_SET : partyType.getPartiesSet();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getAllowedChildConnectionRules() {
        return getAllowedChildConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyAllowedChildConnectionRules() {
        return !getAllowedChildConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getAllowedParentConnectionRules() {
        return getAllowedParentConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyAllowedParentConnectionRules() {
        return !getAllowedParentConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getParties() {
        return getPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParties() {
        return !getPartiesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
