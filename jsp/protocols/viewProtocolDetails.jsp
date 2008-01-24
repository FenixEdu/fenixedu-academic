<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<bean:define id="protocolID"><bean:write name="protocolFactory" property="protocol.idInternal"/></bean:define>


<!-- Protocol Data -->
<div class="mtop2 mbottom05 separator2">
	<h3 class="dinline"><bean:message key="label.protocol.data" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
</div>

<fr:view name="protocolFactory" schema="show.protocol.data">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright"/>
		<fr:property name="columnClasses" value="nowrap,"/>
	</fr:layout>
</fr:view>


<!-- Responsibles -->
<div class="mtop2 mbottom05 separator2">
	<h3 class="dinline"><bean:message key="label.protocol.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
</div>


<p class="mbottom0"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="responsibles">
	<fr:view name="protocolFactory" property="responsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mvert025 thhide1"/>
			<fr:property name="rowClasses" value="thhide2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:notEmpty name="protocolFactory" property="responsibleFunctions">
	<fr:view name="protocolFactory" property="responsibleFunctions" schema="show.protocol.responsibleFunction">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mvert025 thhide1"/>
			<fr:property name="rowClasses" value="thhide2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="responsibles">
	<logic:empty name="protocolFactory" property="responsibleFunctions">
		<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
	</logic:empty>
</logic:empty>

<p class="mbottom0"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
	<fr:view name="protocolFactory" property="partnerResponsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mvert025 thhide1"/>
			<fr:property name="rowClasses" value="thhide2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>


<!-- Units -->
<div class="mtop2 mbottom05 separator2">
	<h3 class="dinline"><bean:message key="label.protocol.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
</div>

<p class="mbottom0"><strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<fr:view name="protocolFactory" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mvert025 thhide1"/>
			<fr:property name="rowClasses" value="thhide2"/>
	</fr:layout>
</fr:view>
<logic:empty name="protocolFactory" property="units">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>

<p class="mbottom0"><strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerUnits">
	<fr:view name="protocolFactory" property="partnerUnits" schema="show.protocol.partnerUnit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mvert025 thhide1"/>
			<fr:property name="rowClasses" value="thhide2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerUnits">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>


<!-- Files -->
<div class="mtop2 mbottom05 separator2">
	<h3 class="dinline"><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
</div>


<logic:notEmpty name="protocolFactory" property="protocol.protocolFiles">
<table class="tstyle1 thlight">
<tr>
	<th><bean:message key="label.filename" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
</tr>
<logic:iterate id="file" name="protocolFactory" property="protocol.protocolFiles">
	<bean:define id="url"><bean:write name="file" property="downloadUrl"/></bean:define>
	<tr>
		<td><html:link href="<%= url %>" target="_blank"><bean:write name="file" property="filename"/></html:link></td>
	</tr>
</logic:iterate>
</table>	
</logic:notEmpty>

<logic:empty name="protocolFactory" property="protocol.protocolFiles">
	<p class="mtop05"><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
