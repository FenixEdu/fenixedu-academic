<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<br/>

<bean:define id="protocolID"><bean:write name="protocolFactory" property="protocol.idInternal"/></bean:define>

<h3><bean:message key="label.protocol.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<fr:view name="protocolFactory" schema="show.protocol.data">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright"/>
	</fr:layout>
</fr:view>
<ul>
	<li>		
		<html:link page="<%= "/editProtocol.do?method=prepareEditProtocolData&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>
	</li>
</ul>
<br/>

<!-- Responsibles -->
<h3><bean:message key="label.protocol.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="responsibles">
	<fr:view name="protocolFactory" property="responsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>

<br/>

<strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
	<fr:view name="protocolFactory" property="partnerResponsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<ul>
	<li>		
		<html:link page="<%= "/editProtocol.do?method=prepareEditResponsibles&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>
	</li>
</ul>
<br/>

<!-- Units -->
<h3><bean:message key="label.protocol.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<fr:view name="protocolFactory" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
<logic:empty name="protocolFactory" property="units">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<br/>
<strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="partnerUnits">
<fr:view name="protocolFactory" property="partnerUnits" schema="show.protocol.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerUnits">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<ul>
	<li>		
		<html:link page="<%= "/editProtocol.do?method=prepareEditUnits&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></html:link>
	</li>
</ul>
<br/>

<!-- Files -->
<h3><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<logic:notEmpty name="protocolFactory" property="protocol.protocolFiles">
<fr:view name="protocolFactory" property="protocol.protocolFiles" schema="show.file">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="protocol.protocolFiles">
	<em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
</logic:empty>
<ul>
	<li>		
		<html:link page="<%= "/editProtocol.do?method=prepareEditProtocolFiles&amp;protocolID=" + protocolID %>"><bean:message key="link.protocol.manage.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></html:link>
	</li>
</ul>