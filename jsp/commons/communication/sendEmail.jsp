<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.sendEmail" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="action" name="actionPath"/>
<bean:define id="unitID" name="unit" property="idInternal"/>

<%--
<ul>
	<li>
		<html:link page="<%= action + "?method=prepare&unitId=" + unitID %>"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>
--%>

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


<bean:define id="schema" value="MailBean.compose.for.units"/>

<logic:present name="unit" property="site">
	<logic:equal name="unit" property="site.class.simpleName" value="PedagogicalCouncilSite">
		<bean:define id="schema" value="MailBean.compose.for.units.fromReadOnly"/>
	</logic:equal>
	<logic:equal name="unit" property="site.class.simpleName" value="ScientificCouncilSite">
		<bean:define id="schema" value="MailBean.compose.for.units.fromReadOnly"/>
	</logic:equal>
</logic:present>

<fr:form action="<%= action + "?method=send&amp;unitId=" + unitID %>">
	<fr:edit id="mailBean" name="mailBean" schema="<%= schema %>">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	    
	    <fr:destination name="invalid" path="<%= action + "?method=sendInvalid&amp;unitId=" + unitID %>"/>
	</fr:edit>

	<html:submit><bean:message key="label.send" bundle="APPLICATION_RESOURCES"/></html:submit>

</fr:form>

