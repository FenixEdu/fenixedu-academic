<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h2><bean:message key="message.question.edit" bundle="TESTS_RESOURCES" /> <ft:view schema="tests.question.type" layout="values" /></h2>

<ft:define id="atomicQuestion" type="net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion" />
<bean:define id="modelSelectDivId">testModelDiv<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="modelSelectId">testModel<bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="questionId"><bean:write name="atomicQuestion" property="idInternal" /></bean:define>
<bean:define id="modelSelectPath">/tests/questionBank.do?method=selectForModel#<bean:write name="modelSelectDivId" /></bean:define>

<ul>
<li><html:link page="/tests/questionBank.do?method=prepareDeleteQuestion" paramId="oid" paramName="atomicQuestion" paramProperty="idInternal">Apagar pergunta</html:link></li>
<logic:equal name="atomicQuestion" property="belongsToAllGroup" value="false">
	<li><html:link page="/tests/questionBank.do?method=prepareAssociateParent" paramId="oid" paramName="atomicQuestion" paramProperty="idInternal">Associar a outro grupo</html:link></li>
	<logic:notEqual name="atomicQuestion" property="parentQuestionGroupsCount" value="1">
		<li><html:link page="/tests/questionBank.do?method=prepareDisassociateParent" paramId="oid" paramName="atomicQuestion" paramProperty="idInternal">Disassociar de grupo</html:link></li>
	</logic:notEqual>
	<li><a href="javascript:switchDisplay('<%= modelSelectDivId %>')">Seleccionar</a></li>
</logic:equal>
</ul>

<logic:equal name="atomicQuestion" property="belongsToAllGroup" value="false">
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
			<fr:hidden slot="question" name="atomicQuestion" />
		</fr:create>
		<html:submit>Seleccionar</html:submit>
		<a href="javascript:switchDisplay('<%= modelSelectDivId %>')">Cancelar</a>
	</html:form>
	</td></tr></table>
</div>
</logic:equal>
<fr:hasMessages for="<%= modelSelectId %>">
	<script type="text/javascript">
		switchDisplay('<%= modelSelectDivId %>');
	</script>
</fr:hasMessages>

<h4>a) Sumário</h4>

<ft:view schema="tests.multipleChoiceQuestion.view">
	<ft:layout name="tabular">
		<ft:property name="classes" value="tstyle1" />
	</ft:layout>
</ft:view>

<logic:empty name="atomicQuestion" property="choices">
<span class="warning0">A pergunta nao tem qualquer opção</span>
</logic:empty>

<h4>b) Materiais de apresentação</h4>

<div>
	<strong><bean:message key="label.testElement.presentationMaterials" bundle="TESTS_RESOURCES" />:</strong>
	(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
	 	<f:parameter id="returnPath" value="/tests/questionBank.do?method=editTestElement" />
	 	<f:parameter id="returnId" name="atomicQuestion" property="idInternal" />
	 	<f:parameter id="contextKey" value="message.questionBank.manage" />
	 	Editar
	 </f:parameterLink>)
</div>
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>

<h4>c) Opções</h4>

<div id="viewShuffleDiv" class="switchBlock">
<ft:view schema="tests.multipleChoiceQuestion.view.shuffle" layout="flow" />
(<a href="javascript:switchDisplay('viewShuffleDiv');switchDisplay('editShuffleDiv')">Editar</a>)
</div>

<div id="editShuffleDiv" class="switchNone">
	<fr:form action="/tests/questionBank.do?method=editTestElement">
		<html:hidden property="oid" value="<%= atomicQuestion.getIdInternal().toString() %>" />
		<ft:edit schema="tests.multipleChoiceQuestion.edit.shuffle"
		         nested="true"
		         id="editShuffle">
			<ft:destination name="invalid" path="/tests/questionBank.do?method=editTestElement" />
		</ft:edit>
		<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('viewShuffleDiv');switchDisplay('editShuffleDiv')">Cancelar</a></span>
	</fr:form>
	<fr:hasMessages for="editShuffle">
		<script type="text/javascript">
			hideElement('viewShuffleDiv');
			showElement('editShuffleDiv');
		</script>
	</fr:hasMessages>
</div>

