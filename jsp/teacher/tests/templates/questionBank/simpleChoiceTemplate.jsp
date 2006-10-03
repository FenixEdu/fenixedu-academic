<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="choice" />

<div>
<strong>
Opção
<ft:view property="position" />
</strong> 
(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="choice" property="idInternal" />
	 	<f:parameter id="returnPath" value="/tests/questionBank.do?method=editTestElement" />
	 	<f:parameter id="returnId" name="choice" property="multipleChoiceQuestion.idInternal" />
	 	<f:parameter id="contextKey" value="message.questionBank.manage" />
	 	editar
	 </f:parameterLink>,
<html:link page="/tests/questionBank.do?method=deleteChoice" paramId="oid" paramName="choice" paramProperty="idInternal">apagar</html:link>, 

<logic:equal name="choice" property="first" value="false">
	<f:parameterLink page="/tests/questionBank.do?method=switchChoice">
		<f:parameter id="relativePosition" value="-1" />
		<f:parameter id="oid" name="choice" property="idInternal" />
		cima
	</f:parameterLink>
</logic:equal>
<logic:equal name="choice" property="first" value="true">cima</logic:equal>, 

<logic:equal name="choice" property="last" value="false">
	<f:parameterLink page="/tests/questionBank.do?method=switchChoice">
		<f:parameter id="relativePosition" value="1" />
		<f:parameter id="oid" name="choice" property="idInternal" />
		baixo
	</f:parameterLink>)
</logic:equal>
<logic:equal name="choice" property="last" value="true">baixo)</logic:equal>
</div>

<ft:view property="orderedPresentationMaterials">
	<ft:layout name="flowLayout">
		<ft:property name="emptyMessageKey" value="tests.presentationMaterials.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>
