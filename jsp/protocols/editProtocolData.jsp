<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.edit"/></h2>

<fr:form action="/editProtocol.do?method=editProtocolData">

<h3 class="mtop15 mbottom05"><bean:message key="label.protocol.data"/></h3>
<fr:edit name="protocolFactory" schema="edit.protocol">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back" property="back">
		<bean:message key="button.back" />
	</html:cancel>
</p>
</fr:form>