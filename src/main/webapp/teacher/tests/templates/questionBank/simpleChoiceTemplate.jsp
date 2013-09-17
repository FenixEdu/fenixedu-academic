<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<ft:define id="choice" />

<div>
<strong>
Op��o
<ft:view property="position" />
</strong> 
(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="choice" property="externalId" />
	 	<f:parameter id="returnPath" value="/tests/questionBank.do?method=editTestElement" />
	 	<f:parameter id="returnId" name="choice" property="multipleChoiceQuestion.externalId" />
	 	<f:parameter id="contextKey" value="message.questionBank.manage" />
	 	editar
	 </f:parameterLink>,
<html:link page="/tests/questionBank.do?method=deleteChoice" paramId="oid" paramName="choice" paramProperty="externalId">apagar</html:link>, 

<logic:equal name="choice" property="first" value="false">
	<f:parameterLink page="/tests/questionBank.do?method=switchChoice">
		<f:parameter id="relativePosition" value="-1" />
		<f:parameter id="oid" name="choice" property="externalId" />
		cima
	</f:parameterLink>
</logic:equal>
<logic:equal name="choice" property="first" value="true">cima</logic:equal>, 

<logic:equal name="choice" property="last" value="false">
	<f:parameterLink page="/tests/questionBank.do?method=switchChoice">
		<f:parameter id="relativePosition" value="1" />
		<f:parameter id="oid" name="choice" property="externalId" />
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
