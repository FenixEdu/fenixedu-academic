<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<%@page import="net.sourceforge.fenixedu.domain.tests.NewModelRestriction"%>
<%@page import="java.util.List"%>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="testModel" property="name" /></h2>

<ul>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=manageTestModels">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=editTestModel">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toModel" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<bean:message key="message.toSelect" bundle="TESTS_RESOURCES" />
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=sortTestModel">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toSort" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<bean:define id="testModel" name="testModel" type="net.sourceforge.fenixedu.domain.tests.NewTestModel" />

<p class="infoop">Pode usar <fr:view name="testModel" property="bag.childAtomicQuestionsCount" /> pergunta(s)
e <fr:view name="testModel" property="bag.childQuestionGroupsCount" /> grupo(s)</p>

<fr:view name="testModel" layout="template-select-tree" />

<h4><bean:message key="message.select.atomicQuestion.header" bundle="TESTS_RESOURCES" />:</h4>
<logic:empty name="testModel" property="atomicQuestionRestrictions">
<span class="empty"><bean:message key="tests.atomicQuestions.empty" bundle="TESTS_RESOURCES" /></span>
</logic:empty>

<logic:notEmpty name="testModel" property="atomicQuestionRestrictions">
<html:form action="/tests/testModels.do?method=selectAtomicQuestionRestrictions">
	<fr:edit name="bean" schema="tests.testModel.selectAtomicQuestions" id="select-atomic-questions">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright" />
			<fr:property name="columnClasses" value="width10em,,tdwarning tdclear" />
		</fr:layout>
		<fr:destination name="invalid" path="/tests/testModels.do?method=invalidSelectAtomicQuestionRestrictions" />
	</fr:edit>
<%
	List<NewModelRestriction> selectedAtomicQuestions = (List<NewModelRestriction>) request.getAttribute("selectedAtomicQuestions");
	for(NewModelRestriction atomicRestriction : testModel.getAtomicQuestionRestrictions()) {
		pageContext.setAttribute("atomicRestriction", atomicRestriction);
		if(selectedAtomicQuestions != null && selectedAtomicQuestions.contains(atomicRestriction)) {
			pageContext.setAttribute("wasSelected", atomicRestriction);
		} else {
			pageContext.setAttribute("wasSelected", null);
		}
%>
<label style="display: inline;">

<logic:present name="wasSelected">
<input alt="input.selectedAtomicQuestionRestrictions" type="checkbox" name="selectedAtomicQuestionRestrictions" value="<%= atomicRestriction.getIdInternal().toString() %>" checked="checked" />
</logic:present>
<logic:notPresent name="wasSelected">
<input alt="input.selectedAtomicQuestionRestrictions" type="checkbox" name="selectedAtomicQuestionRestrictions" value="<%= atomicRestriction.getIdInternal().toString() %>" />
</logic:notPresent>
<bean:message key="message.select.atomicQuestion" bundle="TESTS_RESOURCES" />
</label>

(<f:parameterLink page="/tests/testModels.do?method=deleteModelRestriction">
	<f:parameter id="returnTo" value="/tests/testModels.do?method=selectQuestions" />
	<f:parameter id="oid" name="atomicRestriction" property="idInternal" />
	<bean:message key="message.remove" bundle="TESTS_RESOURCES" />
</f:parameterLink>)

<fr:view name="atomicRestriction" property="question" schema="tests.question.simple">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>
<% } %>
<html:submit><bean:message key="label.button.add" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
</logic:notEmpty>

<h4><bean:message key="message.select.questionGroup.header" bundle="TESTS_RESOURCES" /></h4>
<logic:empty name="testModel" property="questionGroupRestrictions">
<span class="empty"><bean:message key="tests.questionGroups.empty" bundle="TESTS_RESOURCES" /></span>
</logic:empty>

