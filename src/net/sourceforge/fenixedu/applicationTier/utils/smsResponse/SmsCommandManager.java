package net.sourceforge.fenixedu.applicationTier.utils.smsResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.utils.exceptions.SmsCommandConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsCommandManager {

    private static final String SMS_COMMANDS_FILE = "/sms-commands.xml";

    private List smsCommandList;

    private static SmsCommandManager _instance = null;

    private SmsCommandManager() throws SmsCommandConfigurationException {
        this.smsCommandList = new ArrayList();
        loadCommands();
    }

    private void loadCommands() throws SmsCommandConfigurationException {

        SAXBuilder parser = new SAXBuilder("org.apache.crimson.parser.XMLReaderImpl", true);
        Document document = null;

        try {
            URL smsCommandsFileURL = SmsCommandManager.class.getResource(SMS_COMMANDS_FILE);
            if (smsCommandsFileURL == null) {
                throw new SmsCommandConfigurationException("Cannot find SMS commands file in classpath");
            }

            document = parser.build(smsCommandsFileURL);
            Element smsCommandsElement = document.getRootElement();
            List smsCommandElements = smsCommandsElement.getChildren("sms-command");
            Iterator smsCommandIterator = smsCommandElements.iterator();

            while (smsCommandIterator.hasNext()) {
                Element smsCommandElement = (Element) smsCommandIterator.next();
                String expression = smsCommandElement.getAttributeValue("expression");

                if (expression.length() == 0) {
                    throw new IllegalArgumentException("Element expression cannot be empty");
                }

                //remove new lines, because they don't make much sense in SMS
                expression = expression.replaceAll("\n", "");

                boolean replyToSender = new Boolean(smsCommandElement.getAttributeValue("replyToSender"))
                        .booleanValue();

                String serviceName = smsCommandElement.getAttributeValue("serviceName");

                if (serviceName.length() == 0) {
                    throw new IllegalArgumentException("Element service-name cannot be empty");
                }

                String serviceArgsExpression = smsCommandElement.getAttributeValue("serviceArgs").trim();

                String[] serviceArgs = null;

                if (serviceArgsExpression.length() == 0) {
                    serviceArgs = new String[0];
                } else {
                    serviceArgs = serviceArgsExpression.split(",");
                }

                String authenticationType = smsCommandElement.getAttributeValue("authenticationType");

                SmsCommand smsCommand = new SmsCommand(expression, replyToSender, serviceName,
                        serviceArgs, authenticationType);

                this.smsCommandList.add(smsCommand);
            }
        } catch (JDOMException ex) {
            throw new SmsCommandConfigurationException(
                    "Cannot load SMS commands.Please check sms-commands.xml file syntax", ex);
        } catch (IOException ex) {
            throw new SmsCommandConfigurationException("Error loading SMS commands file", ex);
        }

    }

    /**
     * Singleton method
     * 
     * @return
     */
    public static synchronized SmsCommandManager getInstance() throws SmsCommandConfigurationException {
        if (_instance == null) {
            _instance = new SmsCommandManager();
        }

        return _instance;

    }

    public void handleCommand(String senderMsisdn, String smsText)
            throws SmsCommandConfigurationException, FenixFilterException {
        for (Iterator iter = this.smsCommandList.iterator(); iter.hasNext();) {
            SmsCommand smsCommand = (SmsCommand) iter.next();
            if (smsCommand.handleCommand(senderMsisdn, smsText) == true) {
                // stop chain because command is already handled
                return;
            }
        }
    }

}