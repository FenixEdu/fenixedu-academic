<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<fr:view name="candidacy" schema="DegreeCandidacy.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<logic:present name="operations">
	<logic:iterate id="operation" name="operations">
		<bean:define id="operationType" name="operation" property="type.name" />
		<html:link action="/degreeCandidacyManagement.do?method=doOperation&operationType=<%=operationType%>">
			<bean:message name="operation" property="label" />
		</html:link>
	</logic:iterate>
</logic:present>