<logic:notEmpty name="testModel" property="questionGroupRestrictions">
<fr:form action="/tests/testModels.do?method=selectQuestionGroupRestriction">
<table class="tstyle1"><tr><td colspan="2">
<table>
<%
	NewModelRestriction selectedQuestionGroupRestriction = (NewModelRestriction) request.getAttribute("selectedQuestionGroupRestriction");
	for(NewModelRestriction groupRestriction : testModel.getQuestionGroupRestrictions()) {
		pageContext.setAttribute("groupRestriction", groupRestriction);
		if(selectedQuestionGroupRestriction != null && selectedQuestionGroupRestriction.equals(groupRestriction)) {
			pageContext.setAttribute("wasSelected", groupRestriction);
		} else {
			pageContext.setAttribute("wasSelected", null);
		}
%>
<tr>
<td>
<label style="display: inline;">
<logic:present name="wasSelected">
<input alt="input.selectedQuestionGroup" type="radio" name="selectedQuestionGroup" value="<%= groupRestriction.getIdInternal().toString() %>" checked="checked" />
</logic:present>
<logic:notPresent name="wasSelected">
<input alt="input.selectedQuestionGroup" type="radio" name="selectedQuestionGroup" value="<%= groupRestriction.getIdInternal().toString() %>" />
</logic:notPresent>
<fr:view name="groupRestriction" property="question" schema="tests.questionGroup.name" layout="values" />
(<fr:view name="groupRestriction" property="question.allChildAtomicQuestionsCount" /> <bean:message key="message.select.atomicQuestion.header" bundle="TESTS_RESOURCES" />)
</label>
<f:parameterLink page="/tests/testModels.do?method=deleteModelRestriction">
	<f:parameter id="returnTo" value="/tests/testModels.do?method=selectQuestions" />
	<f:parameter id="oid" name="groupRestriction" property="idInternal" />
	<bean:message key="message.remove" bundle="TESTS_RESOURCES" />
</f:parameterLink>
</td>
</tr>
<% } %>
</table>
</td>
<td class="tdwarning tdclear">
<logic:messagesPresent property="selectedQuestionGroup" message="true">
	<html:messages id="errorMessage" property="selectedQuestionGroup" message="true" bundle="TESTS_RESOURCES"> 
		<span><bean:write name="errorMessage"/></span>
	</html:messages>
</logic:messagesPresent>
</td>
</tr><tr>
<tr>
<th><bean:message key="label.modelRestriction.parent" bundle="TESTS_RESOURCES" />:</th>
<td>
<fr:edit name="bean" schema="tests.testModel.selectQuestionGroup.modelGroup" id="select-question-group.model-group" nested="true">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
		<fr:property name="hideValidators" value="true" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidSelectQuestionGroupRestriction" />
</fr:edit>
</td>
<td class="tdwarning tdclear">
	<span><fr:message for="select-question-group.model-group" /></span>
</td>
</tr><tr>
<th><bean:message key="label.modelRestriction.count" bundle="TESTS_RESOURCES" />:</th>
<td>
<fr:edit name="bean"
         schema="tests.testModel.selectQuestionGroup.count"
         id="select-question-group.count"
         nested="true">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
		<fr:property name="hideValidators" value="true" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidSelectQuestionGroupRestriction" />
</fr:edit>
<bean:message key="message.questions" bundle="TESTS_RESOURCES" />
</td>
<td class="tdwarning tdclear">
	<span><fr:message for="select-question-group.count" /></span>
</td>
</tr><tr>
<th><bean:message key="label.modelRestriction.value" bundle="TESTS_RESOURCES" />:</th>
<td>
<fr:edit name="bean"
         schema="tests.testModel.selectQuestionGroup.value"
         id="select-question-group.value"
         nested="true">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true" />
		<fr:property name="hideValidators" value="true" />
	</fr:layout>
	<fr:destination name="invalid" path="/tests/testModels.do?method=invalidSelectQuestionGroupRestriction" />
</fr:edit>
</td>
<td class="tdwarning tdclear">
	<span><fr:message for="select-question-group.value" /></span>
</td>
</tr><tr>
<td>&nbsp;</td>
<td><html:submit><bean:message key="label.button.add" bundle="TESTS_RESOURCES" /></html:submit></td>
<td class="tdclear">&nbsp;</td>
</tr></table>
</fr:form>
</logic:notEmpty>
