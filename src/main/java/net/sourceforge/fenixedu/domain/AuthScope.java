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
package net.sourceforge.fenixedu.domain;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Joiner;

public class AuthScope extends AuthScope_Base {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthScope.class);

    public AuthScope() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static AuthScope getAuthScope(String name) {
        for (AuthScope scope : Bennu.getInstance().getAuthScopesSet()) {
            if (scope.getName().equals(name)) {
                return scope;
            }
        }
        return null;
    }

    public void setJerseyEndpoints(Collection<String> endpoints) {
        setEndpoints(Joiner.on("|").join(endpoints));
    }

    public Collection<String> getJerseyEndpoints() {
        return Arrays.asList(getEndpoints().split("\\|"));
    }

    public void changeJerseyEndpoints(Collection<String> endpoints) {
        Collection<String> currentEndpoints = getJerseyEndpoints();
        Collection<String> removedEndpoints = CollectionUtils.subtract(currentEndpoints, endpoints);
        Collection<String> newEndpoints = CollectionUtils.subtract(endpoints, currentEndpoints);
        if (!removedEndpoints.isEmpty() || !newEndpoints.isEmpty()) {
            notifyAllAppsScopeHasChanged();
        }
        setJerseyEndpoints(endpoints);
    }

    private void notifyAllAppsScopeHasChanged() {
        for (ExternalApplication app : getAppSet()) {
            LOGGER.info("scopes of app {} changed", getName());
            app.deleteAuthorizations();
        }
    }

    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "oauthapps.label.app.scope." + getName());
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        getAppSet().clear();
        setRootDomainObject(null);
        deleteDomainObject();
    }
}
