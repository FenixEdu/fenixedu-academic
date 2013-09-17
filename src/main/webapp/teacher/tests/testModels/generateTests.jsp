<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.generateTests" bundle="TESTS_RESOURCES" /></h2>

<p>
Este modelo de teste tem a capacidade de gerar <bean:write name="bean" property="testModel.maxCombinations" /> varia��o(�es).
</p>

<logic:equal name="bean" property="testModel.gradeComplete" value="false">
<p><span class="warning0">
<bean:message key="message.warning.missingGrades" bundle="TESTS_RESOURCES" />
</span></p>
</logic:equal>

<logic:equal name="bean" property="testModel.canGenerateTests" value="true">
<logic:notEmpty name="firstStep">
<fr:form action="/tests/testModels.do?method=continueGenerateTests">
<fr:edit name="bean"
         id="generate-tests"
         schema="tests.test.create.prepare"
         nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:edit>
<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:notEmpty>
</logic:equal>

<logic:notEmpty name="secondStep">
<logic:equal name="useBoth" value="true">
<fr:form action="/tests/testModels.do?method=generateTests">
<fr:edit name="bean"
         id="generate-tests"
         schema="tests.test.create.both"
         nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidGenerateTests" />
</fr:edit>
<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:equal>

<logic:equal name="useFinalDate" value="true">
<fr:form action="/tests/testModels.do?method=generateTests">
<fr:edit name="bean"
         id="generate-tests"
         schema="tests.test.create.finalDate"
         nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidGenerateTests" />
</fr:edit>
<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:equal>

<logic:equal name="useVariations" value="true">
<fr:form action="/tests/testModels.do?method=generateTests">
<fr:edit name="bean"
         id="generate-tests"
         schema="tests.test.create.variations"
         nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidGenerateTests" />
</fr:edit>
<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:equal>
</logic:notEmpty>
