/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms.messaging.email;


import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:17:42,8/Fev/2006
 * @version $Id$
 */
public class EMailAddress
{
	private String user;

	private String domain;

	private String personalName;

	public EMailAddress()
	{

	}

	public EMailAddress(String user, String domain, String personalName)
	{
		this(user, domain);
		this.personalName = personalName;
	}

	public EMailAddress(String user, String domain)
	{
		this.user = user;
		this.domain = domain;
	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public boolean isValid()
	{
		return true;
	}

	public static boolean isValid(String username, String domain)
	{
		boolean result = false;
		try
		{
			InternetAddress address = new InternetAddress(new StringBuffer(username).append("@").append(domain).toString());
			result = true;
		}
		catch (AddressException e)
		{
			// thats fine
		}
		return result;
	}

	public static boolean isValid(String address)
	{
		boolean result = false;
		if (address != null)
		{
			String[] components = address.split("@");
			if (components.length == 2) result = EMailAddress.isValid(components[0], components[1]);
		}
		return result;
	}

	public String getPersonalName()
	{
		return personalName;
	}

	public void setPersonalName(String personalName)
	{
		this.personalName = personalName;
	}

	public InternetAddress getInternetAddress() throws UnsupportedEncodingException, AddressException
	{
		InternetAddress address = new InternetAddress(new StringBuffer(this.user).append("@").append(this.domain).toString());
		if (this.personalName != null && !this.personalName.equals(""))
		{
			address.setPersonal(this.personalName);
		}

		return address;
	}
}
