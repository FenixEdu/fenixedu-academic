package net.sourceforge.fenixedu.domain.log;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class MonitoredService extends MonitoredService_Base {

    public MonitoredService() {
        super();
    }

    public MonitoredService(String serviceName) {
        setServiceName(serviceName);
    }

}
