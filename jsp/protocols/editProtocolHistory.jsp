<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="link.editDates" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:form action="/protocols.do?method=editProtocolHistory">

<p class="mtop15">
	<span class="error0">
		<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<bean:write name="message" />
		</html:messages>
	</span>
</p>

<logic:notEmpty name="protocolHistoryFactory" property="protocol.orderedProtocolHistoriesMinusLast">
	<fr:view name="protocolHistoryFactory" property="protocol.orderedProtocolHistoriesMinusLast" schema="show.protocolHistories">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

<fr:edit name="protocolHistoryFactory" id="protocolHistoryFactory" schema="edit.protocolHistoryFactory">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
		<fr:property name="columnClasses" value=",,tderror1 tdclear" />
	</fr:layout>

</fr:edit>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</html:cancel>
</p>
</fr:form>
