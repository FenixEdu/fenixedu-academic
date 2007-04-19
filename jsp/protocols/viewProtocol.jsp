<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<bean:define id="protocolID"><bean:write name="protocolFactory" property="protocol.idInternal"/></bean:define>


<!-- Protocol Data -->
<div class="mtop2 mbottom05">
<h3 class="dinline"><bean:message key="label.protocol.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<p class="dinline">(<html:link page="<%= "/editProtocol.do?method=prepareEditProtocolData&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>)</p>
</div>

<fr:view name="protocolFactory" schema="show.protocol.data">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright"/>
	</fr:layout>
</fr:view>


<!-- Responsibles -->
<div class="mtop2 mbottom05">
<h3 class="dinline"><bean:message key="label.protocol.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<p class="dinline">(<html:link page="<%= "/editProtocol.do?method=prepareEditResponsibles&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>)</p>
</div>

<p class="mbottom0"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="responsibles">
	<fr:view name="protocolFactory" property="responsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="responsibles">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>

<p class="mbottom0"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
	<fr:view name="protocolFactory" property="partnerResponsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>


<!-- Units -->
<div class="mtop2 mbottom05">
<h3 class="dinline"><bean:message key="label.protocol.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<p class="dinline">(<html:link page="<%= "/editProtocol.do?method=prepareEditUnits&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></html:link>)</p>
</div>

<p class="mbottom0"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<fr:view name="protocolFactory" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
<logic:empty name="protocolFactory" property="units">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>

<p class="mbottom0"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerUnits">
	<fr:view name="protocolFactory" property="partnerUnits" schema="show.protocol.partnerUnit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerUnits">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>


<!-- Files -->
<div class="mtop2 mbottom05">
<h3 class="dinline"><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<p class="dinline">(<html:link page="<%= "/editProtocol.do?method=prepareEditProtocolFiles&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></html:link>)</p>
</div>


<logic:notEmpty name="protocolFactory" property="protocol.protocolFiles">
	<fr:view name="protocolFactory" property="protocol.protocolFiles" schema="show.file">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="protocol.protocolFiles">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
