package pt.ist.fenix.user.management;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.bennu.user.management.UsernameGenerator;

@WebListener
public class UsernameGeneratorInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        IstUsernameCounter counter = IstUsernameCounter.ensureSingleton();
        UsernameGenerator.setDefault(new ISTUsernameGenerator(counter));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

}
