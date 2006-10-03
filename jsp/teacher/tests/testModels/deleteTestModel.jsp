<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.deleteTestModel" bundle="TESTS_RESOURCES" /></h2>

<h4><bean:message key="message.willDelete" bundle="TESTS_RESOURCES" /></h4>
<fr:view name="testModel" schema="tests.testModel.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>

<ul>
	<li>
		<html:link page="/tests/testModels.do?method=manageTestModels">
			<bean:message key="message.cancel" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
	<li>
		<html:link page="/tests/testModels.do?method=deleteTestModel"
		           paramId="oid" paramName="testModel"
		           paramProperty="idInternal">
			<bean:message key="message.delete" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>
