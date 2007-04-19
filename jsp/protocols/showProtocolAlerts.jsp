<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocolAlerts" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<logic:notEmpty name="protocolHistories">
<p class="mtop2 mbottom0"><strong><bean:message key="message.protocols.alertsList" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
	<fr:view name="protocolHistories" schema="show.protocolHistories.alerts">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
			<fr:property name="link(renew)" value="/protocols.do?method=prepareRenewProtocol" />
			<fr:property name="key(renew)" value="link.renew" />
			<fr:property name="param(renew)" value="protocol.idInternal/idInternal" />
			<fr:property name="bundle(renew)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

<logic:notEmpty name="protocolHistoriesNullEndDate">
	<p class="mtop2 mbottom0"><strong><bean:message key="message.protocols.alertsList.nullEndDate" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
	<fr:view name="protocolHistoriesNullEndDate" schema="show.protocolHistories.alerts">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
			<fr:property name="link(edit)" value="/protocols.do?method=prepareEditProtocolHistory" />
			<fr:property name="key(edit)" value="link.editDates" />
			<fr:property name="param(edit)" value="protocol.idInternal/idInternal" />
			<fr:property name="bundle(edit)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>

<logic:empty name="protocolHistories">
	<logic:empty name="protocolHistoriesNullEndDate">
		<p class="mtop2"><em><bean:message key="message.protocols.noAlerts" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
	</logic:empty>
</logic:empty>