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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="pt.ist.fenixframework.DomainObject"%>

<bean:define id="global" name="global"/>
<bean:define id="unread" name="unread"/>
<bean:define id="archive" name="archive"/>
<bean:define id="year" name="year"/>
<bean:define id="month" name="month"/>
<bean:define id="alertMessage" name="alertMessage"/>
<bean:define id="process" name="alertMessage" property="process"/>

<logic:equal name="global" value="true">
	<logic:equal name="unread" value="true">
		<html:link action="/phdIndividualProgramProcess.do?method=viewUnreadAlertMessages">
			« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
		</html:link>
	</logic:equal>
	<logic:equal name="unread" value="false">
		<logic:equal name="archive" value="true">
			<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewAlertMessageArchive&year=" + year + "&month=" + month %>">
				« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
			</html:link>
		</logic:equal>
		<logic:equal name="archive" value="false">
			<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages">
				« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
			</html:link>
		</logic:equal>
	</logic:equal>
</logic:equal>

<logic:equal name="global" value="false">
	<logic:equal name="unread" value="true">
		<html:link action="<%="/phdIndividualProgramProcess.do?method=viewUnreadProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId()%>">
			« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
		</html:link>
	</logic:equal>
	<logic:equal name="unread" value="false">
		<logic:equal name="archive" value="true">
			<html:link action="<%="/phdIndividualProgramProcess.do?method=viewProcessAlertMessageArchive&year=" + year + "&month=" + month + "&processId=" + ((DomainObject) process).getExternalId()%>">
				« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
			</html:link>
		</logic:equal>
		<logic:equal name="archive" value="false">
			<html:link action="<%="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId()%>">
				« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
			</html:link>
		</logic:equal>
	</logic:equal>
</logic:equal>

<br/>

<fr:view name="alertMessage">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage" bundle="PHD_RESOURCES">	
		<fr:slot name="subject">
			<fr:property name="classes" value="bold"/>
		</fr:slot>
		<fr:slot name="process" layout="link">
			<fr:property name="contextRelative" value="true"/>
			<fr:property name="moduleRelative" value="true"/>
			<fr:property name="linkFormat" value="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewAlertMessages&processId=${externalId}" />
			<fr:property name="format" value="${processNumber}"/>
		</fr:slot>
		<fr:slot name="whenCreated" />
		<fr:slot name="body"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15"/>
	</fr:layout>
</fr:view>

<br/>

<html:link action="<%="/phdIndividualProgramProcess.do?method=markAlertMessageAsUnread&global=" + global + "&unread=" + unread + "&archive=" + archive + "&year=" + year + "&month=" + month + "&alertMessageId=" + ((DomainObject) alertMessage).getExternalId() + "&processId=" + ((DomainObject) process).getExternalId()%>">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.alertMessage.markAsUnread"/>
</html:link>
