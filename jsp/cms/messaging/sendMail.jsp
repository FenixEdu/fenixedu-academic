<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.sendEmail" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="sendMailActioName" name="sendMailForm" property="sendMailActioName"/>
<jsp:include page="/cms/manager/context.jsp"/>

<logic:present name="groupsToChooseFrom">
	<bean:define id="groups" name="sendMailForm" property="groupsToChooseFrom"/>
</logic:present>

<html:form method="post" action="<%=sendMailActioName.toString()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="send"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.group" property="group"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnURL" property="returnURL"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.previousRequestParameters" property="previousRequestParameters"/>

<logic:present name="groups">
	<div style="width:800px">
		<ul>
			<logic:iterate id="group" name="groups" type="net.sourceforge.fenixedu.domain.PersonalGroup">
				<li class="fleft" style="width: 250px;">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedPersonalGroupsIds" property="selectedPersonalGroupsIds">
	       				 <bean:write name="group" property="idInternal"/>
				    </html:multibox>
			        <bean:write name="group" property="name"/>
				</li>
			</logic:iterate>			
		</ul>			
	</div>
</logic:present>

<table class="tstyle5 tdtop thlight thright">
	<tr>
		<th><bean:message key="label.sendEmail.senderName" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fromPersonalName" property="fromPersonalName" size="50"/></td>
	</tr>
	<tr>
		<th><bean:message key="label.sendEmail.senderAdress" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.fromAddress" property="fromAddress" size="50"/></td>
	</tr>
	<tr>
		<th><bean:message key="label.sendEmail.CC" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.copyTo" property="copyTo" size="50"/><font size="-2"> (separe os endere�os por v�rgulas)</font></td>
	</tr>
	<tr>
		<th><bean:message key="label.sendEmail.copyToSender" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copyToSender" property="copyToSender"/>(assinale caso deseje receber uma c�pia da mensagem no endere�o do remetente)</td>
	</tr>
	<tr>
		<th><bean:message key="label.sendEmail.subject" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" size="50"/></td>
	</tr>
	<tr>
		<th><bean:message key="label.sendEmail.message" bundle="APPLICATION_RESOURCES"/>:</th>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.message" rows="16" cols="80" property="message"/></td>
	</tr>
</table>

<%--
<fieldset class="lfloat3">
	<p><label><bean:message key="label.sendEmail.senderName" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.fromPersonalName" property="fromPersonalName" size="50"/></p>
	<p><label><bean:message key="label.sendEmail.senderAdress" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.fromAddress" property="fromAddress" size="50"/></p>
	<p><label><bean:message key="label.sendEmail.CC" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.copyTo" property="copyTo" size="50"/><font size="-2"> (separe os endere�os por v�rgulas)</font></p>
	<p><label><bean:message key="label.sendEmail.copyToSender" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copyToSender" property="copyToSender"/>(assinale caso deseje receber uma c�pia da mensagem no endere�o do remetente)</p>
	<p><label><bean:message key="label.sendEmail.subject" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" size="50"/></p>
	<p><label><bean:message key="label.sendEmail.message" bundle="APPLICATION_RESOURCES"/>:&nbsp;</label><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.message" rows="20" cols="100" property="message"/></p>
</fieldset>
--%>


<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='send';this.form.submit();">
	Enviar
</html:submit>

</html:form>