<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.edit"/></h2>


<h3 class="mtop15"><bean:message key="label.protocol.units"/></h3>

<strong><bean:message key="label.protocol.ist"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="units">
<fr:view name="protocolFactory" property="units" schema="show.protocol.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="units">
	<p><em><bean:message key="label.protocol.hasNone"/></em></p>
</logic:empty>
<br/>

<strong><bean:message key="label.protocol.partner"/></strong><br/>
<logic:notEmpty name="protocolFactory" property="partnerUnits">
<fr:view name="protocolFactory" property="partnerUnits" schema="show.protocol.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>		
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="protocolFactory" property="partnerUnits">
	<p><em><bean:message key="label.protocol.hasNone"/></em></p>
</logic:empty>
<br/>

<fr:form action="/protocols.do?method=editUnits">

<span class="error0">
	<html:errors/>
	<html:messages id="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages>
</span>

<logic:equal name="protocolFactory" property="internalUnit" value="true">
<fr:edit id="unit" name="protocolFactory" schema="search.unit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changeUnitType" path="/protocols.do?method=prepareEditUnits"/>
</fr:edit>
</logic:equal>

<logic:equal name="protocolFactory" property="internalUnit" value="false">
<fr:edit id="partnerUnit" name="protocolFactory" schema="search.partnerUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="changeUnitType" path="/protocols.do?method=prepareEditUnits"/>
</fr:edit>
</logic:equal>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.insert" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" styleClass="inputbutton" property="cancel">
		<bean:message key="button.back" />
	</html:cancel>
</p>
</fr:form>