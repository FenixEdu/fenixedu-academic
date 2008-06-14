<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2>
<fr:view name="modelGroup" property="positionPath" layout="flowLayout">
	<fr:layout name="flowLayout">
		<fr:property name="htmlSeparator" value="." />
	</fr:layout>
</fr:view>) <fr:view name="modelGroup" schema="tests.modelGroup.name" layout="values" />
</h2>

<bean:define id="modelGroup" name="modelGroup" type="net.sourceforge.fenixedu.domain.tests.NewModelGroup" />

<ul>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=editTestModel">
			<f:parameter id="oid" name="modelGroup" property="testModel.idInternal" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<table class="tstyle7 thright mtop0"><tr><td>
<html:form action="/tests/testModels.do?method=editModelGroup">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= modelGroup.getIdInternal().toString() %>" />
	<fr:edit id="edit-model-group" name="modelGroup" schema="tests.modelGroup.name" layout="flow" nested="true" />
	<html:submit><bean:message key="message.question.alter" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
</td></tr></table>

<div class="questionBlockHeader">
	<strong><bean:message key="label.testElement.presentationMaterials" bundle="TESTS_RESOURCES" />:</strong>
	(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="modelGroup" property="idInternal" />
	 	<f:parameter id="returnPath" value="/tests/testModels.do?method=editModelGroup" />
	 	<f:parameter id="returnId" name="modelGroup" property="idInternal" />
	 	<f:parameter id="contextKey" value="message.testModels.manage" />
	 	<bean:message key="message.question.edit" bundle="TESTS_RESOURCES" />
	 </f:parameterLink>)
</div>
<fr:view name="modelGroup" schema="tests.testElement.simple">
	<fr:layout name="values">
		<fr:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</fr:layout>
</fr:view>
