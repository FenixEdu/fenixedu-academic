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
