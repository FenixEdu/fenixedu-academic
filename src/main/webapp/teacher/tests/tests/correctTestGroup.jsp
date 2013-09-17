<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="testGroup" property="name" /></h2>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/teacher.js" %>">
</script>

<bean:define id="testGroup" name="testGroup" type="net.sourceforge.fenixedu.domain.tests.NewTestGroup" />

<ul>
	<li>
		<f:parameterLink page="/tests/tests.do?method=manageTests">
			<f:parameter id="oid" name="executionCourse" property="externalId" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/tests.do?method=publishGrades">
			<f:parameter id="oid" name="testGroup" property="externalId" />
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
