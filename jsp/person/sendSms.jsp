<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.person.sendSms"/></h2>

<html:messages id="msg" message="true">
	<span class="sucessfulOperarion"><bean:write name="msg"/></span><br>
</html:messages>
<span class="error">
	<html:errors/>
</span><br/>
	
<html:form action="/sendSms.do">  
	<html:hidden property="page" value="1" />	
	<html:hidden property="method" value="send" />		

	<table>
		<tr>
			<td>
				<bean:message key="label.person.destinationPhoneNumber"/>:
			</td>
			<td>
				<html:text property="destinationPhoneNumber" size="20" maxlength="9"/>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="label.person.message"/>:
			</td>
			<td>
				<html:textarea property="message" rows="4" cols="30" 
					onkeyup="document.sendSmsForm.charCount.value=160-document.sendSmsForm.message.value.length;" 
					/>
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message key="label.person.remainingChars"/>:
			</td>
			<td>
				<html:text property="charCount" size="4" maxlength="3" readonly="true" value="160" />
			</td>
		</tr>
	</table>

	<p />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.person.send"/>
	</html:submit>

	<p />
	<html:link page="/viewSmsDeliveryReports.do">		
	   	<bean:message key="link.person.sms.viewDeliveryReports"/>
	</html:link>

</html:form>