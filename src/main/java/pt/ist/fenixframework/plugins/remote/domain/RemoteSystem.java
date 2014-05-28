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
package pt.ist.fenixframework.plugins.remote.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoteSystem extends RemoteSystem_Base {

    private RemoteSystem() {
        super();
        RemoteSystem root = FenixFramework.getDomainRoot().getRemoteSystem();
        if (root != null && root != this) {
            throw new Error("Trying to create a 2nd instance of RemoteSystemRoot! There can only be one!");
        }
    }

    @Atomic
    public static void init() {
        if (FenixFramework.getDomainRoot().getRemoteSystem() == null) {
            FenixFramework.getDomainRoot().setRemoteSystem(new RemoteSystem());
        }
    }

    public static RemoteSystem getInstance() {
        return FenixFramework.getDomainRoot().getRemoteSystem();
    }

    @Deprecated
    public java.util.Set<pt.ist.fenixframework.plugins.remote.domain.RemoteHost> getRemoteHosts() {
        return getRemoteHostsSet();
    }

    @Deprecated
    public boolean hasAnyRemoteHosts() {
        return !getRemoteHostsSet().isEmpty();
    }

}
