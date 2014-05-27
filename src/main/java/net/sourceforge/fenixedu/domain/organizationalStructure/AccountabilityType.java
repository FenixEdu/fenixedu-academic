/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

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
                typeName == null ? new MultiLanguageString(Locale.getDefault(), name) : typeName.with(
                        Locale.getDefault(), name);

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
