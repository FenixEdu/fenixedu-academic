<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.sms.InfoSentSms" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>

			
<%
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");				
%>
			
<h2><bean:message key="title.person.sms.deliveryReports"/></h2>

<html:messages id="msg" message="true">
	<span class="sucessfulOperarion"><bean:write name="msg"/></span><br>
</html:messages>
<span class="error">
	<html:errors/>
</span><br/>

<bean:define id="smsDeliveryReportsList" name="<%= SessionConstants.LIST_SMS_DELIVERY_REPORTS %>" type="java.util.List"/>

<table align="center" width="100%" border="0">
	<tr>
		<td width="25%" align="center"><strong><bean:message key="label.person.smsSendDate" /></strong></td>
		<td width="25%" align="center"><strong><bean:message key="label.person.smsDestinationNumber" /></strong></td>
		<td width="25%" align="center"><strong><bean:message key="label.person.smsDeliveryState" /></strong></td>
		<td width="25%" align="center"><strong><bean:message key="label.person.smsDeliveryDate" /></strong></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<logic:iterate id="smsDeliveryReport" name="smsDeliveryReportsList">
		<tr>		
			<td align="center">
				<bean:define id="sendDate" name="smsDeliveryReport" property="sendDate" type="java.util.Date" />
				<%= simpleDateFormat.format(sendDate) %> 
			</td>
			<td align="center">
				<bean:write name="smsDeliveryReport" property="destinationNumber"/>
			</td>
			<td align="center">			
				<bean:define id="stateName" value="<%= "label.smsDeliveryType." + ((InfoSentSms)smsDeliveryReport).getDeliveryType() %>"/>
				<bean:message name="stateName"/>
			</td>
			<td align="center">			
				<logic:notEmpty name="smsDeliveryReport" property="deliveryDate">
					<bean:define id="deliveryDate" name="smsDeliveryReport" property="deliveryDate" type="java.util.Date" />
					<%= simpleDateFormat.format(deliveryDate) %> 	
				</logic:notEmpty>
			</td>			
		</tr>
	</logic:iterate>

</table>	