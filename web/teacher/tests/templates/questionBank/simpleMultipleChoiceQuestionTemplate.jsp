<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="testElement" type="net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion" />
<bean:define id="questionGroup" name="questionGroup" type="net.sourceforge.fenixedu.domain.tests.NewQuestionGroup" />
<bean:define id="modelSelectDivId">testModelDiv<bean:write name="testElement" property="idInternal" /></bean:define>
<bean:define id="modelSelectId">testModel<bean:write name="testElement" property="idInternal" /></bean:define>
<bean:define id="questionId"><bean:write name="questionGroup" property="idInternal" /></bean:define>
<bean:define id="modelSelectPath">/tests/questionBank.do?method=selectForModel#<bean:write name="modelSelectDivId" /></bean:define>

<div>
<strong>
<logic:equal name="testElement" property="belongsToAllGroup" value="true">
Al�nea
<ft:view property="path">
	<ft:layout name="flowLayout">
		<ft:property name="htmlSeparator" value="." />
	</ft:layout>
</ft:view>
</logic:equal>
<logic:equal name="testElement" property="belongsToAllGroup" value="false">
Pergunta
</logic:equal>
</strong>
(<html:link page="/tests/questionBank.do?method=editTestElement" paramId="oid" paramName="testElement" paramProperty="idInternal">editar</html:link>, 
<f:parameterLink page="/tests/questionBank.do?method=prepareDeleteQuestion">
	<f:parameter id="parentQuestionGroupOid" name="questionGroup" property="idInternal" />
	<f:parameter id="oid" name="testElement" property="idInternal" />
	apagar
</f:parameterLink>
<logic:equal name="testElement" property="belongsToAllGroup" value="false">
, <a href="javascript:switchDisplay('<%= modelSelectDivId %>')">seleccionar</a>
</logic:equal>)
<logic:equal name="testElement" property="belongsToAllGroup" value="false">
Seleccionado para <ft:view property="modelRestrictionsCount" /> modelo(s) de teste
</logic:equal>
</div>

<div id="<%= modelSelectDivId %>" class="dnone">
	<table><tr><td style="background-color: #f0f0f0; padding: 0.1em 0.5em;">
	<strong><bean:message key="label.testModel.name-current" bundle="TESTS_RESOURCES" />:</strong>
	<ft:view schema="tests.question.testModels" layout="values" />
	
	<html:form action="<%= modelSelectPath %>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= questionId %>"  />
		<fr:create type="net.sourceforge.fenixedu.domain.tests.NewModelRestriction"
		           schema="tests.modelRestriction.create"
		           id="<%= modelSelectId %>"
		           nested="true"
		           layout="flow">
			<fr:hidden slot="question" name="testElement" />
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

<ft:view schema="tests.multipleChoiceQuestion.simple">
	<ft:layout name="tabular">
		<ft:property name="classes" value="tstyle1" />
	</ft:layout>
</ft:view>
