<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
