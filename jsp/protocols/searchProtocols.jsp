<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<h2><bean:message key="link.protocols.search" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></h2>
<logic:present name="protocolSearch">
	<fr:form action="/protocols.do?method=searchProtocols">
		<html:errors/>
		<p><span class="error0"><fr:message for="protocolBeginDate"/></span>
		<p><span class="error0"><fr:message for="protocolEndDate"/></span>
		<p><span class="error0"><fr:message for="signedDate"/></span>

		<fr:edit name="protocolSearch" id="number" schema="edit.protocolSearch.number"/>
		<fr:edit name="protocolSearch" id="protocolBeginDate" schema="edit.protocolSearch.protocolBeginDate" layout="flow">
			<fr:layout name="flow">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		<br/>
		<fr:edit name="protocolSearch" id="protocolEndDate" schema="edit.protocolSearch.protocolEndDate" layout="flow">
			<fr:layout name="flow">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		<br/>
		<fr:edit name="protocolSearch" id="signedDate" schema="edit.protocolSearch.signedDate" layout="flow">
			<fr:layout name="flow">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		<br/>
		<fr:edit name="protocolSearch" id="protocolSearch" schema="edit.protocolSearch"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.ok" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</html:submit>
	</fr:form>
	<br/>
	<br/>
	<logic:notEmpty name="protocolSearch" property="search">
		<fr:view name="protocolSearch" property="search" layout="tabular" schema="show.protocol.toList" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view> 
	</logic:notEmpty>
</logic:present>

