package net.sourceforge.fenixedu.domain.dissertation;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import net.sourceforge.fenixedu.domain.User;

public class Comment extends Comment_Base {
    
    public  Comment() {
        super();
    }
    
    private DateTime created = null;
    private MultiLanguageString requirements = null;
    private User user = null;
    
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public MultiLanguageString getRequirements() {
		return requirements;
	}
	public void setRequirements(MultiLanguageString requirements) {
		this.requirements = requirements;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
    
}
