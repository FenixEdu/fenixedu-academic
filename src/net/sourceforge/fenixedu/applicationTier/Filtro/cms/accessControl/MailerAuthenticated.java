/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.cms.accessControl;

import java.util.Properties;

import net.sourceforge.fenixedu._development.PropertiesManager;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.FilterParameters;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 22:19:11,16/Nov/2005
 * @version $Id$
 */
public class MailerAuthenticated extends CmsAccessControlFilter {

    private final static String propertiesFilename = "/cms.properties";

    private static String username;

    private static String password;

    public void execute(ServiceRequest request, ServiceResponse response,
            FilterParameters filterParameters) throws FilterException, Exception {
        if (username == null || password == null) {
            try {
                final Properties properties = PropertiesManager.loadProperties(propertiesFilename);
                username = properties.getProperty("cms.mailer.username");
                password = properties.getProperty("cms.mailer.password");
                if (username == null || password == null) {
                    throw new MailerNotAuthenticated("mailer username and/or password missing from resources bundle");
                }
            } catch (Exception e) {
                throw new MailerNotAuthenticated("exception while retrieving mailer username and password");
            }
        }

        final String username = (String) request.getServiceParameters().getParameter(3);
        final String password = (String) request.getServiceParameters().getParameter(4);

        if (!MailerAuthenticated.username.equals(username) || !MailerAuthenticated.password.equals(password)) {
            throw new MailerNotAuthenticated("wrong mailer username and password pair given");
        }
    }

}
