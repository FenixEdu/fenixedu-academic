/*
 * Created on 11/Jun/2004
 *  
 */
package ServidorApresentacao.Action.publico.sms;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.utils.SmsUtil;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.SmsDeliveryType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class UpdateSmsDeliveryReportDispatchAction extends FenixDispatchAction
{

	public void update(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{

		String host = SmsUtil.getInstance().getHost();
		String remoteAddress = request.getRemoteAddr();
		String hostAddress = "";

		//get host ip
		try
		{
			InetAddress address = InetAddress.getByName(host);
			byte[] ipAddress = address.getAddress();

			// Convert to dot representation
			for (int i = 0; i < ipAddress.length; i++)
			{
				if (i > 0)
				{
					hostAddress += ".";
				}
				hostAddress += ipAddress[i] & 0xFF;
			}

		}
		catch (UnknownHostException e)
		{
		}

		//check if the host is accepted
		if (remoteAddress.equals(hostAddress) == false)
			return;

		//check username/password
		String username = getFromRequest("deliveryUsername", request);
		String password = getFromRequest("deliveryPassword", request);
		String deliveryUsername = SmsUtil.getInstance().getDeliveryUsername();
		String deliveryPassword = SmsUtil.getInstance().getDeliveryPassword();

		if ((!username.equals(deliveryUsername)) || (!password.equals(deliveryPassword)))
			return;

		//read smsId and deliveryType
		Integer smsId = new Integer(getFromRequest("smsId", request));
		SmsDeliveryType smsDeliveryType =
			SmsDeliveryType.getEnum(new Integer(getFromRequest("deliveryType", request)).intValue());

		//update sms delivery report
		Object args[] = { smsId, smsDeliveryType };

		try
		{
			ServiceUtils.executeService(null, "UpdateSmsDeliveryReport", args);
		}
		catch (FenixServiceException e1)
		{
		}

	}

	private String getFromRequest(String parameter, HttpServletRequest request)
	{
		String parameterString = request.getParameter(parameter);
		if (parameterString == null)
		{
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

}
