<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<ul class="list5">
	<li>
		<html:link page="/createProtocol.do?method=prepareCreateProtocolData"><bean:message key="link.protocols.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>
	</li>
</ul>

<logic:notEmpty name="protocols">
	<fr:view name="protocols" schema="show.protocol.toList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="link(edit)" value="/editProtocol.do?method=viewProtocol" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="param(edit)" value="idInternal" />
			<fr:property name="bundle(edit)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>