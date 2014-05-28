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


<bean:define id="global" value="<%= request.getParameter("global") %>" />

<logic:equal value="true" name="global">
	<span class="mbottom1">
		<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages"><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES"/></html:link>
		<bean:size id="messagesSize" name="alertMessagesToNotify"/>
		<logic:notEqual name="messagesSize" value="0">
			<span class="mbottom1">
				<logic:equal name="messagesSize" value="1">
					(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
				</logic:equal>
				<logic:notEqual name="messagesSize" value="1">
					(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/>)
				</logic:notEqual>
			</span>
		</logic:notEqual>
	</span>
</logic:equal>

<logic:equal value="false" name="global">
	<span class="mbottom1">
		<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId"><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES"/></html:link>
		<bean:size id="messagesSize" name="processAlertMessagesToNotify"/>
		<logic:notEqual name="messagesSize" value="0">
			<span class="mbottom1">
				<logic:equal name="messagesSize" value="1">
					(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
				</logic:equal>
				<logic:notEqual name="messagesSize" value="1">
					(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/>)
				</logic:notEqual>
			</span>
		</logic:notEqual>
	</span>
</logic:equal>
