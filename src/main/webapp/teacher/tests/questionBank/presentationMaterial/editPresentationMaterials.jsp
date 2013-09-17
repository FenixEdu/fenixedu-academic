<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h3><fr:view name="bean" property="contextLabel" /></h3>
<h2><bean:message key="title.editPresentationMaterials" bundle="TESTS_RESOURCES" /></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PresentationMaterialBean" />
<bean:define id="returnPath" name="bean" property="returnPath" type="java.lang.String" />

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/teacher.js" %>"></script>

<ul>
<li>
<f:parameterLink page="<%= returnPath %>">
	<f:parameter id="oid" name="bean" property="returnId" />
	Voltar
</f:parameterLink>
</li>
<li>
<a href="javascript:switchDisplay('create-presentation-material')">Criar material</a>
</li>
</ul>
<div id="create-presentation-material" class="dnone">
	<table><tr><td style="background-color: #f0f0f0; padding: 0.1em 0.5em;">
	<html:form action="/tests/questionBank/presentationMaterial.do?method=prepareCreatePresentationMaterial">
		<fr:edit id="choose-material-type"
		         schema="tests.presentationMaterial.chooseType"
		         name="bean"
		         nested="true"
		         layout="flow">
			<fr:destination name="invalid" path="/tests/questionBank/presentationMaterial.do?method=invalidPrepareCreatePresentationMaterial" />
		</fr:edit>
		<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
		<a href="javascript:switchDisplay('create-presentation-material')">Cancelar</a>
	</html:form>
	</td></tr></table>
</div>

<fr:hasMessages for="choose-material-type">
	<script type="text/javascript">
		switchDisplay('create-presentation-material');
	</script>
</fr:hasMessages>

<fr:view name="bean" property="testElement" schema="tests.testElement.complex" layout="values" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
