<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="email" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.sendEmail" bundle="APPLICATION_RESOURCES"/></h2>

<logic:messagesPresent property="error" message="true">
	<html:messages id="message" property="error" message="true" bundle="MESSAGING_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="problem" message="true">
    <p>
        <span class="error0">
            <bean:message key="messaging.mail.address.problem.text" bundle="MESSAGING_RESOURCES"/>:
          
          	<ul>
            <html:messages id="message" property="problem" message="true" bundle="MESSAGING_RESOURCES">
				   <li><bean:write name="message"/></li>
            </html:messages>
            </ul>
        </span>
    </p>
</logic:messagesPresent>

<logic:messagesPresent property="warning" message="true">
    <html:messages id="message" property="warning" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="confirmation" message="true">
    <html:messages id="message" property="confirmation" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="success0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>


<fr:form action="<%=  "/sendEmail.do?method=send" %>">
	<fr:edit id="mailBean" name="mailBean" schema="MailBean.compose.for.units">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	    
	    <fr:destination name="invalid" path="<%= "/sendEmail.do?method=sendInvalid" %>"/>
	</fr:edit>

	<html:submit><bean:message key="label.send" bundle="APPLICATION_RESOURCES"/></html:submit>

</fr:form>