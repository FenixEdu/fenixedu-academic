<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.deleteQuestion" bundle="TESTS_RESOURCES" /></h2>

<h4><bean:message key="message.willDelete" bundle="TESTS_RESOURCES" /></h4>
<logic:equal name="question" property="composite" value="false">
<fr:view name="question" schema="tests.question.simple">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>
</logic:equal>
<logic:equal name="question" property="composite" value="true">
<fr:view name="question" layout="short" />
</logic:equal>

<ul>
<logic:present name="parentQuestionGroupId">
<li>
<f:parameterLink page="/tests/questionBank.do?method=editTestElement">
	<f:parameter id="oid" name="parentQuestionGroupId" />
	<bean:message key="message.cancel" bundle="TESTS_RESOURCES" />
</f:parameterLink>
</li>

<li>
<f:parameterLink page="/tests/questionBank.do?method=deleteQuestion">
	<f:parameter id="oid" name="questionGroupId" />
	<f:parameter id="parentQuestionGroupId" name="parentQuestionGroupId" />
	<bean:message key="message.delete" bundle="TESTS_RESOURCES" />
</f:parameterLink>
</li>
</logic:present>

<logic:notPresent name="parentQuestionGroupId">
<li>
<f:parameterLink page="/tests/questionBank.do?method=editTestElement">
	<f:parameter id="oid" name="questionGroupId" />
	<bean:message key="message.cancel" bundle="TESTS_RESOURCES" />
</f:parameterLink>
</li>
<li>
<f:parameterLink page="/tests/questionBank.do?method=deleteQuestion">
	<f:parameter id="oid" name="questionGroupId" />
	<bean:message key="message.delete" bundle="TESTS_RESOURCES" />
</f:parameterLink>
</li>
</logic:notPresent>
</ul>
