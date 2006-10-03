<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>Grupo <ft:view property="name" /></h2>
<span class="questionBank">
<strong><bean:message key="tests.questionBank.viewing" bundle="TESTS_RESOURCES" />:</strong> <ft:view property="questionBank" layout="short" />
</span>

<ft:define id="questionGroup" type="net.sourceforge.fenixedu.domain.tests.NewQuestionGroup" />
<bean:define id="questionGroup" name="questionGroup" type="net.sourceforge.fenixedu.domain.tests.NewQuestionGroup" toScope="request" />
<bean:define id="modelSelectDivId">testModelDiv<bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="modelSelectId">testModel<bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="questionId"><bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="modelSelectPath">/tests/questionBank.do?method=selectForModel#<bean:write name="modelSelectDivId" /></bean:define>

<ul>
<li><html:link page="/tests/questionBank.do?method=prepareCreateQuestionGroup" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Criar grupo</html:link></li>
<li><html:link page="/tests/questionBank.do?method=prepareDeleteQuestion" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Apagar grupo</html:link></li>
<li><html:link page="/tests/questionBank.do?method=prepareAssociateParent" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Associar a outro grupo</html:link></li>
<logic:notEqual name="questionGroup" property="parentQuestionGroupsCount" value="1">
<li><html:link page="/tests/questionBank.do?method=prepareDisassociateParent" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Disassociar de grupo</html:link></li>
</logic:notEqual>
<li><html:link page="/tests/questionBank.do?method=prepareCreateAtomicQuestion" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Criar pergunta</html:link></li>
<li><a href="javascript:switchDisplay('<%= modelSelectDivId %>')">Seleccionar</a></li>
</ul>

<div id="<%= modelSelectDivId %>" class="dnone">
	<table><tr><td style="background-color: #f0f0f0; padding: 0.1em 0.5em;">
	<strong><bean:message key="label.testModel.name-current" bundle="TESTS_RESOURCES" />:</strong>
	<ft:view schema="tests.question.testModels" layout="values" />
	
	<html:form action="<%= modelSelectPath %>">
		<html:hidden property="oid" value="<%= questionId %>" />
		<fr:create type="net.sourceforge.fenixedu.domain.tests.NewModelRestriction"
		           schema="tests.modelRestriction.create"
		           id="<%= modelSelectId %>"
		           nested="true">
			<fr:layout name="flow" />
			<fr:hidden slot="question" name="questionGroup" />
		</fr:create>
		<html:submit>Seleccionar</html:submit>
		<a href="javascript:switchDisplay('<%= modelSelectDivId %>')">Cancelar</a>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="<%= modelSelectId %>">
	<script type="text/javascript">
		switchDisplay('<%= modelSelectDivId %>');
	</script>
</fr:hasMessages>

<h4>a) Sumário</h4>

<div id="viewNameDiv" class="switchBlock">
<ft:view schema="tests.questionGroup.name" layout="flow" />
(<a href="javascript:switchDisplay('viewNameDiv');switchDisplay('editNameDiv')">Editar</a>)
</div>

<div id="editNameDiv" class="switchNone">
	<fr:form action="/tests/questionBank.do?method=editTestElement">
		<html:hidden property="oid" value="<%= questionGroup.getIdInternal().toString() %>" />
		<ft:edit schema="tests.questionGroup.edit.name"
		         nested="true"
		         id="editName">
			<ft:destination name="invalid" path="/tests/questionBank.do?method=editTestElement" />
		</ft:edit>
		<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('viewNameDiv');switchDisplay('editNameDiv')">Cancelar</a></span>
	</fr:form>
	<fr:hasMessages for="editName">
		<script type="text/javascript">
			hideElement('viewNameDiv');
			showElement('editNameDiv');
		</script>
	</fr:hasMessages>
</div>

<ft:view schema="tests.questionGroup.view">
	<ft:layout name="tabular">
		<ft:property name="classes" value="tstyle1" />
	</ft:layout>
</ft:view>

<logic:equal name="questionGroup" property="gradeComplete" value="false">
<span class="warning0">Deve atribuir cotação a todas as perguntas deste grupo</span>
</logic:equal>

<h4>b) Perguntas</h4>

<ft:view property="orderedChildAtomicQuestions" >
	<ft:layout name="flowLayout">
		<ft:property name="eachClasses" value="atomicQuestion" />
		<ft:property name="eachLayout" value="summary" />
		<ft:property name="emptyMessageKey" value="tests.atomicQuestions.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>
