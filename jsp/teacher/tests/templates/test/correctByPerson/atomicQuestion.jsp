<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<%@page import="net.sourceforge.fenixedu.domain.tests.NewAnswer"%>
<ft:define id="atomicQuestion" type="net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion" />

<strong>
<ft:view property="path">
	<ft:layout name="flowLayout">
		<ft:property name="htmlSeparator" value="." />
	</ft:layout>
</ft:view>) Pergunta
</strong>
(<ft:view property="grade.value" />/<ft:view property="grade.scale" />)

<logic:notEmpty name="atomicQuestion" property="presentationMaterials">
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>
</logic:notEmpty>


<bean:define id="personId"><bean:write name="person" property="idInternal" /></bean:define>
<bean:define id="testGroupId"><bean:write name="atomicQuestion" property="test.testGroup.idInternal" /></bean:define>
<bean:define id="correctId">correct-id-<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="correctDiv">correct-div-<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="correctControlDiv">correct-control-div-<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" />
<bean:define id="editLink">/tests/tests.do?method=correctByPerson#<bean:write name="correctDiv" /></bean:define>
<bean:define id="schema">tests.answer.view.<bean:write name="atomicQuestion" property="class.simpleName" /></bean:define>
<% pageContext.setAttribute("answer", atomicQuestion.getAnswer(person)); %>

<div style="padding-left: 1em;">
	<fr:view name="answer" schema="<%= schema %>">
		<ft:layout name="flow">
			<ft:property name="labelExcluded" value="true" />
		</ft:layout>
	</fr:view>
</div>

<div id="<%= correctControlDiv %>">

<logic:empty name="answer" property="percentage">
	<bean:message bundle="TESTS_RESOURCES" key="tests.percentage.empty" />
</logic:empty>
<logic:notEmpty name="answer" property="percentage">
<strong><bean:message bundle="TESTS_RESOURCES" key="label.tests.corrector.percentage" />:</strong> <fr:view name="answer" layout="computed-grade"  />
</logic:notEmpty>

<span class="switchInline">(<a href="javascript:switchDisplay('<%= correctDiv %>');switchDisplay('<%= correctControlDiv %>')">Editar</a>)</span></div>

<div id="<%= correctDiv %>" class="switchNone">
<fr:form action="<%= editLink %>">
	<html:hidden property="personId" value="<%= personId %>" />
	<html:hidden property="testGroupId" value="<%= testGroupId %>" />
	<fr:edit name="answer"
	         schema="tests.answer.percentage"
	         id="<%= correctId %>"
	         nested="true">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdwarning tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit>Submeter</html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('<%= correctDiv %>');switchDisplay('<%= correctControlDiv %>')">Cancelar</a></span>
</fr:form>
</div>
<fr:hasMessages for="<%= correctId %>">
	<script type="text/javascript">
		showElement('<%= correctDiv %>');
		hideElement('<%= correctControlDiv %>');
	</script>
</fr:hasMessages>
