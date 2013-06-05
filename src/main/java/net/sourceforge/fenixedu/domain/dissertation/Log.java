package net.sourceforge.fenixedu.domain.dissertation;

import net.sourceforge.fenixedu.domain.User;

public class Log extends Log_Base {
    
    public  Log() {
        super();
    }
    private User user = null;
    
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
