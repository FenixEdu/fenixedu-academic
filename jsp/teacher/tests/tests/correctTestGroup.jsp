<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="testGroup" property="name" /></h2>

<script type="text/javascript" language="javascript" src="tests.js">
</script>

<bean:define id="testGroup" name="testGroup" type="net.sourceforge.fenixedu.domain.tests.NewTestGroup" />

<ul>
	<li>
		<f:parameterLink page="/tests/tests.do?method=manageTests">
			<f:parameter id="oid" name="executionCourse" property="idInternal" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/tests.do?method=publishGrades">
			<f:parameter id="oid" name="testGroup" property="idInternal" />
			<bean:message key="message.publishGrades" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<strong><bean:message key="message.tests.corrected" bundle="TESTS_RESOURCES" />:</strong>
<fr:view name="uncorrectedByPerson">
	<fr:layout>
		<fr:property name="eachSchema" value="tests.test.correct-by-person" />
		<fr:property name="sortBy" value="person.name" />
		<fr:property name="eachLayout" value="values" />
	</fr:layout>
</fr:view>

<strong><bean:message key="message.tests.uncorrected" bundle="TESTS_RESOURCES" />:</strong>
<fr:view name="correctedByPerson">
	<fr:layout>
		<fr:property name="eachSchema" value="tests.test.correct-by-person" />
		<fr:property name="sortBy" value="person.name" />
		<fr:property name="eachLayout" value="values" />
	</fr:layout>
</fr:view>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
