<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.manageTestModels" bundle="TESTS_RESOURCES" /></h2>

<span class="errors"><html:errors/></span>

<script type="text/javascript" language="javascript" src="tests.js">
</script>

<ul class="switchBlock">
<li><a href="javascript:switchDisplay('create-test-model-div')"><bean:message key="message.createTestModel" bundle="TESTS_RESOURCES" /></a></li>
</ul>
<div class="switchNone"><p><strong><bean:message key="message.createTestModel" bundle="TESTS_RESOURCES" />:</strong></p></div>

<div id="create-test-model-div" class="switchNone">
	<table class="tstyle7 thright mtop0"><tr><td>
	<html:form action="/tests/testModels.do?method=createTestModel">
		<fr:create id="create-test-model"
		           schema="tests.testModel.create"
		           type="net.sourceforge.fenixedu.domain.tests.NewTestModel"
		           layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="columnClasses" value="width10em,," />
			</fr:layout>
			<fr:hidden slot="creator" name="UserView" property="person.teacher" />
			<fr:destination name="invalid" path="/tests/testModels.do?method=createTestModel" />
		</fr:create>
		<html:submit><bean:message key="label.button.create" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('create-test-model-div')"><bean:message key="message.cancel" bundle="TESTS_RESOURCES" /></a></span>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="create-test-model">
	<script type="text/javascript">
		showElement('create-test-model-div');
	</script>
</fr:hasMessages>

<h4><bean:message key="message.testModels.list" bundle="TESTS_RESOURCES" /></h4>
<logic:notEmpty name="testModels">
<fr:view name="testModels" schema="tests.testModel.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="link(generate)" value="/tests/testModels.do?method=prepareGenerateTests" />
		<fr:property name="param(generate)" value="idInternal/oid" />
		<fr:property name="key(generate)" value="label.button.generateTest" />
		<fr:property name="bundle(generate)" value="TESTS_RESOURCES" />
		<fr:property name="link(edit)" value="/tests/testModels.do?method=editTestModel" />
		<fr:property name="param(edit)" value="idInternal/oid" />
		<fr:property name="key(edit)" value="message.question.edit" />
		<fr:property name="bundle(edit)" value="TESTS_RESOURCES" />
		<fr:property name="link(delete)" value="/tests/testModels.do?method=prepareDeleteTestModel" />
		<fr:property name="param(delete)" value="idInternal/oid" />
		<fr:property name="key(delete)" value="message.delete" />
		<fr:property name="bundle(delete)" value="TESTS_RESOURCES" />
		
		<fr:property name="groupLinks" value="true" />
		<fr:property name="linkGroupSeparator" value=", " />
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="testModels">
	<bean:message key="tests.testModels.empty" bundle="TESTS_RESOURCES" />
</logic:empty>

<h4><bean:message key="label.generatedTests" bundle="TESTS_RESOURCES" /></h4>

<logic:notEmpty name="testGroups">
<fr:view name="testGroups" schema="tests.testGroup.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="testGroups">
	<bean:message key="tests.testGroups.empty" bundle="TESTS_RESOURCES" />
</logic:empty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
