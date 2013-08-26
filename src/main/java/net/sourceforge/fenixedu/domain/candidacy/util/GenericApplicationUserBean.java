package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import pt.ist.fenixWebFramework.services.Service;

public class GenericApplicationUserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GenericApplicationPeriod genericApplicationPeriod;
    private String username;

    public GenericApplicationUserBean(final GenericApplicationPeriod genericApplicationPeriod) {
        this.genericApplicationPeriod = genericApplicationPeriod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Service
    public void addManagerUser() {
        if (genericApplicationPeriod.isCurrentUserAllowedToMange()) {
            final User user = User.readUserByUserUId(username);
            if (user != null) {
                genericApplicationPeriod.addManager(user);
            }
        }
    }

}
