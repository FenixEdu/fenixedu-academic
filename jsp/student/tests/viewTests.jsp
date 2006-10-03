<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.viewTests" bundle="TESTS_RESOURCES" /></h2>

<script type="text/javascript" language="javascript" src="tests.js">
</script>


<h4><bean:message key="label.publishedTests" bundle="TESTS_RESOURCES" /></h4>
<logic:notEmpty name="publishedTestGroups">
	<fr:view name="publishedTestGroups" schema="tests.testGroup.view.student">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
<logic:empty name="publishedTestGroups">
	<bean:message key="tests.testGroups.empty" bundle="TESTS_RESOURCES" />
</logic:empty>

<h4><bean:message key="label.correctedTests" bundle="TESTS_RESOURCES" /></h4>
<logic:notEmpty name="finishedTestGroups">
	<fr:view name="finishedTestGroups" schema="tests.testGroup.view.student.grade">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="finishedTestGroups">
	<bean:message key="tests.testGroups.empty" bundle="TESTS_RESOURCES" />
</logic:empty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
