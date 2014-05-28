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
package net.sourceforge.fenixedu.util;

import java.sql.Connection;
import java.util.Iterator;
import java.util.ServiceLoader;

public abstract class ConnectionManager {

    private static final ConnectionManager instance;

    static {
        Iterator<ConnectionManager> managers = ServiceLoader.load(ConnectionManager.class).iterator();
        if (!managers.hasNext()) {
            throw new Error("No implementation of ConnectionManager was found in the classpath");
        }
        instance = managers.next();
    }

    public static Connection getCurrentSQLConnection() {
        return instance.getConnection();
    }

    protected abstract Connection getConnection();

}
