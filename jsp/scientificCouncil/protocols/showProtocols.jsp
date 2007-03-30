<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>
<h2><bean:message key="title.protocols"/></h2>

<html:link page="/protocols.do?method=prepareCreateProtocol"><bean:message key="link.protocols.insert"/></html:link>

<logic:notEmpty name="protocols">
	<fr:view name="protocols" schema="show.protocol.toList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="link(edit)" value="/protocols.do?method=viewProtocol" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="param(edit)" value="idInternal" />
		</fr:layout>
	</fr:view> 
</logic:notEmpty>