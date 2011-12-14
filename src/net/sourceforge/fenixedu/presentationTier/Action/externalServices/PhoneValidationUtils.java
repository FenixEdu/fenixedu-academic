package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;

public class PhoneValidationUtils {

    private String TWILIO_SID;
    private String TWILIO_STOKEN;
    private String TWILIO_FROM_NUMBER;
    private TwilioRestClient TWILIO_CLIENT;
    public String HOST;
    private String CIIST_SMS_USERNAME;
    private String CIIST_SMS_PASSWORD;
    private String CIIST_SMS_GATEWAY_URL;
    private HttpClient CIIST_CLIENT;

    private static PhoneValidationUtils instance;

    public static PhoneValidationUtils getInstance() {
	if (instance == null) {
	    instance = new PhoneValidationUtils();
	}
	return instance;
    }

    private PhoneValidationUtils() {
	TWILIO_SID = PropertiesManager.getProperty("twilio.sid");
	TWILIO_STOKEN = PropertiesManager.getProperty("twilio.stoken");
	TWILIO_FROM_NUMBER = PropertiesManager.getProperty("twilio.from.number");
	System.out.printf("sid : %s stoken: %s from: %s\n", TWILIO_SID, TWILIO_STOKEN, TWILIO_FROM_NUMBER);
	TWILIO_CLIENT = new TwilioRestClient(TWILIO_SID, TWILIO_STOKEN);
	final String appName = PropertiesManager.getProperty("app.name");
	final String appContext = PropertiesManager.getProperty("app.context");
	final String httpPort = PropertiesManager.getProperty("http.port");
	HOST = String.format("http://%s:%s/%s", appName, httpPort, appContext);
	CIIST_SMS_USERNAME = PropertiesManager.getProperty("ciist.sms.username");
	CIIST_SMS_PASSWORD = PropertiesManager.getProperty("ciist.sms.password");
	CIIST_SMS_GATEWAY_URL = PropertiesManager.getProperty("ciist.sms.gateway.url");
	CIIST_CLIENT = new HttpClient();
	Credentials credentials = new UsernamePasswordCredentials(CIIST_SMS_USERNAME, CIIST_SMS_PASSWORD);
	CIIST_CLIENT.getState().setCredentials(AuthScope.ANY, credentials);
    }

    public boolean makeCall(String phoneNumber, String code, String lang) {
	final boolean booleanProperty = PropertiesManager.getBooleanProperty("development.mode");
	if (!booleanProperty) {
	    final Account account = TWILIO_CLIENT.getAccount(); // Make a call
	    CallFactory callFactory = account.getCallFactory();
	    Map<String, String> callParams = new HashMap<String, String>();
	    callParams.put("To", phoneNumber);
	    callParams.put("From", TWILIO_FROM_NUMBER);
	    callParams.put("Url", HOST + "/external/partyContactValidation.do?method=validatePhone&code=" + code + "&lang="
		    + lang);
	    try {
		callFactory.create(callParams);
		return true;
	    } catch (TwilioRestException e) {
		System.out.println("Error makeCall: " + e);
		return false;
	    }
	} else {
	    System.out.println("Call to >" + phoneNumber + "<: Bem-vindo ao sistema Fénix. Introduza o código " + code
		    + " . Obrigado!");
	    return true;
	}
    }

    public boolean sendSMS(String number, String token) {
	final boolean booleanProperty = PropertiesManager.getBooleanProperty("development.mode");
	number = number.replace(" ", "");
	final String message = "Bem-vindo ao sistema Fénix. Introduza o código " + token + " . Obrigado!";
	if (!booleanProperty) {
	    PostMethod method = new PostMethod(CIIST_SMS_GATEWAY_URL);
	    method.addParameter(new NameValuePair("number", number));
	    method.addParameter(new NameValuePair("msg", message));
	    try {
		CIIST_CLIENT.executeMethod(method);
		if (method.getStatusCode() != 200) {
		    return false;
		}
	    } catch (HttpException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	    }
	} else {
	    System.out.println("SMS to >" + number + "<: " + message);
	}
	return true;
    }
}
