package org.fenixedu.academic.domain.treasury;

public class TreasuryBridgeAPIFactory {

    private static ITreasuryBridgeAPI _impl;
    
    public static synchronized ITreasuryBridgeAPI implementation() {
        return _impl;
    }
    
    public static synchronized void registerImplementation(ITreasuryBridgeAPI impl) {
        _impl = impl;
    }
}