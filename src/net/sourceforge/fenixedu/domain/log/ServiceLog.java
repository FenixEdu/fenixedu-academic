package net.sourceforge.fenixedu.domain.log;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class ServiceLog extends ServiceLog_Base {

    public ServiceLog() {
        super();
    }

    public ServiceLog(String invokerUsername, String serviceName, String serviceArguments) {
        setInvokerUsername(invokerUsername);
        setServiceName(serviceName);
        setServiceArguments(serviceArguments);
    }

}
