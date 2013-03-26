<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h2><bean:message key="message.question.view" bundle="TESTS_RESOURCES" />
    <ft:view schema="tests.question.type" layout="values" /></h2>

<ft:define id="questionGroup" type="net.sourceforge.fenixedu.domain.tests.NewAllGroup" />
<bean:define id="questionGroup" name="questionGroup" toScope="request" type="net.sourceforge.fenixedu.domain.tests.NewAllGroup" />
<bean:define id="modelSelectDivId">testModelDiv<bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="modelSelectId">testModel<bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="questionId"><bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="modelSelectPath">/tests/questionBank.do?method=selectForModel#<bean:write name="modelSelectDivId" /></bean:define>

<ul>
<li><html:link page="/tests/questionBank.do?method=deleteQuestion" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Apagar pergunta</html:link></li>
<logic:equal name="questionGroup" property="belongsToAllGroup" value="false">
	<li><html:link page="/tests/questionBank.do?method=prepareAssociateParent" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Associar a outro grupo</html:link></li>
	<logic:notEqual name="questionGroup" property="parentQuestionGroupsCount" value="1">
		<li><html:link page="/tests/questionBank.do?method=prepareDisassociateParent" paramId="oid" paramName="questionGroup" paramProperty="idInternal">Disassociar de grupo</html:link></li>
	</logic:notEqual>
	<li><a href="javascript:switchDisplay('<%= modelSelectDivId %>')">Seleccionar</a></li>
</logic:equal>
</ul>

<div id="<%= modelSelectDivId %>" class="dnone">
	<table><tr><td style="background-color: #f0f0f0; padding: 0.1em 0.5em;">
	<strong><bean:message key="label.testModel.name-current" bundle="TESTS_RESOURCES" />:</strong>
	<ft:view schema="tests.question.testModels" layout="values" />
	
	<html:form action="<%= modelSelectPath %>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= questionId %>" />
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

<h4>a) Sum�rio</h4>

<ft:view schema="tests.allGroup.view">
	<ft:layout name="tabular">
		<ft:property name="classes" value="tstyle1" />
	</ft:layout>
</ft:view>

<logic:equal name="questionGroup" property="gradeComplete" value="false">
<span class="warning0">Deve atribuir cota��o a todas as alineas desta pergunta</span>
</logic:equal>

<h4>b) Materiais de apresenta��o</h4>

<div class="questionBlockHeader">
	<strong><bean:message key="label.testElement.presentationMaterials" bundle="TESTS_RESOURCES" />:</strong>
	(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="questionGroup" property="idInternal" />
	 	<f:parameter id="returnPath" value="/tests/questionBank.do?method=editTestElement" />
	 	<f:parameter id="returnId" name="questionGroup" property="idInternal" />
	 	<f:parameter id="contextKey" value="message.questionBank.manage" />
	 	Editar
	 </f:parameterLink>)
</div>
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>

<h4>c) Al�neas</h4>

<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareCreateAtomicQuestion">
		<f:parameter id="oid" name="questionGroup" property="idInternal" />
		Criar al�nea
	</f:parameterLink>
</li>
</ul>

<ft:view property="orderedChildAtomicQuestions">
	<ft:layout name="flowLayout">
		<ft:property name="eachLayout" value="summary" />
		<ft:property name="emptyMessageKey" value="tests.atomicQuestions.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>

<logic:equal name="questionGroup" property="topAllGroup" value="false">
<h4>d) Cota��o</h4>

<logic:notEmpty name="questionGroup" property="grade">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=deleteGrade">
		<f:parameter id="oid" name="questionGroup" property="idInternal" />
		Apagar
	</f:parameterLink>
</li>
</ul>

<div class="questionBlock">
<ft:view schema="tests.atomicQuestion.grade.percentage" layout="values" />
</div>
</logic:notEmpty>

<logic:empty name="questionGroup" property="grade">
<div class="questionBlock">
	<fr:form action="/tests/questionBank.do?method=createGrade">
		<ft:edit schema="tests.atomicQuestion.grade"
		         id="create-grade"
		         nested="true"
		         layout="flow" />
		<html:submit>Criar</html:submit>
	</fr:form>
</div>
</logic:empty>
</logic:equal>

<logic:equal name="questionGroup" property="belongsToAllGroup" value="true">
<h4>e) Pr�-condi��o</h4>
<logic:notEmpty name="questionGroup" property="preCondition">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=deletePreCondition">
		<f:parameter id="oid" name="questionGroup" property="idInternal" />
		Apagar pr�-condi��o
	</f:parameterLink>
</li>

<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditPreCondition">
		<f:parameter id="oid" name="questionGroup" property="idInternal" />
		Editar pr�-condi��o
	</f:parameterLink>
</li>
</ul>
<div class="questionBlock">
	<ft:view schema="tests.question.preCondition" layout="flow" />
</div>
</logic:notEmpty>

<logic:empty name="questionGroup" property="preCondition">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditPreCondition">
		<f:parameter id="oid" name="questionGroup" property="idInternal" />
		Inserir pr�-condi��o
	</f:parameterLink>
</li>
</ul>
<span class="emptyMessage"><bean:message bundle="TESTS_RESOURCES" key="label.question.preCondition.null" /></span>
</logic:empty>
</logic:equal>
