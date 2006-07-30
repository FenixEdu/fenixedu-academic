<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.person.sendSms"/></h2>

<span class="error"><!-- Error messages go here -->
	Após uma fase de testes com sucesso, o serviço de envio de SMS encontra-se indisponível até ser celebrado um acordo com as operadoras.
</span><br/>

<%--

<html:messages id="msg" message="true">
	<span class="sucessfulOperarion"><bean:write name="msg"/></span><br/>
</html:messages>

<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>

<html:form action="/sendSms.do">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="send" />		

	<table>
		<tr>
			<td>
				<bean:message key="label.person.destinationPhoneNumber"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.destinationPhoneNumber" property="destinationPhoneNumber" size="20" maxlength="9"/>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="label.person.message"/>:
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.message" property="message" rows="4" cols="30" 
					onkeyup="document.sendSmsForm.charCount.value=140-document.sendSmsForm.message.value.length;" 
					/>
			</td>
		</tr>			
		<tr>
			<td>
				<bean:message key="label.person.remainingChars"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.charCount" property="charCount" size="4" maxlength="3" readonly="true" value="140" />
			</td>
		</tr>
	</table>

	<p />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.person.send"/>
	</html:submit>

	<p />
	<html:link page="/viewSmsDeliveryReports.do">		
	   	<bean:message key="link.person.sms.viewDeliveryReports"/>
	</html:link>

</html:form>

--%>
