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
package org.fenixedu.academic.domain.accessControl.rules;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public abstract class AccessRule extends AccessRule_Base {
    protected AccessRule() {
        super();
        setRuleSystem(AccessRuleSystem.getInstance());
        setCreated(new DateTime());
        setCreator(Authenticate.getUser());
    }

    @Override
    public DateTime getCreated() {
        // TODO remove when framework supports read-only slots
        return super.getCreated();
    }

    @Override
    public DateTime getRevoked() {
        // TODO remove when framework supports read-only slots
        return super.getRevoked();
    }

    @Override
    public User getCreator() {
        // TODO remove when framework supports read-only slots
        return super.getCreator();
    }

    @Override
    public AccessOperation<?, ?> getOperation() {
        // TODO remove when framework supports read-only slots
        return super.getOperation();
    }

    public <R extends AccessRule> Optional<R> changeOperation(AccessOperation<R, ?> operation) {
        return change(operation, getWhoCanAccess(), getWhatCanAffect());
    }

    public Group getWhoCanAccess() {
        return super.getPersistentGroup().toGroup();
    }

    protected boolean isMember(User user) {
        return getWhoCanAccess().isMember(user);
    }

    protected boolean isMember(User user, DateTime when) {
        return getWhoCanAccess().isMember(user, when);
    }

    public <R extends AccessRule, T extends AccessTarget> Optional<R> changeWhoCanAccess(Group whoCanAccess) {
        return change((AccessOperation<R, T>) getOperation(), whoCanAccess, getWhatCanAffect());
    }

    public abstract <T extends AccessTarget> Set<T> getWhatCanAffect();

    public <R extends AccessRule, T extends AccessTarget> Optional<R> changeWhatCanAffect(Set<T> whatCanAffect) {
        return change((AccessOperation<R, T>) getOperation(), getWhoCanAccess(), whatCanAffect);
    }

    public <T extends AccessRule> Optional<T> grant(User user) {
        return changeWhoCanAccess(getWhoCanAccess().grant(user));
    }

    public <T extends AccessRule> Optional<T> revoke(User user) {
        return changeWhoCanAccess(getWhoCanAccess().revoke(user));
    }

    protected <R extends AccessRule, T extends AccessTarget> Optional<R> change(AccessOperation<R, T> operation,
            Group whoCanAccess, Set<T> whatCanAffect) {
        Objects.requireNonNull(operation);
        Objects.requireNonNull(whoCanAccess);
        Objects.requireNonNull(whatCanAffect);
        revoke();
        if (!whoCanAccess.equals(NobodyGroup.get())) {
            return operation.grant(whoCanAccess, whatCanAffect);
        }
        return Optional.empty();
    }

    public void revoke() {
        setRevoked(new DateTime());
        setDeletedFromRuleSystem(getRuleSystem());
        setRuleSystem(null);
    }
}
