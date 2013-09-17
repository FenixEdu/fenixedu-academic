<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:view name="candidacy" schema="DegreeCandidacy.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<logic:present name="operations">
	<logic:iterate id="operation" name="operations">
		<bean:define id="operationType" name="operation" property="type.name" />
		<html:link action="/degreeCandidacyManagement.do?method=doOperation&amp;operationType=<%=operationType%>">
			<bean:message name="operation" property="label" />
		</html:link>
	</logic:iterate>
</logic:present>