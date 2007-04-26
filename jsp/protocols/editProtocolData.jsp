<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:form action="/editProtocol.do?method=editProtocolData">
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<h3 class="mtop15"><bean:message key="label.protocol.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<fr:hasMessages for="protocolHistoryDates">
	<p class="mtop15"><span class="error0 mbottom0"><fr:message for="protocolHistoryDates" show="message"/></span></p>
</fr:hasMessages>
<p>
	<span class="error0">
		<html:messages id="dateMessage" name="errorDateMessage" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<bean:write name="dateMessage" />
		</html:messages>
	</span>
</p>
<p class="mbottom05"><bean:message key="label.protocol.dates" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</p>
<fr:edit id="protocolHistoryDates" schema="edit.protocolHistory" name="protocolFactory" property="protocolHistories">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5 thcenter thlight mtop0"/>
	</fr:layout>
</fr:edit>

<p>
	<span class="error0">
		<html:errors bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		<html:messages id="message" name="errorMessage" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<logic:equal name="protocolFactory" property="protocol.active" value="false">
	<fr:edit id="protocol" name="protocolFactory" schema="edit.protocol.data">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:equal>
<logic:equal name="protocolFactory" property="protocol.active" value="true">
	<fr:edit id="protocol" name="protocolFactory" schema="edit.protocol.totalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:equal>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
</p>
</fr:form>