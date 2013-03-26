<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><fr:view name="bean" property="contextLabel" /></h3>
<h2><bean:message key="title.deletePresentationMaterial" bundle="TESTS_RESOURCES" /></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PresentationMaterialBean" />
<bean:define id="presentationMaterial" name="presentationMaterial" type="net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial" />
<bean:define id="presentationMaterialId"><bean:write name="presentationMaterial" property="idInternal" /></bean:define>

<h4>Vai apagar</h4>
<html:form action="/tests/questionBank/presentationMaterial.do?method=deletePresentationMaterial">
	<fr:edit name="bean" nested="true" id="delete-presentation-material" visible="false" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= presentationMaterialId %>" />
	<div>
		<fr:view name="presentationMaterial" />
	</div>
	<html:submit>Apagar</html:submit>
	<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
		<f:parameter id="oid" name="bean" property="testElement.idInternal" />
		<f:parameter id="returnPath" name="bean" property="returnPath" />
		<f:parameter id="returnId" name="bean" property="returnId" />
		<f:parameter id="contextKey" name="bean" property="contextKey" />
		Cancelar
	</f:parameterLink>
</html:form>

