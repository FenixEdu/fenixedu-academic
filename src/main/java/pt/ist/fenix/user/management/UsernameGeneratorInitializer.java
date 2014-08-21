package pt.ist.fenix.user.management;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.bennu.core.domain.User;

@WebListener
public class UsernameGeneratorInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        IstUsernameCounter counter = IstUsernameCounter.ensureSingleton();
        User.setUsernameGenerator(new ISTUsernameGenerator(counter));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

}
