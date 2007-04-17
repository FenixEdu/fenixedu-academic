<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="link.editDates" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<span class="error0 mtop0">
	<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
	<bean:write name="message" />
	</html:messages>
</span>

<fr:form action="/protocols.do?method=editProtocolHistory">

<fr:view name="protocolHistoryFactory" property="protocol.orderedProtocolHistoriesMinusLast" schema="show.protocolHistories">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 printborder tdleft" />
	</fr:layout>
</fr:view> 
<br/>
<fr:edit name="protocolHistoryFactory" id="protocolHistoryFactory" schema="edit.protocolHistoryFactory" layout="tabular"/>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:cancel>
</p>
</fr:form>