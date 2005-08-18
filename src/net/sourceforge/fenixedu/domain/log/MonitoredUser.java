package net.sourceforge.fenixedu.domain.log;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class MonitoredUser extends MonitoredUser_Base {
    
    public MonitoredUser() {
        super();
    }
    
    public MonitoredUser(String username) {
        setUsername(username);
    }
    
}
