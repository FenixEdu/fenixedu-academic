<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="modelRestriction" type="net.sourceforge.fenixedu.domain.tests.NewModelRestriction" />

<logic:equal name="modelRestriction" property="question.composite" value="true">
	<ft:view property="position" />) Grupo <ft:view property="question.name" />
	(<f:parameterLink page="/tests/testModels.do?method=unselectRestriction">
		<f:parameter id="oid" name="modelRestriction" property="idInternal" />
		Remover
	</f:parameterLink>)

	<div class="switchBlock" id="<%= "view-model-restriction-div" + modelRestriction.getIdInternal() %>">
	<table class="tstyle1 thright" style="width: 60em;">
	<tr><th class="width10em">Regra:</th>
		<td>Escolher <ft:view property="count" /> pergunta(s)</td>
		<td rowspan="2"><a href="javascript:switchDisplay('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');switchDisplay('view-model-restriction-div<%= modelRestriction.getIdInternal() %>')">Editar</a></td>
	</tr>
	<tr><th>Cotação:</th><td>
		<logic:empty name="modelRestriction" property="value">
			<bean:message key="label.atomicQuestion.grade.null" bundle="TESTS_RESOURCES" />
		</logic:empty>
		<logic:notEmpty name="modelRestriction" property="value">
			<ft:view property="value" />/<ft:view property="testModel.scale" />
		</logic:notEmpty>
	</td></tr>
	</table>
	</div>

	<div class="switchNone" id="<%= "edit-model-restriction-div" + modelRestriction.getIdInternal() %>">
		<html:form action="/tests/testModels.do?method=editModelRestriction">
			<html:hidden property="oid" value="<%= modelRestriction.getIdInternal().toString() %>" />
			<ft:edit id="<%= "edit-model-restriction" + modelRestriction.getIdInternal() %>"
			         schema="tests.modelRestriction.edit.questionGroup"
			         nested="true">
				<ft:layout name="tabular">
					<ft:property name="style" value="width: 60em;" />
					<ft:property name="classes" value="tstyle1" />
					<ft:property name="columnClasses" value="width10em,," />
				</ft:layout>
				<ft:destination name="invalid" path="/tests/testModels.do?method=editModelRestriction" />
			</ft:edit>
			<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
			<span class="switchInline"><a href="javascript:switchDisplay('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');switchDisplay('view-model-restriction-div<%= modelRestriction.getIdInternal() %>')">Cancelar</a></span>
		</html:form>
	</div>
	<fr:hasMessages for="<%= "edit-model-restriction" + modelRestriction.getIdInternal() %>">
		<script type="text/javascript">
			showElement('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');
			hideElement('view-model-restriction-div<%= modelRestriction.getIdInternal() %>');
		</script>
	</fr:hasMessages>
</logic:equal>

<logic:equal name="modelRestriction" property="question.composite" value="false">
	<ft:view property="position" />) Pergunta
	(<f:parameterLink page="/tests/testModels.do?method=unselectRestriction">
		<f:parameter id="oid" name="modelRestriction" property="idInternal" />
		Remover
	</f:parameterLink>)

	<ft:view property="question" schema="tests.question.simple">
		<ft:layout name="tabular" >
					<ft:property name="style" value="width: 60em;" />
			<ft:property name="classes" value="tstyle1 thright mbottom0" />
			<ft:property name="columnClasses" value="width10em," />
		</ft:layout>
	</ft:view>
	<div class="switchBlock" id="<%= "view-model-restriction-div" + modelRestriction.getIdInternal() %>">
		<table class="tstyle7 thright mtop0" style="width: 60em;"><tr>
		<th class="width10em">Cotação:</th>
		<td>
			<logic:empty name="modelRestriction" property="value">
				<bean:message key="label.atomicQuestion.grade.null" bundle="TESTS_RESOURCES" />
			</logic:empty>
			<logic:notEmpty name="modelRestriction" property="value">
				<ft:view property="value" />/<ft:view property="testModel.scale" />
			</logic:notEmpty>
			(<a href="javascript:switchDisplay('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');switchDisplay('view-model-restriction-div<%= modelRestriction.getIdInternal() %>')">Editar</a>)
		</td>
		</tr></table>
	</div>

	<div class="switchNone" id="<%= "edit-model-restriction-div" + modelRestriction.getIdInternal() %>">
		<html:form action="/tests/testModels.do?method=editModelRestriction">
			<html:hidden property="oid" value="<%= modelRestriction.getIdInternal().toString() %>" />
			<ft:edit id="<%= "edit-model-restriction" + modelRestriction.getIdInternal() %>"
			         schema="tests.modelRestriction.edit.atomicQuestion"
			         nested="true">
				<ft:layout name="tabular">
					<ft:property name="style" value="width: 60em;" />
					<ft:property name="classes" value="tstyle7 thright mtop0" />
					<ft:property name="columnClasses" value="width10em,," />
				</ft:layout>
				<ft:destination name="invalid" path="/tests/testModels.do?method=editModelRestriction" />
			</ft:edit>
			<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
			<span class="switchInline"><a href="javascript:switchDisplay('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');switchDisplay('view-model-restriction-div<%= modelRestriction.getIdInternal() %>')">Cancelar</a></span>
		</html:form>
	</div>
	<fr:hasMessages for="<%= "edit-model-restriction" + modelRestriction.getIdInternal() %>">
		<script type="text/javascript">
			switchDisplay('edit-model-restriction-div<%= modelRestriction.getIdInternal() %>');
			switchDisplay('view-model-restriction-div<%= modelRestriction.getIdInternal() %>');
		</script>
	</fr:hasMessages>
</logic:equal>