<ul>
<li>
	<html:link page="/tests/questionBank.do?method=createChoice" paramId="oid" paramName="atomicQuestion" paramProperty="idInternal">Inserir opção</html:link>
</li>
</ul>

<ft:view property="orderedChoices">
	<ft:layout name="flowLayout">
		<ft:property name="eachLayout" value="summary" />
		<ft:property name="emptyMessageKey" value="tests.choices.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>

<h4>d) Cotação</h4>

<logic:notEmpty name="atomicQuestion" property="grade">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=deleteGrade">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Apagar
	</f:parameterLink>
</li>
</ul>

<div class="questionBlock">
<ft:view schema="tests.atomicQuestion.grade.percentage" layout="values" />
</div>
</logic:notEmpty>

<logic:empty name="atomicQuestion" property="grade">
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

<h4>e) Validador</h4>

<logic:notEmpty name="atomicQuestion" property="validator">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=deleteValidator">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Apagar validador
	</f:parameterLink>
</li>

<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditValidator">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Editar validator
	</f:parameterLink>
</li>
</ul>
<div class="questionBlock">
	<ft:view schema="tests.atomicQuestion.validator" layout="flow" />
</div>
</logic:notEmpty>

<logic:empty name="atomicQuestion" property="validator">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditValidator">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Inserir validador
	</f:parameterLink>
</li>
</ul>
<span class="emptyMessage"><bean:message bundle="TESTS_RESOURCES" key="label.atomicQuestion.validator.null" /></span>
</logic:empty>

<h4>f) Correctores</h4>

<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditCorrector">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Gerir correctores
	</f:parameterLink>
</li>
</ul>

<div class="questionBlock">
<logic:notEmpty name="atomicQuestion" property="correctors">
	<fr:view name="atomicQuestion" property="correctors" schema="tests.corrector.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",width10em,width10em,width10em,width10em" />
			<fr:property name="link(delete)" value="/tests/questionBank.do?method=deleteCorrector" />
			<fr:property name="param(delete)" value="idInternal/oid" />
			<fr:property name="key(delete)" value="message.delete" />
			<fr:property name="bundle(delete)" value="TESTS_RESOURCES" />
			<fr:property name="order(delete)" value="1" />
			<fr:property name="link(up)" value="/tests/questionBank.do?method=switchCorrector" />
			<fr:property name="param(up)" value="idInternal/oid,relativePosition=-1" />
			<fr:property name="key(up)" value="message.up" />
			<fr:property name="bundle(up)" value="TESTS_RESOURCES" />
			<fr:property name="order(up)" value="2" />
			<fr:property name="visibleIfNot(up)" value="first" />
			<fr:property name="link(down)" value="/tests/questionBank.do?method=switchCorrector" />
			<fr:property name="param(down)" value="idInternal/oid,/relativePosition=1" />
			<fr:property name="key(down)" value="message.down" />
			<fr:property name="bundle(down)" value="TESTS_RESOURCES" />
			<fr:property name="order(down)" value="3" />
			<fr:property name="visibleIfNot(down)" value="last" />
			<fr:property name="sortBy" value="position" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
</div>
<logic:empty name="atomicQuestion" property="correctors">
	<span class="emptyMessage"><bean:message bundle="TESTS_RESOURCES" key="tests.correctors.empty" /></span>
</logic:empty>

<logic:equal name="atomicQuestion" property="belongsToAllGroup" value="true">
<h4>g) Pré-condição</h4>
<logic:notEmpty name="atomicQuestion" property="preCondition">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=deletePreCondition">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Apagar pré-condição
	</f:parameterLink>
</li>

<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditPreCondition">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Editar pré-condição
	</f:parameterLink>
</li>
</ul>
<div class="questionBlock">
	<ft:view schema="tests.question.preCondition" layout="flow" />
</div>
</logic:notEmpty>

<logic:empty name="atomicQuestion" property="preCondition">
<ul>
<li>
	<f:parameterLink page="/tests/questionBank.do?method=prepareEditPreCondition">
		<f:parameter id="oid" name="atomicQuestion" property="idInternal" />
		Inserir pré-condição
	</f:parameterLink>
</li>
</ul>
<span class="emptyMessage"><bean:message bundle="TESTS_RESOURCES" key="label.question.preCondition.null" /></span>
</logic:empty>
</logic:equal>
