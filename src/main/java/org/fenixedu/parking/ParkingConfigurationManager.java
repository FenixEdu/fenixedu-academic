/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of Fenix Parking.
 *
 * Fenix Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fenix Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.parking;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class ParkingConfigurationManager {
    @ConfigurationManager(description = "Parking Configuration")
    public interface ParkingConfigurationProperties {

        @ConfigurationProperty(key = "parkingCardId.admin.password")
        public String getParkingCardIdAdminPassword();

        @ConfigurationProperty(key = "exportParkingData.password")
        public String getExportParkingDataPassword();

        @ConfigurationProperty(key = "exportParkingData.username")
        public String getExportParkingDataUsername();
    }

    public static ParkingConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ParkingConfigurationProperties.class);
    }
}
