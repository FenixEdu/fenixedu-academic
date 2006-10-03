<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="testModel" property="name" /></h2>

<bean:define id="testModel" name="testModel" type="net.sourceforge.fenixedu.domain.tests.NewTestModel" />
<bean:define id="testModelId"><bean:write name="testModel" property="idInternal" /></bean:define>

<script type="text/javascript" language="javascript" src="tests.js"></script>

<ul>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=manageTestModels">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<bean:message key="message.toModel" bundle="TESTS_RESOURCES" />
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=selectQuestions">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toSelect" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=sortTestModel">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toSort" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<logic:equal name="testModel" property="oversized" value="true">
<p><span class="warning0">
<bean:message key="message.warning.invalidGrades" bundle="TESTS_RESOURCES" />
</span></p>
</logic:equal>

<div>
	<fr:view name="testModel" schema="tests.testModel.name.view" layout="flow" />
	<span class="switchInline">(<a href="javascript:switchDisplay('edit-title-div')"><bean:message key="message.question.edit" bundle="TESTS_RESOURCES" /></a>)</span>
</div>
<div id="edit-title-div" class="switchNone">
	<table class="tstyle7 thright mtop0"><tr><td>
	<html:form action="/tests/testModels.do?method=editTestModel">
		<html:hidden property="oid" value="<%= testModelId %>" />
		<fr:edit id="edit-title" name="testModel" schema="tests.testModel.name" nested="true" layout="flow" />
		<html:submit><bean:message key="message.question.alter" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('edit-title-div')"><bean:message key="message.cancel" bundle="TESTS_RESOURCES" /></a></span>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="edit-title">
	<script type="text/javascript">
		showElement('edit-title-div');
	</script>
</fr:hasMessages>

<div>
	<fr:view name="testModel" schema="tests.testModel.scale.view" layout="flow" />
	<span class="switchInline">(<a href="javascript:switchDisplay('edit-scale-div')"><bean:message key="message.question.edit" bundle="TESTS_RESOURCES" /></a>)</span>
</div>
<div id="edit-scale-div" class="switchNone">
	<table class="tstyle7 thright mtop0"><tr><td>
	<html:form action="/tests/testModels.do?method=editTestModel">
		<html:hidden property="oid" value="<%= testModelId %>" />
		<fr:edit id="edit-scale" name="testModel" schema="tests.testModel.scale" layout="flow" />
		<html:submit><bean:message key="message.question.alter" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('edit-scale-div')"><bean:message key="message.cancel" bundle="TESTS_RESOURCES" /></a></span>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="edit-scale">
	<script type="text/javascript">
		showElement('edit-scale-div');
	</script>
</fr:hasMessages>

<div>
	<strong><bean:message key="label.testElement.presentationMaterials" bundle="TESTS_RESOURCES" />:</strong>
	(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="testModel" property="idInternal" />
	 	<f:parameter id="returnPath" value="/tests/testModels.do?method=editTestModel" />
	 	<f:parameter id="returnId" name="testModel" property="idInternal" />
	 	<f:parameter id="contextKey" value="message.testModels.manage" />
	 	<bean:message key="message.question.edit" bundle="TESTS_RESOURCES" />
	 </f:parameterLink>)
</div>
<fr:view name="testModel" schema="tests.testElement.simple">
	<fr:layout name="values">
		<fr:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</fr:layout>
</fr:view>

<ul class="switchBlock">
<li>
<a href="javascript:switchDisplay('create-model-group-div')"><bean:message key="message.createSection" bundle="TESTS_RESOURCES" /></a>
</li>
</ul>
<div class="switchNone"><p class="mbottom05"><strong><bean:message key="message.createSection" bundle="TESTS_RESOURCES" />:</strong></p></div>

<div id="create-model-group-div" class="switchNone">
	<table class="tstyle7 thright mtop0"><tr><td>
	<html:form action="/tests/testModels.do?method=createModelGroup">
		<fr:edit id="create-model-group"
		         name="bean"
		         schema="tests.modelGroup.create"
		         nested="true">
			<fr:layout name="tabular">
			</fr:layout>
			<fr:destination name="invalid" path="/tests/testModels.do?method=invalidCreateModelGroup" />
		</fr:edit>
		<html:submit><bean:message key="label.button.add" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('create-model-group-div')"><bean:message key="message.cancel" bundle="TESTS_RESOURCES" /></a></span>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="create-model-group">
	<script type="text/javascript">
		showElement('create-model-group-div');
	</script>
</fr:hasMessages>

<fr:view name="testModel" layout="template-model-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
