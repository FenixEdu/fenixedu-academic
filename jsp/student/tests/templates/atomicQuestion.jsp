<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="atomicQuestion" type="net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion" />

<bean:define id="stateClasses" type="java.util.Map" name="stateClasses" scope="request" toScope="page" />
<bean:define id="stateClass" value="<%= stateClasses.get(atomicQuestion.getState()).toString() %>" />
<div class="qcontainer <%= stateClass %>">

<div class="qheader">
	<strong>
	<ft:view property="path">
		<ft:layout name="flowLayout">
			<ft:property name="htmlSeparator" value="." />
		</ft:layout>
	</ft:view>) <ft:view property="questionType" />
	</strong>
	<ft:view property="grade" layout="format" />
	<em class="qshowstate">
		<ft:view property="state" />
	</em>
</div>

<div class="qbody">
	<logic:notEmpty name="atomicQuestion" property="presentationMaterials">
	<ft:view schema="tests.testElement.simple">
		<ft:layout name="values">
			<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
		</ft:layout>
	</ft:view>
	</logic:notEmpty>
</div>

<bean:define id="schema">tests.answer.details-for.<bean:write name="atomicQuestion" property="class.simpleName" /></bean:define>
<bean:define id="answer_div">answer_div<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="answer_id">atomic_question_id<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="testGroupId" type="java.lang.String"><bean:write name="atomicQuestion" property="test.testGroup.idInternal" /></bean:define>

<span class="switchInline">
<span class="qlinks">
	(
	<logic:equal name="atomicQuestion" property="answerable" value="true">
	<a href="javascript:switchDisplay('<%= answer_div %>')">Responder</a>
	</logic:equal>
	
	<logic:equal name="atomicQuestion" property="answerEditable" value="true">
	<a href="javascript:switchDisplay('<%= answer_div %>')">Editar</a>
	</logic:equal>
	<logic:equal name="atomicQuestion" property="answerDeletable" value="true">
	, <f:parameterLink page="/tests/tests.do?method=deleteAnswer">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Apagar
	</f:parameterLink>
	</logic:equal>
	
	<logic:equal name="atomicQuestion" property="canGiveUp" value="true">
	, <f:parameterLink page="/tests/tests.do?method=giveUpQuestion">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Desistir
	</f:parameterLink>
	</logic:equal>
	)
</span>
</span>

<div class="qanswer">
<div id="<%= answer_div %>" class="switchNone">
	<fr:form action="/tests/tests.do?method=answerQuestion">
		<html:hidden property="oid" value="<%= testGroupId %>" />
		<ft:edit schema="<%= schema %>"
		         id="<%= answer_id %>">
			<ft:layout name="flow">
				<ft:property name="labelExcluded" value="true" />
			</ft:layout>
		</ft:edit>
		<div>
		<html:submit>Responder</html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('<%= answer_div %>')">Cancelar</a></span>
		</div>
	</fr:form>
</div>
<fr:hasMessages for="<%= answer_id %>">
	<script type="text/javascript">
		showElement('<%= answer_div %>');
	</script>
</fr:hasMessages>
</div>

</div>
