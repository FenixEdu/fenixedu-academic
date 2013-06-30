package net.sourceforge.fenixedu.domain;

public class AppUserSession extends AppUserSession_Base {

    public AppUserSession() {
        super();
    }

    public void addExternalApplication(ExternalApplication app) {
        this.getApplication().add(app);
    }

    @Override
    public void addUser(User user) {
        this.getUser().add(user);
    }

}
