package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.Bennu;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AccountabilityType extends AccountabilityType_Base {

    protected AccountabilityType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum, MultiLanguageString name) {
        this();
        setType(accountabilityTypeEnum);
        setTypeName(name);
    }

    @Override
    public void setType(AccountabilityTypeEnum type) {
        if (type == null) {
            throw new DomainException("error.accountabilityType.empty.type");
        }
        super.setType(type);
    }

    public static AccountabilityType readByType(AccountabilityTypeEnum typeEnum) {
        Collection<AccountabilityType> allAccountabilityTypes = Bennu.getInstance().getAccountabilityTypesSet();
        for (AccountabilityType accountabilityType : allAccountabilityTypes) {
            if (accountabilityType.getType().equals(typeEnum)) {
                return accountabilityType;
            }
        }
        return null;
    }

    @Override
    public void setTypeName(MultiLanguageString typeName) {
        if (typeName == null || typeName.isEmpty()) {
            throw new DomainException("error.accountabilityType.empty.typeName");
        }
        super.setTypeName(typeName);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getType() != null && getTypeName() != null && !getTypeName().isEmpty();
    }

    public boolean isFunction() {
        return false;
    }

    public boolean isSharedFunction() {
        return false;
    }

    public String getName() {
        return getTypeName().getPreferedContent();
    }

    public void setName(String name) {

        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.accountabilityType.empty.name");
        }

        MultiLanguageString typeName = getTypeName();
        typeName =
                typeName == null ? new MultiLanguageString(Language.getDefaultLanguage(), name) : typeName.with(
                        Language.getDefaultLanguage(), name);

        setTypeName(typeName);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Accountability> getAccountabilities() {
        return getAccountabilitiesSet();
    }

    @Deprecated
    public boolean hasAnyAccountabilities() {
        return !getAccountabilitiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule> getConnectionRules() {
        return getConnectionRulesSet();
    }

    @Deprecated
    public boolean hasAnyConnectionRules() {
        return !getConnectionRulesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasTypeName() {
        return getTypeName() != null;
    }

}
