/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class AccountabilityType extends AccountabilityType_Base {

    protected AccountabilityType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum, LocalizedString name) {
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
            if (accountabilityType.getType() != null && accountabilityType.getType().equals(typeEnum)) {
                return accountabilityType;
            }
        }
        return null;
    }

    @Override
    public void setCode(String code) {
        findByCode(code).filter(at -> at != this).ifPresent(at -> {
            throw new DomainException("error.accountabilityType.existingCode", code, at.getName());
        });

        super.setCode(code);
    }

    public static Optional<AccountabilityType> findByCode(final String code) {
        return StringUtils.isNotBlank(code) ? Bennu.getInstance().getAccountabilityTypesSet().stream()
                .filter(at -> code.equals(at.getCode())).findAny() : Optional.empty();
    }

    @Override
    public void setTypeName(LocalizedString typeName) {
        if (typeName == null || typeName.isEmpty()) {
            throw new DomainException("error.accountabilityType.empty.typeName");
        }
        super.setTypeName(typeName);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getType() != null || (getTypeName() != null && !getTypeName().isEmpty());
    }

    public boolean isFunction() {
        return false;
    }

    public boolean isSharedFunction() {
        return false;
    }

    public String getName() {
        return getType() != null ? getType().getLocalizedName() : super.getTypeName().getContent();
    }

//    public String getName() {
//        return LocaleUtils.getPreferedContent(getTypeName());
//    }
//
//    public void setName(String name) {
//
//        if (name == null || StringUtils.isEmpty(name.trim())) {
//            throw new DomainException("error.accountabilityType.empty.name");
//        }
//
//        LocalizedString typeName = getTypeName();
//        typeName = typeName == null ? new LocalizedString(Locale.getDefault(), name) : typeName.with(Locale.getDefault(), name);
//
//        setTypeName(typeName);
//    }

    public boolean hasConnectionRuleFor(PartyType parentType, PartyType childType) {
        return getConnectionRuleFor(parentType, childType) != null;
    }

    public ConnectionRule getConnectionRuleFor(PartyType parentType, PartyType childType) {
        for (final ConnectionRule connectionRule : getConnectionRulesSet()) {
            if (connectionRule.isValid(parentType, childType)) {
                return connectionRule;
            }
        }

        return null;
    }

    public ConnectionRule addConnectionRule(PartyType parentType, PartyType childType, Boolean managedByUser) {
        final ConnectionRule result = new ConnectionRule(parentType, childType, this);
        result.setManagedByUser(managedByUser);
        return result;
    }

    public boolean canConnect(PartyType parentType, PartyType childType) {

        for (final ConnectionRule connectionRule : getConnectionRulesSet()) {
            if (connectionRule.isValid(parentType, childType)) {
                return true;
            }
        }

        return false;
    }

    public void delete() {
        if (!getAccountabilitiesSet().isEmpty()) {
            throw new DomainException("error.AccountabilityType.cannotDelete.hasAssociatedAccountabilities");
        }

        getConnectionRulesSet().forEach(cr -> cr.delete());

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
