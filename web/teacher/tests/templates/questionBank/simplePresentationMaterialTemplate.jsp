<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="presentationMaterial" type="net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial" />
<bean:define id="editPresentationMaterialDivId">edit-presentationMaterial-div<bean:write name="presentationMaterial" property="idInternal" /></bean:define>
<bean:define id="viewPresentationMaterialDivId">view-presentationMaterial-div<bean:write name="presentationMaterial" property="idInternal" /></bean:define>
<bean:define id="editPresentationMaterialId"><bean:write name="presentationMaterial" property="idInternal" /></bean:define>
<bean:define id="viewPresentationMaterialId"><bean:write name="presentationMaterial" property="idInternal" /></bean:define>
<bean:define id="testElementId"><bean:write name="bean" property="testElement.idInternal" /></bean:define>
<bean:define id="schema">tests.presentationMaterial.edit-details-for.<bean:write name="presentationMaterial" property="presentationMaterialType.name" /></bean:define>

<div class="questionBlock">
<div>
<h4 class="dinline pbottom05">
Material <ft:view property="position" />
</h4> 
(<span class="switchInline"><a href="javascript:switchDisplay('<%= editPresentationMaterialDivId %>');switchDisplay('<%= viewPresentationMaterialDivId %>')">editar</a>
,</span>
<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareDeletePresentationMaterial">apagar
	<f:parameter id="oid" name="presentationMaterial" property="idInternal" />
	<f:parameter id="returnPath" name="bean" property="returnPath" />
	<f:parameter id="returnId" name="bean" property="returnId" />
	<f:parameter id="contextKey" name="bean" property="contextKey" />
</f:parameterLink>)

(ordenar:
<logic:equal name="presentationMaterial" property="first" value="false">
<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=switchPresentationMaterial">
	<f:parameter id="relativePosition" value="-1" />
	<f:parameter id="oid" name="presentationMaterial" property="idInternal" />
	<f:parameter id="returnPath" name="bean" property="returnPath" />
	<f:parameter id="returnId" name="bean" property="returnId" />
	<f:parameter id="contextKey" name="bean" property="contextKey" />
	cima
</f:parameterLink>
</logic:equal>
<logic:equal name="presentationMaterial" property="first" value="true">cima</logic:equal>
,
<logic:equal name="presentationMaterial" property="last" value="false">
<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=switchPresentationMaterial">
	<f:parameter id="relativePosition" value="1" />
	<f:parameter id="oid" name="presentationMaterial" property="idInternal" />
	<f:parameter id="returnPath" name="bean" property="returnPath" />
	<f:parameter id="returnId" name="bean" property="returnId" />
	<f:parameter id="contextKey" name="bean" property="contextKey" />
	baixo
</f:parameterLink>)
</logic:equal>
<logic:equal name="presentationMaterial" property="last" value="true">baixo)</logic:equal>
</div>

<div id="<%= viewPresentationMaterialDivId %>" class="switchBlock">
<ft:view layout="flowLayout" />
</div>
</div>

<div id="<%= editPresentationMaterialDivId %>" class="switchNone">
	<table><tr><td style="background-color: #f0f0f0; padding: 0.1em 0.5em;">
	<html:form action="/tests/questionBank/presentationMaterial.do?method=editPresentationMaterial">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= testElementId %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnPath" property="returnPath" name="bean" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.returnId" property="returnId" name="bean" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contextKey" property="contextKey" name="bean" />
		<ft:edit id="<%= editPresentationMaterialId %>"
		         schema="<%= schema %>"
		         nested="true"
		         layout="tabular" />
		<html:submit><bean:message key="label.button.continue" bundle="TESTS_RESOURCES" /></html:submit>
		<span class="switchInline"><a href="javascript:switchDisplay('<%= editPresentationMaterialDivId %>');switchDisplay('<%= viewPresentationMaterialDivId %>')">Cancelar</a></span>
	</html:form>
	</td></tr></table>
</div>
<fr:hasMessages for="<%= editPresentationMaterialId %>">
	<script type="text/javascript">
		showElement('<%= editPresentationMaterialDivId %>');
		hideElement('<%= viewPresentationMaterialDivId %>');
	</script>
</fr:hasMessages>
