/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST Integration.
 *
 * FenixEdu IST Integration is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST Integration is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST Integration.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.candidacy;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FirstTimeDocumentsConfiguration extends FirstTimeDocumentsConfiguration_Base {

    private FirstTimeDocumentsConfiguration() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setConfigurationProperties(new JsonObject());
    }

    public static synchronized FirstTimeDocumentsConfiguration getInstance() {
        if (Bennu.getInstance().getFirstTimeDocumentsConfiguration() == null) {
            initialize();
        }
        return Bennu.getInstance().getFirstTimeDocumentsConfiguration();
    }

    @Atomic(mode = TxMode.WRITE)
    private static void initialize() {
        if (Bennu.getInstance().getFirstTimeDocumentsConfiguration() == null) {
            new FirstTimeDocumentsConfiguration();
        }
    }

    public boolean isToExclude(String property) {
        if (getConfigurationProperties().has(property)) {
            return getConfigurationProperties().get(property).getAsBoolean();
        }
        return false;
    }

    @Override
    public void setConfigurationProperties(JsonElement configurationProperties) {
        if (!configurationProperties.isJsonObject()) {
            throw new IllegalArgumentException("Configuration must be a json object");
        }
        super.setConfigurationProperties(configurationProperties);
    }

    @Override
    public JsonObject getConfigurationProperties() {
        return clone(super.getConfigurationProperties().getAsJsonObject());
    }

    private JsonObject clone(JsonObject json) {
        return new JsonParser().parse(json.toString()).getAsJsonObject();
    }
}
