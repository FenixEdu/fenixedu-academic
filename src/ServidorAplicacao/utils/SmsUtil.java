/*
 * Created on 2/Jun/2004
 *  
 */
package ServidorAplicacao.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;

import ServidorAplicacao.utils.exceptions.FenixUtilException;
import ServidorAplicacao.utils.exceptions.SmsSendUtilException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class SmsUtil
{
	private static SmsUtil _instance = null;

	private String host;
	private int port;
	private String uri;
	private String protocol;
	private String username;
	private String password;
	private int monthlySmsLimit;
	private String deliveryUsername;
	private String deliveryPassword;
	private String deliveryHost;
	private int deliveryPort;
	private String deliveryUri;
	private String deliveryProtocol;

	public static synchronized SmsUtil getInstance()
	{
		if (_instance == null)
		{
			_instance = new SmsUtil();
		}

		return _instance;
	}

	/**
	 * Default constructor (reads "/SMSCofiguration.properties")
	 */
	private SmsUtil()
	{

		Properties properties = new Properties();

		InputStream inputStream = getClass().getResourceAsStream("/SMSConfiguration.properties");
		try
		{
			properties.load(inputStream);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		this.host = properties.getProperty("host");
		this.uri = properties.getProperty("host.uri");
		this.port = (new Integer(properties.getProperty("host.port"))).intValue();
		this.protocol = properties.getProperty("protocol");
		this.username = properties.getProperty("username").trim();
		this.password = properties.getProperty("password").trim();
		this.monthlySmsLimit = (new Integer(properties.getProperty("monthlySmsLimit"))).intValue();
		this.deliveryUsername = properties.getProperty("deliveryUsername").trim();
		this.deliveryPassword = properties.getProperty("deliveryPassword").trim();
		this.deliveryHost = properties.getProperty("deliveryHost");
		this.deliveryPort = (new Integer(properties.getProperty("deliveryHost.port"))).intValue();
		this.deliveryUri = properties.getProperty("deliveryHost.uri");
		this.deliveryProtocol = properties.getProperty("deliveryProtocol");

	}

	/**
	 * 
	 * Send a Sms with delivery report support (The gateway will update da SentSms with Id=smsId)
	 * 
	 * @param destinationPhoneNumber
	 * @param message
	 * @param smsId
	 * @throws FenixUtilException
	 */
	public void sendSms(Integer destinationPhoneNumber, String message, Integer smsId)
		throws FenixUtilException
	{

		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(this.host, this.port, this.protocol);
		HttpMethod httpMethod = new GetMethod(this.uri);

		NameValuePair deliveryUsername = new NameValuePair("deliveryUsername", this.deliveryUsername);
		NameValuePair deliveryPassword = new NameValuePair("deliveryPassword", this.deliveryPassword);
		NameValuePair deliveryMethod = new NameValuePair("method", "updateDeliveryReport");
		NameValuePair deliverySmsId = new NameValuePair("smsId", smsId.toString());

		String deliveryQuery =
			EncodingUtil.formUrlEncode(
				new NameValuePair[] {
					deliveryUsername,
					deliveryPassword,
					deliverySmsId,
					deliveryMethod },
				"8859_1")
				+ "&deliveryType=%d";

		URL url = null;
		try
		{
			url = new URL(this.deliveryProtocol, this.deliveryHost, this.deliveryPort, this.deliveryUri);
		}
		catch (MalformedURLException e2)
		{
			throw new SmsSendUtilException();
		}

		String deliveryUrl = url.toExternalForm() + "?" + deliveryQuery;

		NameValuePair username = new NameValuePair("username", this.username);
		NameValuePair password = new NameValuePair("password", this.password);
		NameValuePair to = new NameValuePair("to", destinationPhoneNumber.toString());
		NameValuePair text = new NameValuePair("text", message);
		NameValuePair dlrmaks = new NameValuePair("dlrmask", "31");
		NameValuePair dlrurl = new NameValuePair("dlrurl", deliveryUrl);

		String query =
			EncodingUtil.formUrlEncode(
				new NameValuePair[] { username, password, to, text, dlrmaks, dlrurl },
				"8859_1");

		httpMethod.setQueryString(query);

		try
		{
			client.executeMethod(httpMethod);

			String result = httpMethod.getResponseBodyAsString();
			System.err.println(result);

			httpMethod.releaseConnection();

			if (result.equals("Sent.") == false)
			{
				throw new SmsSendUtilException();
			}

		}
		catch (HttpException e)
		{
			throw new SmsSendUtilException();
		}
		catch (IOException e)
		{
			throw new SmsSendUtilException();
		}

	}

	/**
	 * 
	 * Send a Sms without delivery report support
	 * 
	 * @param destinationPhoneNumber
	 * @param message
	 * @param smsId
	 * @throws FenixUtilException
	 */
	public void sendSmsWithoutDeliveryReports(Integer destinationPhoneNumber, String message)
		throws FenixUtilException
	{

		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(this.host, this.port, this.protocol);
		HttpMethod httpMethod = new GetMethod(this.uri);

		NameValuePair username = new NameValuePair("username", this.username);
		NameValuePair password = new NameValuePair("password", this.password);
		NameValuePair to = new NameValuePair("to", destinationPhoneNumber.toString());
		NameValuePair text = new NameValuePair("text", message);

		String query =
			EncodingUtil.formUrlEncode(new NameValuePair[] { username, password, to, text }, "8859_1");

		httpMethod.setQueryString(query);

		try
		{
			 client.executeMethod(httpMethod);

			String result = httpMethod.getResponseBodyAsString();
			System.err.println(result);

			httpMethod.releaseConnection();

			if (result.equals("Sent.") == false)
			{
				throw new SmsSendUtilException();
			}

		}
		catch (HttpException e)
		{
			throw new SmsSendUtilException();
		}
		catch (IOException e)
		{
			throw new SmsSendUtilException();
		}

	}
	
	/**
	 * 
	 * @param responseMessage
	 * @param responseLenght
	 * @return
	 */
	public List splitMessage(StringBuffer message, int messageLenght)
	{		
		List responseMessagesList = new ArrayList();
		while(message.length() > messageLenght){
			
			responseMessagesList.add(message.substring(0, messageLenght));
			message.delete(0, messageLenght);
			
		}
		responseMessagesList.add(message.toString());
		
		return responseMessagesList;
	}

	/**
	 * @return Returns the monthlySmsLimit.
	 */
	public int getMonthlySmsLimit()
	{
		return monthlySmsLimit;
	}

	/**
	 * @return Returns the host.
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * @return Returns the deliveryPassword.
	 */
	public String getDeliveryPassword()
	{
		return deliveryPassword;
	}

	/**
	 * @return Returns the deliveryUsername.
	 */
	public String getDeliveryUsername()
	{
		return deliveryUsername;
	}

}
