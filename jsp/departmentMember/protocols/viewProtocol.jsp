<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols"  bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<br/>

<bean:define id="protocolID"><bean:write name="protocol" property="idInternal"/></bean:define>

<h3><bean:message key="label.protocol.data"  bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
<fr:view name="protocol" schema="view.protocol">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright"/>
	</fr:layout>
</fr:view>
<br/>

<!-- Responsibles -->
<h3><bean:message key="label.protocol.responsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocol" property="responsibles">
	<fr:view name="protocol" property="responsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocol" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>

<br/>

<strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocol" property="partnerResponsibles">
	<fr:view name="protocol" property="partnerResponsibles" schema="show.protocol.responsible">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="protocol" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<br/>

<!-- Units -->
<h3><bean:message key="label.protocol.units" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<fr:view name="protocol" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
<logic:empty name="protocol" property="units">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<br/>
<strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
<logic:notEmpty name="protocol" property="partners">
<fr:view name="protocol" property="partners" schema="show.protocol.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocol" property="partners">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<br/>

<!-- Files -->

<h3><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<logic:notEmpty name="protocol" property="protocolFiles">
<fr:view name="protocol" property="protocolFiles" schema="show.file">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocol" property="protocolFiles">
	<em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
</logic:empty>