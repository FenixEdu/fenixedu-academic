<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<jsp:include page="/phd/common/viewAlertMessage.jsp"/>

<h3><bean:message key="title.phd.alertMessages.possible.responsible.for.this.message.generation" bundle="PHD_RESOURCES" /></h3>

<p><em>
		<bean:message key="label.phd.alertMessages.one.of.alerts.may.generated.message" bundle="PHD_RESOURCES" />
</em></p>

<bean:define id="possibleAlerts" name="alertMessage" property="alertsPossibleResponsibleForMessageGeneration" />

<logic:empty name="possibleAlerts">
	<bean:message key="message.phd.alertMessages.alerts.not.found" bundle="PHD_RESOURCES" />
</logic:empty>

<logic:notEmpty name="possibleAlerts">

	<logic:iterate id="alert" name="possibleAlerts">
		
		<fr:view name="alert">
	
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.alert.PhdAlert" bundle="PHD_RESOURCES">
				<fr:slot name="whenCreated" />
				<fr:slot name="active" />
				<fr:slot name="fireDate" />
				
				<logic:equal name="alert" property="class.simpleName" value="PhdCustomAlert"> 
					<fr:slot name="targetGroupInText" />
				</logic:equal>
				
				<fr:slot name="formattedSubject" />
				<fr:slot name="formattedBody" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight" />
			</fr:layout>
			
		</fr:view>
		
	</logic:iterate>

</logic:notEmpty>

<h3>Possiveis emails</h3>

<bean:define id="possibleEmails" name="alertMessage" property="emailsWithMatchWithThisMessage" />

<logic:empty name="possibleEmails">
	<bean:message key="message.phd.alertMessages.emails.not.found" bundle="PHD_RESOURCES" />
</logic:empty>

<logic:notEmpty name="possibleEmails">

	<logic:iterate id="email" name="possibleEmails">
		<fr:view name="email">
			
			<fr:schema type="net.sourceforge.fenixedu.domain.util.email.Message" bundle="PHD_RESOURCES" >
				<fr:slot name="sender.fromName" bundle="MESSAGING_RESOURCES" key="label.fromName"/>
				<fr:slot name="sender.fromAddress" bundle="MESSAGING_RESOURCES" key="label.fromAddress"/>
				<fr:slot name="created" bundle="MESSAGING_RESOURCES" key="label.email.created"/>
				<fr:slot name="sent" bundle="MESSAGING_RESOURCES" key="label.email.sentDate"/>
				<fr:slot name="replyTos" bundle="MESSAGING_RESOURCES" key="label.replyTos">
				    <fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.util.email.ReplyTo.selectItem"/>
			        <fr:property name="eachLayout" value="values"/>
				</fr:slot>
				<fr:slot name="recipientsAsToText" bundle="MESSAGING_RESOURCES" key="label.receiversGroup.to"/>
				<fr:slot name="recipientsAsCcText" bundle="MESSAGING_RESOURCES" key="label.receiversGroup.cc"/>
				<fr:slot name="recipientsAsText" bundle="MESSAGING_RESOURCES" key="label.receiversGroup"/>
				<fr:slot name="recipientsGroupMembersInText" bundle="MESSAGING_RESOURCES" key="label.receiversGroup"/>
				<fr:slot name="subject" bundle="MANAGER_RESOURCES" key="label.email.subject"/>
				<fr:slot name="body" bundle="MANAGER_RESOURCES" key="label.email.message" />
				<fr:slot name="htmlBody" bundle="MESSAGING_RESOURCES" key="label.email.message.html.content" layout="code-block"/>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight" />
			</fr:layout>
			
		</fr:view>

		<hr />
	</logic:iterate>
	
</logic:notEmpty>
